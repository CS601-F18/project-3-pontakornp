package cs601.project3.handler;

import java.util.logging.Level;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

/**
 * 
 * @author pontakornp
 *
 * Handles invalid request by returning appropriate html message
 * 
 */
public class ErrorHandler implements Handler {
	
	/**
	 * Handles message to be shown the user for different types of error request
	 */
	public void handle(HTTPRequest req, HTTPResponse resp) {
		int statusCode = req.getStatusCode();
		ChatAndSearchApplicationLogger.write(Level.INFO, "Error Handler status code: " + statusCode, 0);
		if(statusCode == 400) {
			StringBuilder html = new StringBuilder();
			html.append("<html>"); 
			html.append("<head><title>400 Bad Request</title></head>");
			html.append("<body>");
			html.append("<p>Invalid request.</p>");
			html.append("</body>");
			html.append("</html>");
			resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
			resp.setPage(html.toString());
		} else if(statusCode == 405) {
			StringBuilder html = new StringBuilder();
			html.append("<html>");
			html.append("<head><title>405 Method Not Allowed</title></head>");
			html.append("<body>");
			html.append("<p>Page not supported.</p>");
			html.append("</body>");
			html.append("</html>");
			resp.setHeader(HTTPConstants.METHOD_NOT_ALLOWED_HEADER);
			resp.setPage(html.toString());
		} else {
			StringBuilder html = new StringBuilder();
			html.append("<html>");
			html.append("<head><title>404 Not Found</title></head>");
			html.append("<body>");
			html.append("<p>Page not found.</p>");
			html.append("</body>");
			html.append("</html>");
			resp.setHeader(HTTPConstants.NOT_FOUND_HEADER);
			resp.setPage(html.toString());
		}
	}
}