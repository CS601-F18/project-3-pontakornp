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
	
	public void handleRequest(String method, String path, String query) {
		if(requestMap.containsKey(path)) {
			Handler handler = requestMap.get(path);
			handler.handle(method, path, query);
		}
	}
	
	public void run() {
		try (
			InputStream instream = sock.getInputStream();
			PrintWriter writer = new PrintWriter(sock.getOutputStream(), true);
		){
//			String responseHeader = "HTTP/1.0 200 OK\n" +
//					"\r\n";
//			String page = "<html> " + 
//					"<head><title>TEST</title></head>" + 
//					"<body>This is a short test page.</body>" + 
//					"</html>";
			
			String message = "";
			String line = oneLine(instream);
			String[] reqs = line.split(" ");
			if(reqs.length >= 2) {
				String method = reqs[0];
				String param = reqs[1];
				String path = "";
				String query = "";
				int queryIndex = param.indexOf("?");
				if(queryIndex != -1) {
					path = param.substring(0, queryIndex);
					query = param.substring(queryIndex + 1);
				} else {
					path = param;
				}
				System.out.println(method);
				System.out.println(path);
				System.out.println(query);
				handleRequest(method, path, query);
			}
			
			int length = 0;
			while(line != null && !line.trim().isEmpty()) {
				
				message += line + "\n";
				line = oneLine(instream);
				
				
				
				
				//TODO: fix this messy hack
				if(line.startsWith("Content-Length:")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
				
				//1. is this a valid format (key : value)?
				//2. is the key valid? (constants defined somewhere)
				//3. is the value valid for the key?							
			}
			System.out.println("Request: \n" + message);
					
			
			byte[] bytes = new byte[length];
			int read = sock.getInputStream().read(bytes);
			
			while(read < length) {
				read += sock.getInputStream().read(bytes, read, (bytes.length-read));
			}
			
			System.out.println("Bytes expected: " + length + " Bytes read: " + read);			
			
			//save uploaded image to out.jpg
			FileOutputStream fout = new FileOutputStream("out.jpeg");
			fout.write(bytes);
			fout.close(); 
						
			//send response to client
			String headers = "HTTP/1.0 200 OK\n" +
						"\r\n";
			
			String page = "<html> " + 
						"<head><title>TEST</title></head>" + 
					    "<body>This is a short test page.</body>" + 
						"</html>";
			
			writer.write(headers);
			writer.write(page);

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
	
	
	public void shutdown() {
		try {
//			writer.close();
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}