package cs601.project3.handler;

import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

public class ErrorHandler implements Handler {
	public void handle(HTTPRequest req, HTTPResponse resp) {
//		if(!req.getMethod().equals("GET") && !req.getMethod().equals("POST")) {
//			String page = "<html>" + 
//							"<head><title>405 Method Not Allowed</title></head>" +
//							"<body>" +
//								"Page not supported." +
//							"</body>" +
//						"</html>";
//			resp.setHeader(HTTPConstants.METHOD_NOT_ALLOWED_HEADER);
//			resp.setPage(page);
//		} else {
//			String page = "<html>" + 
//							"<head><title>404 Not Found</title></head>" +
//							"<body>" +
//								"Page not found." +
//							"</body>" +
//						"</html>";
//			resp.setHeader(HTTPConstants.NOT_FOUND_HEADER);
//			resp.setPage(page);
//		}
		int statusCode = req.getStatusCode();
		if(statusCode == 400) {
			String page = "<html>" + 
					"<head><title>400 Bad Request</title></head>" +
					"<body>" +
					"Invalid request." +
					"</body>" +
					"</html>";
			resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
			resp.setPage(page);
		} else if(statusCode == 405) {
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