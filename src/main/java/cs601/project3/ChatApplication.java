package cs601.project3;

import cs601.project3.handler.ChatHandler;
import cs601.project3.http.HTTPServer;

/**
 * 
 * @author pontakornp
 * 
 * This application will let user chat to a channel in Slack Application
 *
 */
public class ChatApplication {
	/**
	 * 
	 * Starts the server for client to connect and chat in Slack
	 */
	public static void main(String[] args) {
		ChatAndSearchApplicationLogger.initialize(ChatApplication.class.getName(), "chatApplicationLog.txt");
		int port = 9090;
		HTTPServer server = new HTTPServer(port);
		//The request GET /reviewsearch will be dispatched to the 
		//handle method of the ChatHandler.
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}