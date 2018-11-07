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

public class FindHandler implements Handler {
	
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		resp.setHeader(HTTPConstants.OK_HEADER);
		if(req.getMethod().equals("GET")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "GET Handle method: " + req.getMethod(), 0);
			doGet(resp);
			
		} else { // method == "POST"
			ChatAndSearchApplicationLogger.write(Level.INFO, "POST Handle method: " + req.getMethod(), 0);
			doPost(req, resp);
		}
	}
	
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("asin") || req.getQueryStringMap().get("asin").equals("")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map not conain main param or value is null", 0);
			req.setStatusCode(400);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
	
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();
		InvertedIndex reviewIndex = indexSingleton.getReviewInvertedIndex();
		InvertedIndex qaIndex = indexSingleton.getQAInvertedIndex();
		String value = req.getQueryStringMap().get("asin");
		ChatAndSearchApplicationLogger.write(Level.INFO, "Main param's value: " + value, 0);
//		value = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase(); // clean the value from the query
		String filteredValue = value.toLowerCase(); // clean the value from the query
		ChatAndSearchApplicationLogger.write(Level.INFO, "Main param's value after cleaning: " + value, 0);
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Review and QA documents</title></head>");
		html.append("<body>");
		html.append("<p>Search ASIN: " + value+ "</p>");
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
//				html.append("<tr>");
//				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getASIN() + "</td>");
//				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getReviewerID() + "</td>");
//				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getReviewText() + "</td>");
//				html.append("<td style=\"border: 1px solid #dddddd;\">" + review.getOverall() + "</td>");
//				html.append("</tr>");
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
	
	private void doGet(HTTPResponse resp) {
		String html = "<html> " + 
				"<head><title>ASIN Search</title></head>" + 
				"<body>" + 
					"<form action\"/find\" method=\"post\">" +
						"ASIN search:<br>" + 
						"<input type=\"text\" name=\"asin\"><br>" +
						"<input type=\"submit\" value=\"Search\">" +
					"</form>" +
				"</body>" +
				"</html>";
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html);
	}
}
