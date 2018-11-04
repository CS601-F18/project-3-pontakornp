package cs601.project3;

public class ErrorHandler implements Handler {
	public void handle(HTTPRequest req, HTTPResponse resp) {
		if(!req.getMethod().equals("GET") && !req.getMethod().equals("POST")) {
			String page = "<html>" + 
							"<head><title>405 Method Not Allowed</title></head>" +
							"<body>" +
								"Page not supported." +
							"</body>" +
						"</html>";
			resp.setHeader(HTTPConstants.METHOD_NOT_ALLOWED_HEADER);
			resp.setPage(page);
		} else {
			String page = "<html>" + 
							"<head><title>404 Not Found</title></head>" +
							"<body>" +
								"Page not found." +
							"</body>" +
						"</html>";
			resp.setHeader(HTTPConstants.NOT_FOUND_HEADER);
			resp.setPage(page);
		}
	}
}
