package cs601.project3.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cs601.project3.handler.Handler;

/**
 * 
 * Server handles the request of the client by waiting for the request and response back.
 * 
 */
public class HTTPServer{
	private ServerSocket server;
	private ExecutorService pool;
	private volatile boolean running;
	private HashMap<String, Handler> requestMap = new HashMap<String, Handler>();
	
	public HTTPServer(int port) {	
		try {
			this.server = new ServerSocket(port);
			this.pool = Executors.newFixedThreadPool(5);
			this.running = true;
		} catch(IOException ioe) {
			System.out.println("Cannot start the server. Please try again.");
		}
	}
	
	public void startup() {
		while(running) {
			System.out.println("listen");
			try {
				Socket sock = server.accept();
				pool.execute(new HTTPServerWorker(sock, requestMap));
			} catch (IOException e) {
				System.out.println("Socket error");
			}
		}
	}
	
	public void addMapping(String command, Handler handler) {
		requestMap.put(command, handler);
	}
	
	public void shutdown() {
		running = false;
		pool.shutdown();
		try {
			pool.awaitTermination(60, TimeUnit.MILLISECONDS);
		} catch(InterruptedException ioe) {
			System.out.println("Thread got interrupted. Please try again.");
		}
	}
}
