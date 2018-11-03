package cs601.project3;

public class ErrorHandler implements Handler {
	public void handle(HTTPRequest req, HTTPResponse resp) {
		if(!req.getMethod().equals("GET") && !req.getMethod().equals("POST")) {
			String headers = "HTTP/1.0 405 Method Not Allowed\n" +
					"\r\n";
			String page = "<html>" + 
							"<body>" +
								"Page not supported." +
							"</body>" +
						"</html>";
			resp.setHeader(headers);
			resp.setPage(page);
			System.out.println("405");
		} else {
			String headers = "HTTP/1.0 404 Not Found\n" +
					"\r\n";
			String page = "<html>" + 
							"<body>" +
								"Page not found." +
							"</body>" +
						"</html>";
			resp.setHeader(headers);
			resp.setPage(page);
			System.out.println("404");
		}
	}
}
