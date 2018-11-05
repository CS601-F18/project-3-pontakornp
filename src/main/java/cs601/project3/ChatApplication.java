package cs601.project3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatApplication {
	public static void main(String[] args) {
//		if(!HTTPConstants.headers.contains("accept")) {
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
