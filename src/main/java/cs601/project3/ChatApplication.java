package cs601.project3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs601.project3.handler.ChatHandler;
import cs601.project3.http.HTTPServer;

public class ChatApplication {
	public static void main(String[] args) {
//		if(!HTTPConstants.headers.contains("accept-encoding")) {
//			System.out.println("yesss");
//		} else {
//			System.out.println("noooooooo");
//		}
		int port = 9090;
		HTTPServer server = new HTTPServer(port);
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}
