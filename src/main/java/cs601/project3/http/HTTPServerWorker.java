package cs601.project3.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.logging.Level;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.handler.ErrorHandler;
import cs601.project3.handler.Handler;

/**
 * 
 * @author pontakornp
 *
 *	Works as a thread of Http server to handle each client
 */
public class HTTPServerWorker implements Runnable{
	private Socket sock;
	private HashMap<String, Handler> requestMap;
	
	public HTTPServerWorker(Socket sock, HashMap<String, Handler> requestMap) {
		this.sock = sock;
		this.requestMap = requestMap;
	}
	
	/**
	 * Handles request and send response to client
	 */
	public void run() {
		try (
			InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
		){
			String headers = "";
			String requestLine = oneLine(instream);
			headers += requestLine + "\n";
			String line = oneLine(instream);
			int length = 0;
			while(line != null && !line.trim().isEmpty()) {
				headers += line + "\n";
				line = oneLine(instream);
				if(line == null || line.trim().isEmpty() || line.equals("")) {
					break;
				}
				if(line.toLowerCase().startsWith("content-length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
			}
			ChatAndSearchApplicationLogger.write(Level.INFO, "Request Headers: \n" + headers, 0);
			byte[] bytes = new byte[length];
			int read = sock.getInputStream().read(bytes);
			while(read < length) {
				read += sock.getInputStream().read(bytes, read, (bytes.length - read));
			}
			ChatAndSearchApplicationLogger.write(Level.INFO, "Bytes expected: " + length + " Bytes read: " + read, 0);
			String body = new String(bytes);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Body: " + body, 0);
			handleRequest(requestLine, body, writer);
		} catch (IOException e) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Instream or PrintWriter throws IOException", 1);
		}
	}
	
	/**
	 * Read a line of bytes until \n character or stream has ended.
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != '\n' && (int)b != -1) {
			bout.write(b);
			b = (byte) instream.read();
		}
		return new String(bout.toByteArray());
	}
	
	/**
	 * Handles request by setting appropriate status for each request and call other methods to help
	 * @param requestLine
	 * @param body
	 * @param writer
	 */
	private void handleRequest(String requestLine, String body, PrintWriter writer) {
		String[] reqs = requestLine.split(" ");
		String method = "";
		String path = "";
		String protocol = "";
		int statusCode = 200;
		if(reqs.length != 3) { //check if requestLineParts has three components
			statusCode = 400;
			ChatAndSearchApplicationLogger.write(Level.INFO, "Resquest line does not have 3 components, Status Code is " + statusCode + " Length:" + reqs.length, 0);
		} else {
			method = reqs[0].trim();
			path = reqs[1].trim();
			protocol = reqs[2].trim();
		}
		if(!protocol.equals("HTTP/1.0") && !protocol.equals("HTTP/1.1")) { //check if support the protocol version specified
			statusCode = 400;
			ChatAndSearchApplicationLogger.write(Level.INFO, "Protocol not supported, Status Code is " + statusCode + " Protocol:" + protocol, 0);
		} else if(!method.equals("GET") && !method.equals("POST")) { //check if method not get return 405
			statusCode = 405;
			ChatAndSearchApplicationLogger.write(Level.INFO, "Method is not GET or POST, Status Code is " + statusCode + " Method:" + method, 0);
		}
		// check if there is a query string passed by the get method
		if(path.indexOf("?") != -1) {
			path = path.substring(0, path.indexOf("?"));
		}
		try {
			body = URLDecoder.decode(body, "UTF-8");
			ChatAndSearchApplicationLogger.write(Level.INFO, "Body after decoding: " + body, 0);
		} catch (UnsupportedEncodingException e) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Error when decoding body " + body, 1);
		}
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		if(method.equals("POST")) {
			if(!body.equals("")) {
				int paramNum = body.split("&").length;
				ChatAndSearchApplicationLogger.write(Level.INFO, "Number of body parameters: " + paramNum, 0);
				for(int i = 0; i < paramNum; i++) {
					String queryString = body.split("&")[i];
					int signIndex = queryString.indexOf("=");
					String key = "";
					String value = "";
					if(signIndex == -1) { //if param is not valid
						key = queryString;
						statusCode = 400;
						ChatAndSearchApplicationLogger.write(Level.INFO, "Contain invalid param: " + key + " Status Code: " + statusCode, 0);
					} else { //if param is valid
						key = queryString.substring(0, signIndex);
						value = queryString.substring(signIndex + 1);
						ChatAndSearchApplicationLogger.write(Level.INFO, "Param key: " + key, 0);
						ChatAndSearchApplicationLogger.write(Level.INFO, "Param value: " + value, 0);
					}
					if(queryStringMap.containsKey(key)) {
						statusCode = 400;
						ChatAndSearchApplicationLogger.write(Level.INFO, "Query String Duplicate, Status Code: " + statusCode, 0);
					}
					queryStringMap.put(key, value);
				}
			} else {
				statusCode = 400;
				ChatAndSearchApplicationLogger.write(Level.INFO, "POST with empty body: " + statusCode, 0);
			}
		}
		HTTPRequest req = new HTTPRequest();
		req.setMethod(method);
		req.setPath(path);
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(statusCode);
		ChatAndSearchApplicationLogger.write(Level.INFO, "Status Code before call handler : " + statusCode, 0);
		HTTPResponse resp = new HTTPResponse();
		callHandler(req, resp);
		sendResponse(resp, writer);
	}
	
	/**
	 * Calls appropriate handler
	 * 
	 * @param req
	 * @param resp
	 */
	private void callHandler(HTTPRequest req, HTTPResponse resp) {
		ChatAndSearchApplicationLogger.write(Level.INFO, "Handle method: " + req.getMethod(), 0);
		ChatAndSearchApplicationLogger.write(Level.INFO, "Handle path: " + req.getPath(), 0);
		if(req.getStatusCode() != 200) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Status Code not 200 but: " + req.getStatusCode(), 0);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			
		} else {
			if(requestMap.containsKey(req.getPath())) {
				ChatAndSearchApplicationLogger.write(Level.INFO, "Status Code is 200 and contain path: " + req.getPath(), 0);
				Handler handler = requestMap.get(req.getPath());
				handler.handle(req, resp);
			} else {
				ChatAndSearchApplicationLogger.write(Level.INFO, "Change Status Code to 404 because path not found: " + req.getPath(), 0);
				req.setStatusCode(404);
				Handler handler = new ErrorHandler();
				handler.handle(req, resp);
			}
		}
	}
	
	/**
	 * Sends response to client
	 * 
	 * @param resp
	 * @param writer
	 */
	private void sendResponse(HTTPResponse resp, PrintWriter writer) {
		String headers = resp.getHeaders();
		String page = resp.getPage();
		byte[] bytes  = page.getBytes();
		int contentLength = bytes.length;
		String contentLengthLine = "Content-Length: " + contentLength + "\n";
		headers += contentLengthLine + "\r\n";
		writer.write(headers);
		writer.write(page);
	}
}