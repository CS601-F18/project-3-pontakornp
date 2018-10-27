package cs601.project3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class HTTPServerWorker implements Runnable{
	private Socket socket;
	
	public HTTPServerWorker(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			String responseHeader = "HTTP/1.0 200 OK\n" +
					"\r\n";
			String page = "<html> " + 
					"<head><title>TEST</title></head>" + 
					"<body>This is a short test page.</body>" + 
					"</html>";
			writer.write(responseHeader);
			writer.write(page);
			writer.close();
			socket.close();
			System.out.println("Done");
		} catch (IOException e) {
			System.out.println("error");
		}
	}
}