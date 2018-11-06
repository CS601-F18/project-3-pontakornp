package cs601.project3.handler;

import java.util.logging.Level;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;
import cs601.project3.invertedindex.CustomerEngagementFrequency;
import cs601.project3.invertedindex.InvertedIndex;
import cs601.project3.invertedindex.InvertedIndexSingleton;
import cs601.project3.invertedindex.Review;

public class ReviewSearchHandler implements Handler{

	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		if(req.getMethod().equals("GET")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "GET Handle method: " + req.getMethod(), 0);
			doGet(resp);
		} else { // method == "POST"
			ChatAndSearchApplicationLogger.write(Level.INFO, "POST Handle method: " + req.getMethod(), 0);
			doPost(req, resp);
		}
	}
	
	private void doGet(HTTPResponse resp) {
		String html = "<html> " + 
				"<head><title>Review Search</title></head>" + 
				"<body>" + 
					"<form action=\"/reviewsearch\" method=\"post\">" +
						"Review search:<br/>" + 
						"<input type=\"text\" name=\"query\"/><br/>" +
						"<input type=\"submit\" value=\"Search\"/>" +
					"</form>" +
				"</body>" +
				"</html>";
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html);
	}
	
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();
		InvertedIndex reviewIndex = indexSingleton.getReviewInvertedIndex();
		String value = req.getQueryStringMap().get("query");
		ChatAndSearchApplicationLogger.write(Level.INFO, "Main param's value: " + value, 0);
		value = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase(); // clean the value from the query
		ChatAndSearchApplicationLogger.write(Level.INFO, "Main param's value after cleaning: " + value, 0);
		int resultSize = reviewIndex.getTermMap().get(value).size();
		ChatAndSearchApplicationLogger.write(Level.INFO, "Result Size:" + resultSize, 0);
		String html = "<html> " + 
					"<head><title>Review Search Results</title></head>" + 
					"<body>" + 
						"<p>Search term: " + value + "</p>" +
						"<table style=\"width:100%\">" +
							"<tr>" +
							    "<th style=\"border: 1px solid #dddddd;\">ASIN</th>" +
							    "<th style=\"border: 1px solid #dddddd;\">Reviewer ID</th>" +
							    "<th style=\"border: 1px solid #dddddd;\">Review Text</th>" +
							    "<th style=\"border: 1px solid #dddddd;\">Overall Score</th>" +
							    "<th style=\"border: 1px solid #dddddd;\">Number of Occurences</th>" +
						    "</tr>";
		for(int i = 0; i < resultSize; i++) {
			CustomerEngagementFrequency cef = reviewIndex.getTermMap().get(value).get(i);
			Review review = (Review)cef.getCE();
			html +=	"<tr>" +
				    	"<td style=\"border: 1px solid #dddddd;\">" + review.getASIN() + "</th>" +
				    	"<td style=\"border: 1px solid #dddddd;\">" + review.getReviewerID() + "</td>" +
				    	"<td style=\"border: 1px solid #dddddd;\">" + review.getReviewText() + "</td>" +
				    	"<td style=\"border: 1px solid #dddddd;\">" + review.getOverall() + "</td>" +
				    	"<td style=\"border: 1px solid #dddddd;\">" + cef.getFreq() + "</td>" +
			    	"</tr>";
		}
		html += "</table>" +
				"</body>" +
				"</html>";
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html);
	}
	
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("query") || req.getQueryStringMap().get("query").equals("")) {
			req.setStatusCode(400);
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map not conain main param or value is null", 0);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
}
