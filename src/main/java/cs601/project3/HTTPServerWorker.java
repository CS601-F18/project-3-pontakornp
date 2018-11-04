package cs601.project3;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
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
		if(requestMap.containsKey(req.getPath())) {
			System.out.println("yes map");
			Handler handler = requestMap.get(req.getPath());
			handler.handle(req, resp);
		} else {
			System.out.println("no map");
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
		}
	}
	
	private void handleRequest(String header, String body, PrintWriter writer) {
		String[] reqs = header.split(" ");
		if(reqs.length >= 4) {
			String method = reqs[0];
			String path = reqs[1];
			String HTTPversion = reqs[2];
			String serverName = reqs[3]; 
			int queryStringIndex = path.indexOf("?");
			// check if there is a query string passed by the get method
			if(queryStringIndex != -1) {
				path = path.substring(0, queryStringIndex);
			}
			HTTPRequest req = new HTTPRequest(method, serverName, path, body);
			HTTPResponse resp = new HTTPResponse();
			callHandler(req, resp);
			String headers = resp.getHeaders();
			String page = resp.getPage();
			System.out.println(headers);
			System.out.println(page);
			writer.write(headers);
			writer.write(page);
			System.out.println("done");
		}
	}
	
	public void run() {
		try (
			InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
		){
			String message = "";
			String line = oneLine(instream);
			String header = line;
			int length = 0;
			String serverName = "";
			int port = 0;
			while(line != null && !line.trim().isEmpty()) {
				message += line + "\n";
				line = oneLine(instream);
				if(line.startsWith("Host:")) {
					serverName = line.split(":")[1].trim();
					port = Integer.parseInt(line.split(":")[2].trim());
				}
				//TODO: fix this messy hack
				if(line.startsWith("Content-Length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
				//1. is this a valid format (key : value)?
				//2. is the key valid? (constants defined somewhere)
				//3. is the value valid for the key?							
			}
			System.out.println("Request: " + message);
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
			header += " " + serverName;
			handleRequest(header, body, writer);
//			//save uploaded image to out.jpg
//			FileOutputStream fout = new FileOutputStream("out.jpeg");
//			fout.write(bytes);
//			fout.close(); 
						
			//send response to client
//			String headers = "HTTP/1.0 200 OK\n" +
//						"\r\n";
//			
//			String page = "<html> " + 
//						"<head><title>TEST</title></head>" + 
//					    "<body>This is a short test page.</body>" + 
//						"</html>";
			
//			writer.write(headers);
//			writer.write(page);

//			String responseHeader = "HTTP/1.0 200 OK\n" +
//					"\r\n";
//			String page = "<html> " + 
//					"<head><title>TEST</title></head>" + 
//					"<body>" + 
//						"This is a short test page.<br>" + 
//						"<input type='text' name='test'><br>" +
//						"<input type='submit' value='Submit'>" +
//					"</body>" + 
//					"</html>";
//			writer.write(responseHeader);
//			writer.write(page);
//			writer.flush();
////			socket.close();
//			System.out.println("Done");
		} catch (IOException e) {
			System.out.println("error");
		}
//		shutdown();
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