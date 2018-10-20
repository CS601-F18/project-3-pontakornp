package cs601.project3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * 
 * Server handles the request of the client by waiting for the request and response back.
 * 
 */
public class HTTPServer {
	private ServerSocket server;
	private Socket sock;
	private BufferedReader instream;
	
	public HTTPServer(int port) {	
		try {
			server = new ServerSocket(port);
			sock = server.accept();
			instream = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		} catch(IOException ioe) {
			System.out.println("Server cannot be connected. Please try again.");
		}
	}
	
	public void addMapping(String command, Handler handler) {
		
	}
	
	public void closeServer() {
		try {
			sock.close();
		} catch (IOException e) {
			System.out.println("Server cannot be closed. Please try again.");
		}
	}
}
