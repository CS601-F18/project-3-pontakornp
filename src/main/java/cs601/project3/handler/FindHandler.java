package cs601.project3.handler;

import java.util.ArrayList;

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
			System.out.println("GET");
			doGet(resp);
			
		} else { // method == "POST"
			System.out.println("POST");
			doPost(req, resp);
		}
	}
	
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("asin") || req.getQueryStringMap().get("asin").equals("")) {
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
		value = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase(); // clean the value from the query
		String html = "<html> " + 
					"<head><title>Review and QA documents</title></head>" + 
					"<body>" + 
						"<p>Search ASIN: " + value+ "</p>";
		
		if(reviewIndex.getASINMap().containsKey(value)) {
			html += "<table style=\"width:100%\">" +
						"<tr>" +
						    "<th style=\"border: 1px solid #dddddd;\">ASIN</th>" +
						    "<th style=\"border: 1px solid #dddddd;\">Reviewer ID</th>" +
						    "<th style=\"border: 1px solid #dddddd;\">Review Text</th>" +
						    "<th style=\"border: 1px solid #dddddd;\">Overall Score</th>" +
					    "</tr>";
			ArrayList<CustomerEngagement> asinReviewList = reviewIndex.getASINMap().get(value);
			for(CustomerEngagement ce: asinReviewList) {
				Review review = (Review)ce;
				html +=	"<tr>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + review.getASIN() + "</th>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + review.getReviewerID() + "</td>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + review.getReviewText() + "</td>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + review.getOverall() + "</td>" +
				    	"</tr>";
			}
			html += "</table>";
		}
		
		if(qaIndex.getASINMap().containsKey(value)) {
			html += "<table style=\"width:100%\">" +
						"<tr>" +
						    "<th style=\"border: 1px solid #dddddd;\">ASIN</th>" +
						    "<th style=\"border: 1px solid #dddddd;\">Question</th>" +
						    "<th style=\"border: 1px solid #dddddd;\">Answer</th>" +
					    "</tr>";
			ArrayList<CustomerEngagement> asinQAList = qaIndex.getASINMap().get(value);
			for(CustomerEngagement ce: asinQAList) {
				QA qa = (QA)ce;
				html +=	"<tr>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + qa.getASIN() + "</th>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + qa.getQueston() + "</td>" +
					    	"<td style=\"border: 1px solid #dddddd;\">" + qa.getAnswer() + "</td>" +
				    	"</tr>";
			}
			html += "</table>";
		}
		html += "</body>" +
				"</html>";
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html);
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
