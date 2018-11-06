package cs601.project3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs601.project3.http.HTTPClient;

public class SearchSystemTest {
	@Test
	public void testCorrectGetReviewSearch() {
//		ChatApplication.main();
		String response = "HTTP/1.0 200 OK\n" +
		"\r\n";
		response += "<html> " + 
				"<head><title>Review Search</title></head>" + 
				"<body>" + 
					"<form action=\"/reviewsearch\" method=\"post\">" +
						"Review search:<br/>" + 
						"<input type=\"text\" name=\"query\"/><br/>" +
						"<input type=\"submit\" value=\"Search\"/>" +
					"</form>" +
				"</body>" +
				"</html>";
		HTTPClient.PORT = 8080;
		assertEquals(response, HTTPClient.test("localhost", "GET", "/reviewsearch"));
	}
}
