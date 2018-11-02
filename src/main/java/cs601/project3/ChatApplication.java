package cs601.project3;

public class ChatApplication {
	public static void main(String[] args) {
		int port = 9090;
		HTTPServer server = new HTTPServer(port);
		server.addMapping("/slackbot", new ChatHandler());
		server.startup();
	}
}
