package cs601.project3;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
		} else if(requestMap.containsKey(req.getPath())) {
			System.out.println("yes map");
			Handler handler = requestMap.get(req.getPath());
			handler.handle(req, resp);
		} else {
			req.setStatusCode(404);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
		}
	}
	
	private void handleRequest(String requestLine, String body, int statusCode, PrintWriter writer) {
		String[] reqs = requestLine.split(" ");
		String method = "";
		String path = "";
		String protocol = "";
		if(reqs.length != 3) { //check if requestLineParts has three components
			statusCode = 400;
		} else {
			method = reqs[0];
			path = reqs[1];
			protocol = reqs[2];
		}
		if(!protocol.equals("HTTP/1.1") && !protocol.equals("HTTP/1.0")) { //check if support the protocol version specified
			statusCode = 400;
		} else if(!method.equals("GET") && !method.equals("POST")) { //check if method not get return 405
			statusCode = 405;
		}
		int queryStringIndex = path.indexOf("?");
		// check if there is a query string passed by the get method
		if(queryStringIndex != -1) {
			path = path.substring(0, queryStringIndex);
		}
		HTTPRequest req = new HTTPRequest(method, path, body, statusCode);
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
	
	private int getHeaderStatus(String line) {
		int statusCode = 200;
		System.out.println(line);
		System.out.println(line.split(":")[0].toLowerCase());
		String check = line.split(":")[0].toLowerCase();
		ArrayList<String> headerss = new ArrayList<>(Arrays.asList("a-im"));
		if(line.indexOf(":") == -1) { //check if header format is valid
			System.out.println("Invalid format of headers");
			statusCode = 400;
		} else if(!HTTPConstants.headers.contains(check)) { //check if key is valid
			System.out.println("Invalid header key");
			statusCode = 400;
		} else if(line.split(":")[1].equals("")) { //check if value is an empty string
			System.out.println("Invalid header value");
			statusCode = 400;
		}
		return statusCode;
	}
	
	public void run() {
		try (
			InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
		){
			String headers = "";
			String requestLine = oneLine(instream);
			String line = oneLine(instream);
			int length = 0;
			int statusCode = 200;
			while(line != null && !line.trim().isEmpty()) {
				headers += line + "\n";
				line = oneLine(instream);
				if(line.toLowerCase().startsWith("content-length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
				if(getHeaderStatus(line) != 200) {
					statusCode = getHeaderStatus(line);
				}
			}
			System.out.println("Request: \n" + headers);
			byte[] bytes = new byte[length];
			int read = sock.getInputStream().read(bytes);
			while(read < length) {
				read += sock.getInputStream().read(bytes, read, (bytes.length - read));
			}
			System.out.println("Bytes expected: " + length + " Bytes read: " + read);	
			String body = "";
			if(length == read && length != 0) {
				body = new String(bytes);
			}
			handleRequest(requestLine, body, statusCode, writer);
		} catch (IOException e) {
			System.out.println("error");
		}
	}
	
	/**
	 * Read a line of bytes until \n character.
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	private static String oneLine(InputStream instream) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b = (byte) instream.read();
		while(b != '\n') {
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