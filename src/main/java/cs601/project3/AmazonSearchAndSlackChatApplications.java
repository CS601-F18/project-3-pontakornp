package cs601.project3;

import java.util.ArrayList;
import java.util.Collections;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;

public class AmazonSearchAndSlackChatApplications {
	public static void main(String args[]) {
		int port = 1024;
		HTTPServer server = new HTTPServer(port);
		
		// build invertedindex
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();
		InvertedIndex reviewIndex = indexSingleton.getReviewInvertedIndex();
		InvertedIndex qaIndex = indexSingleton.getQAInvertedIndex();
		
		// map path
		server.addMapping("/reviewsearch", new ReviewSearchHandler());
		server.addMapping("/find", new FindHandler());
		// start server
		server.startup();
	}
	// separate search and chat main?
	// what singleton return
	// what start up does
	// how to connect to microcloud server
}