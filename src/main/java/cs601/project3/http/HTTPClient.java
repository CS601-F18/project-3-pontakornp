package cs601.project3.http;

import java.util.*;
import java.util.logging.Level;

import cs601.project3.ChatAndSearchApplicationLogger;

import java.io.*;
import java.net.*;

public class HTTPClient {
	
	public static int PORT = 9090;
	
	public static String test(String host, String method, String path) {
		
		StringBuffer buf = new StringBuffer();
		
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
			) { 

			//send request
			String request = getRequest(host, method, path);
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
//			String line = reader.readLine();
//			while(line != null) {				
//				buf.append(line + "\n"); //append the newline stripped by readline
//				line = reader.readLine();
//			}
//			
//			
			
			String headers = "";
			String requestLine = oneLine(instream);
			headers += requestLine + "\n";
			String line = oneLine(instream);
			headers += line + "\n";
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
			System.out.println(headers);
			byte[] bytes = new byte[length];
			int read = sock.getInputStream().read(bytes);
			while(read < length) {
				read += sock.getInputStream().read(bytes, read, (bytes.length - read));
			}
			System.out.println("Bytes expected: " + length + " Bytes read: " + read);
			String body = new String(bytes);
			return body;

		} catch (IOException e) {
			System.out.println("Error on socket or when reading input");
		}
//		return buf.toString();
		return "Nothing";
	}

	private static String getRequest(String host, String method, String path) {
		String request = method + " " + path + " HTTP/1.0" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n";								
		return request;
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
	
//	public static void main(String[] args) {
//		PORT = 8080;
//		System.out.println(test("localhost", "GET", "/reviewsearch"));
//	}
}
