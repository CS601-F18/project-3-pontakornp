package cs601.project3.http;

import java.util.*;
import java.io.*;
import java.net.*;

public class HTTPClient {
	
	public static int PORT = 9090;
	
	public static String test(String host, String path) {
		
		StringBuffer buf = new StringBuffer();
		
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
			) { 

			//send request
			String request = getRequest(host, path);
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			String line = reader.readLine();
			while(line != null) {				
				buf.append(line + "\n"); //append the newline stripped by readline
				line = reader.readLine();
			}

		} catch (IOException e) {
			System.out.println("HTTPFetcher::download " + e.getMessage());
		}
		return buf.toString();
	}

	private static String getRequest(String host, String path) {
		String request = "GET " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n";								
		return request;
	}
	
	public static void main(String[] args) {
		System.out.println(test("localhost", "/slackbot"));
	}
}
