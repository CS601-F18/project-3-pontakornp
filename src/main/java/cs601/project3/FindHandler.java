package cs601.project3;

import java.util.ArrayList;

public class FindHandler implements Handler {
	
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		resp.setHeader(HTTPConstants.OK_HEADER);
		if(req.getMethod().equals("GET")) {
			System.out.println("GET");
			resp.setPage(getForm());
			
		} else { // method == "POST"
			System.out.println("POST");
			resp.setPage(getSearchResult(req, resp));
		}
	}
	
	private String getSearchResult(HTTPRequest req, HTTPResponse resp) {
		InvertedIndexSingleton indexSingleton = InvertedIndexSingleton.getInstance();
		InvertedIndex reviewIndex = indexSingleton.getReviewInvertedIndex();
		InvertedIndex qaIndex = indexSingleton.getQAInvertedIndex();
		String key = req.getQueryString().split("=")[0];
		String value = req.getQueryString().split("=")[1];
		// check if query string valid
		if(!key.equals("asin") || value.equals("") || (!reviewIndex.getASINMap().containsKey(value) && !qaIndex.getASINMap().containsKey(value))) {
			System.out.println("ASIN is not found. Please try to find other ASIN.\n");
			return getForm();
		}
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
						    "<th style=\"border: 1px solid #dddddd;\">Number of Occurences</th>" +
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
		return html;
	}
	
	private String getForm() {
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
		return html;
	}
}
