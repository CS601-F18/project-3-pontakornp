package cs601.project3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class HTTPServerWorker implements Runnable{
	private Socket socket;
	private HashMap<String, Handler> requestMap;
	
	public HTTPServerWorker(Socket socket, HashMap<String, Handler> requestMap) {
		this.socket = socket;
		this.requestMap = requestMap;
	}
	
	public void run() {
		
		try (
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
		){
			String responseHeader = "HTTP/1.0 200 OK\n" +
					"\r\n";
			String page = "<html> " + 
					"<head><title>TEST</title></head>" + 
					"<body>This is a short test page.</body>" + 
					"</html>";
			writer.write(responseHeader);
			writer.write(page);
			writer.flush();
//			socket.close();
			System.out.println("Done");
		} catch (IOException e) {
			System.out.println("error");
		}
		shutdown();
	}
	
	public void reviewSearch() {
		
	}
	public void find() {
		
	}
	
	public void handleRequest(String command) {
		if(requestMap.containsKey(command)) {
			Handler handler = requestMap.get(command);
			handler.handle();
		}
	}
	public void shutdown() {
		try {
//			writer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}