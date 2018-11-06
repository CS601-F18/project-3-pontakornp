package cs601.project3;

import cs601.project3.handler.FindHandler;
import cs601.project3.handler.ReviewSearchHandler;
import cs601.project3.http.HTTPServer;
import cs601.project3.invertedindex.InvertedIndexSingleton;

public class SearchApplication {
	public static void main(String[] args) {
		ChatAndSearchApplicationLogger.initialize(ChatApplication.class.getName(), "searchApplicationLog.txt");
		int port = 8080;
		HTTPServer server = new HTTPServer(port);
		
		// build invertedindex
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();

		//The request GET /reviewsearch will be dispatched to the 
		//handle method of the ReviewSearchHandler.
		server.addMapping("/reviewsearch", new ReviewSearchHandler());
		//The request GET /find will be dispatched to the 
		//handle method of the FindHandler.
		server.addMapping("/find", new FindHandler());
		server.startup();
	}
}
