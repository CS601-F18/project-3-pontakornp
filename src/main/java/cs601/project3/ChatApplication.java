package cs601.project3;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import cs601.project3.handler.ChatHandler;
import cs601.project3.http.HTTPServer;

public class ChatApplication {
	public static void main(String[] args) {
		ChatAndSearchApplicationLogger.initialize(ChatApplication.class.getName(), "chatApplicationLog.txt");
		int port = 9090;
		HTTPServer server = new HTTPServer(port);
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}
