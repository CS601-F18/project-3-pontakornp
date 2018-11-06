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

public class HTTPServerWorker implements Runnable{
	private Socket sock;
	private HashMap<String, Handler> requestMap;
	
	public HTTPServerWorker(Socket sock, HashMap<String, Handler> requestMap) {
		this.sock = sock;
		this.requestMap = requestMap;
	}
	
	private void callHandler(HTTPRequest req, HTTPResponse resp) {
		System.out.println(req.getPath());
		if(req.getStatusCode() != 200) {
			System.out.println("no map");
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
		} else {
			if(requestMap.containsKey(req.getPath())) {
				System.out.println("yes map");
				Handler handler = requestMap.get(req.getPath());
				handler.handle(req, resp);
			} else {
				req.setStatusCode(404);
				Handler handler = new ErrorHandler();
				handler.handle(req, resp);
			}
		}
	}
	
	private void handleRequest(String requestLine, String body, PrintWriter writer) {
		String[] reqs = requestLine.split(" ");
		String method = "";
		String path = "";
		String protocol = "";
		int statusCode = 200;
		if(reqs.length != 3) { //check if requestLineParts has three components
			statusCode = 400;
		} else {
			method = reqs[0].trim();
			path = reqs[1].trim();
			protocol = reqs[2].trim();
		}
		if(!protocol.equals("HTTP/1.0") && !protocol.equals("HTTP/1.1")) { //check if support the protocol version specified
			statusCode = 400;
			//
		} else if(!method.equals("GET") && !method.equals("POST")) { //check if method not get return 405
			statusCode = 405;
		}
		// check if there is a query string passed by the get method
		if(path.indexOf("?") != -1) {
			path = path.substring(0, path.indexOf("?"));
		}
		try {
			body = URLDecoder.decode(body, "UTF-8");
			System.out.println("Body: " +body);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Decoding error.");
		}
//		HTTPRequest req = new HTTPRequest(method, path, body, statusCode);
		
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
//		if(body.indexOf("&") != -1) {
		if(method.equals("POST")) {
			if(!body.equals("")) {
				int paramNum = body.split("&").length;
				System.out.println("paramNum" + paramNum);
				for(int i = 0; i < paramNum; i++) {
					String queryString = body.split("&")[i];
					int firstSignIndex = queryString.indexOf("=");
					String key = queryString.substring(0, firstSignIndex);
					String value = queryString.substring(firstSignIndex + 1);
					ChatAndSearchApplicationLogger.write(Level.INFO, "Param key: " + key, 0);
					ChatAndSearchApplicationLogger.write(Level.INFO, "Param value: " + value, 0);
					if(queryStringMap.containsKey(key)) {
						statusCode = 400;
					}
					queryStringMap.put(key, value);
				}
			} else {
				statusCode = 400;
			}
		}
		HTTPRequest req = new HTTPRequest();
		req.setMethod(method);
		req.setPath(path);
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(statusCode);
		HTTPResponse resp = new HTTPResponse();
		callHandler(req, resp);
		sendResponse(resp, writer);
	}
	
	private void sendResponse(HTTPResponse resp, PrintWriter writer) {
		String headers = resp.getHeaders();
		String page = resp.getPage();
//		System.out.println(headers);
//		System.out.println(page);
		writer.write(headers);
		writer.write(page);
//		System.out.println("done");
	}
	
	private boolean verifyHeaderStatus(String line) {
		boolean isValid = true;
		System.out.println(line);
		System.out.println(line.split(":")[0].toLowerCase());
		String check = line.split(":")[0].toLowerCase().trim();
		if(line.indexOf(":") == -1) { //check if header format is valid
			System.out.println("Invalid format of headers");
			isValid = false;
		} else if(!HTTPConstants.headers.contains(check)) { //check if key is valid
			System.out.println("Invalid header key");
			isValid = false;
		} else if(line.split(":")[1].equals("")) { //check if value is an empty string
			System.out.println("Invalid header value");
			isValid = false;
		}
		return isValid;
	}
	
	public void run() {
		try (
			InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
		){
			String headers = "";
			String requestLine = oneLine(instream);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Request Line: " + requestLine, 0);
			String line = oneLine(instream);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Other Lines: " + line, 0);
			int length = 0;
			while(line != null && !line.trim().isEmpty()) {
				headers += line + "\n";
				line = oneLine(instream);
				ChatAndSearchApplicationLogger.write(Level.INFO, line, 0);
				if(line == null || line.trim().isEmpty() || line.equals("")) {
					break;
				}
				if(line.toLowerCase().startsWith("content-length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
			}
			System.out.println("Request: \n" + requestLine);
			
			byte[] bytes = new byte[length];
			int read = sock.getInputStream().read(bytes);
			while(read < length) {
				read += sock.getInputStream().read(bytes, read, (bytes.length - read));
			}
			System.out.println("Bytes expected: " + length + " Bytes read: " + read);	
			String body = new String(bytes);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Body: " + body, 0);
			handleRequest(requestLine, body, writer);
		} catch (IOException e) {
			System.out.println("error");
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
	
	private void shutdown() {
		try {
//			writer.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}