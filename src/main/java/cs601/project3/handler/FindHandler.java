package cs601.project3.handler;

import java.util.ArrayList;
import java.util.logging.Level;

import org.apache.commons.lang3.StringEscapeUtils;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;
import cs601.project3.invertedindex.CustomerEngagement;
import cs601.project3.invertedindex.InvertedIndex;
import cs601.project3.invertedindex.InvertedIndexSingleton;
import cs601.project3.invertedindex.QA;
import cs601.project3.invertedindex.Review;

/**
 * 
 * @author pontakornp
 *
 * Handles requests that will find review/qa data by asin search
 * 
 */
public class FindHandler implements Handler {
	/**
	 * Sends request/response to appropriate method
	 */
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		resp.setHeader(HTTPConstants.OK_HEADER);
		if(req.getMethod().equals("GET")) {
			doGet(resp);
		} else { // method == "POST"
			doPost(req, resp);
		}
	}
	
	/**
	 * Update the response to have a web page for user to input search to find review/qa data by asin
	 * 
	 * @param resp - Http response
	 */
	private void doGet(HTTPResponse resp) {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>ASIN Search</title></head>");
		html.append("<body>");
		html.append("<form action=\"/find\" method=\"post\">");
		html.append("ASIN search:<br/>");
		html.append("<input type=\"text\" name=\"asin\"/><br/>");
		html.append("<input type=\"submit\" value=\"Search\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html.toString());
	}
	
	/**
	 * Update the respone to have a web page that contains the result of the asin search
	 * 
	 * @param req - Http request
	 * @param resp - Http response
	 */
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();
		InvertedIndex reviewIndex = indexSingleton.getReviewInvertedIndex();
		InvertedIndex qaIndex = indexSingleton.getQAInvertedIndex();
		String value = req.getQueryStringMap().get("asin");
		String filteredValue = value.toLowerCase(); // clean the value from the query
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Review and QA documents</title></head>");
		html.append("<body>");
		html.append("<p>Search ASIN: " + value + "</p>");
		int count = 0;
		if(reviewIndex.getASINMap().containsKey(filteredValue)) {
			html.append("<table style=\"width:100%\">");
			html.append("<tr>");
			html.append("<th style=\"border: 1px solid #dddddd;\">ASIN</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Reviewer ID</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Review Text</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Overall Score</th>");			    
			html.append("</tr>");
			ArrayList<CustomerEngagement> asinReviewList = reviewIndex.getASINMap().get(filteredValue);
			for(CustomerEngagement ce: asinReviewList) {
				Review review = (Review)ce;
				html.append("<tr>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getASIN() + "</td>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getReviewerID() + "</td>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + StringEscapeUtils.escapeHtml4(review.getReviewText()) + "</td>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getOverall() + "</td>");
				html.append("</tr>");
			}
			html.append("</table>");
		} else {
			count += 1;
		}
		if(qaIndex.getASINMap().containsKey(filteredValue)) {
			html.append("<table style=\"width:100%\">");
			html.append("<tr>");
			html.append("<th style=\"border: 1px solid #dddddd;\">ASIN</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Question</th>");
			html.append("<th style=\"border: 1px solid #dddddd;\">Answer</th>");
			html.append("</tr>");
			ArrayList<CustomerEngagement> asinQAList = qaIndex.getASINMap().get(filteredValue);
			for(CustomerEngagement ce: asinQAList) {
				QA qa = (QA)ce;
				html.append("<tr>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + qa.getASIN() + "</td>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + StringEscapeUtils.escapeHtml4(qa.getQueston()) + "</td>");
				html.append("<td style=\"border: 1px solid #dddddd;\">" + StringEscapeUtils.escapeHtml4(qa.getAnswer())+ "</td>");
				html.append("</tr>");
			}
			html.append("</table>");
		} else {
			count += 1;
		}
		if(count == 2) {
			html.append("<p>Search not found.</p>");
		}
		html.append("</body>");
		html.append("</html>");
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
		if (!req.getQueryStringMap().containsKey("asin") || req.getQueryStringMap().get("asin").equals("")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map does not contain main param or value is null", 0);
			req.setStatusCode(400);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
}