package cs601.project3.handler;

import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;
import cs601.project3.invertedindex.CustomerEngagementFrequency;
import cs601.project3.invertedindex.InvertedIndex;
import cs601.project3.invertedindex.InvertedIndexSingleton;
import cs601.project3.invertedindex.Review;

/**
 * 
 * @author pontakornp
 *
 * Handles requests that will search for review data by search keyword
 * 
 */
public class ReviewSearchHandler implements Handler{
	/**
	 * Sends request/response to appropriate method
	 */
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		if(req.getMethod().equals("GET")) {
			doGet(resp);
		} else { // method == "POST"
			doPost(req, resp);
		}
	}
	
	/**
	 * Update the response to have a web page for user to input search keyword to return review data
	 * 
	 * @param resp - Http response
	 */
	private void doGet(HTTPResponse resp) {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Review Search</title></head>");
		html.append("<body>");
		html.append("<form action=\"/reviewsearch\" method=\"post\">");
		html.append("Review search:<br/>");
		html.append("<input type=\"text\" name=\"query\"/><br/>");
		html.append("<input type=\"submit\" value=\"Search\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html.toString());
	}
	
	/**
	 * Update the response to have a web page that contains the result of the term search
	 * 
	 * @param req - Http request
	 * @param resp - Http response
	 */
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		InvertedIndex reviewIndex = InvertedIndexSingleton.getInstance().getReviewInvertedIndex();
		String value = req.getQueryStringMap().get("query");
		value = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase(); // clean the value from the query
		StringBuilder html = new StringBuilder();
		if(reviewIndex.getTermMap().containsKey(value)) {
			html.append("<html>"); 
			html.append("<head><title>Review Search Results</title></head>"); 
			html.append("<body>"); 
			html.append("<p>Search term: " + value + "</p>");
			html.append("<table style=\"width:100%\">");
			html.append("<tr>");
			html.append("<th style=\"border: 1px solid #dddddd;\">ASIN</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Reviewer ID</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Review Text</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Overall Score</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Number of Occurences</th>");
		    html.append("</tr>");
		    ArrayList<CustomerEngagementFrequency> list = reviewIndex.getTermMap().get(value);
			for(CustomerEngagementFrequency cef: list) {
				Review review = (Review)cef.getCE();
				html.append("<tr>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getASIN() + "</td>");
			    html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getReviewerID() + "</td>");
			    html.append("<td style=\"border: 1px solid #dddddd;\">" + StringEscapeUtils.escapeHtml4(review.getReviewText()) + "</td>");
			    html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getOverall() + "</td>");
			    html.append("<td style=\"border: 1px solid #dddddd;\">" + cef.getFreq() + "</td>");
		    	html.append("</tr>");
			}
			html.append("</table>");
			html.append("</body>");
			html.append("</html>");
		} else {
			html.append("<html>");
			html.append("<head><title>Review Search Results</title></head>");
			html.append("<body>");
			html.append("<p>Search not found.</p>");
			html.append("</body>");
			html.append("</html>");
		}
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html.toString());
	}
	
	/**
	 * Checks if there's a duplicated valid parameter
	 * 
	 * @param req - Http Request
	 * @param resp - Http Response
	 * @return true or false
	 */
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("query") || req.getQueryStringMap().get("query").equals("")) {
			req.setStatusCode(400);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map does not contain main param or value is null", 0);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
}
