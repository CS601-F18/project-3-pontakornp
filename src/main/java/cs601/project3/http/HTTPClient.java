package cs601.project3.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This HTTP Client is used for JUnit testing
 * @author pontakornp
 *
 */
public class HTTPClient {
	public static int PORT = 8080;
	
	/**
	 * Make a connection to input url to return request line
	 * 
	 * @param host
	 * @param method
	 * @param path
	 * @param query
	 * @return
	 */
	public static String getRequestLine(String host, String method, String path, String query) {
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
			) {
			//send request
			String request = "";
			if(query.equals("")) {
				request = getRequest(host, method, path);
			} else {
				request = getRequest(host, method, path, query);
			}
			out.write(request.getBytes());
			out.flush();
			//receive response
			String line = reader.readLine();
			String requestLine = line;
			return requestLine;
		} catch (IOException e) {
			System.out.println("Error on socket or when reading input");
		}
		return "";
	}
	
	/**
	 * Make a connection to a url to return the body
	 * 
	 * @param host
	 * @param method
	 * @param path
	 * @param query
	 * @return
	 */
	public static String connect(String host, String method, String path, String query) {
		StringBuffer buf = new StringBuffer();
		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
			) {
			//send request
			String request = "";
			if(query.equals("")) {
				request = getRequest(host, method, path);
			} else {
				request = getRequest(host, method, path, query);
			}
			out.write(request.getBytes());
			out.flush();
			//receive response
			String line = reader.readLine();
			while(line != null) {
				buf.append(line + "\n"); //append the newline stripped by readline
				if(line.equals("")) {
					line = reader.readLine();
					break;
				}
				line = reader.readLine();
			}
			return line;
		} catch (IOException e) {
			System.out.println("Error on socket or when reading input");
		}
		return "";
	}
	
	/**
	 * Make a request by using host, method, and path
	 * 
	 * @param host
	 * @param method
	 * @param path
	 * @return
	 */
	private static String getRequest(String host, String method, String path) {
		String request = method + " " + path + " HTTP/1.0" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n";								
		return request;
	}
	
	/**
	 * Make a request by using host, method, path, and query string
	 * 
	 * @param host
	 * @param method
	 * @param path
	 * @param query
	 * @return
	 */
	private static String getRequest(String host, String method, String path, String query) {
		String request = method + " " + path + " HTTP/1.0" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Content-Length: " + query.getBytes().length + "\n"
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ "\r\n"
				+ query;
		System.out.println(request);
		return request;
	}
}