package cs601.project3.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.handler.Handler;

/**
 * 
 * Server handles the request of the client by waiting for the request and response back.
 * Handles multiple clients by using thread pools
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
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Cannot start the server", 1);
		}
	}
	
	/**
	 * Starts the server
	 */
	public void startup() {
		while(running) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Server listens", 0);
			try {
				Socket sock = server.accept();
				pool.execute(new HTTPServerWorker(sock, requestMap));
			} catch (IOException e) {
				ChatAndSearchApplicationLogger.write(Level.WARNING, "Error when server trying to accept the client to connect through socket", 1);
			}
		}
	}
	
	/**
	 * Adds mapping that has command/path as key and handler as a value
	 * 
	 * @param command
	 * @param handler
	 */
	public void addMapping(String command, Handler handler) {
		requestMap.put(command, handler);
	}
	
	/**
	 * shut down thread pool
	 */
	public void shutdown() {
		running = false;
		pool.shutdown();
		try {
			pool.awaitTermination(60, TimeUnit.MILLISECONDS);
		} catch(InterruptedException ioe) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Thread in thread pool got interrupted", 1);
		}
	}
}
