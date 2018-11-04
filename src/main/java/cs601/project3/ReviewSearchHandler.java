package cs601.project3;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ReviewSearchHandler implements Handler{

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
		
		int firstSignIndex = req.getQueryString().indexOf("=");
		String key = req.getQueryString().substring(0, firstSignIndex);

//		String key = req.getQueryString().split("=")[0];
		String value = req.getQueryString().substring(firstSignIndex + 1);
		System.out.println(key + ":" + value);
		try {
			value = URLDecoder.decode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("An issue occurs when decoding parameters. Please try again.");
		}
		value = value.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		// check if query string valid
		if(!key.equals("query") || value.equals("") || !reviewIndex.getTermMap().containsKey(value)) {
			System.out.println("Search term is not found. Please try other search term.\n");
			return getForm();
		}
		int resultSize = reviewIndex.getTermMap().get(value).size();
		System.out.println("Result Size:" + resultSize);
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
		return html;
	}
	
	private String getForm() {
		String html = "<html> " + 
				"<head><title>Review Search</title></head>" + 
				"<body>" + 
					"<form action\"/reviewsearch\" method=\"post\">" +
						"Review search:<br>" + 
						"<input type=\"text\" name=\"query\"><br>" +
						"<input type=\"submit\" value=\"Search\">" +
					"</form>" +
				"</body>" +
				"</html>";
		return html;
	}
	
}
