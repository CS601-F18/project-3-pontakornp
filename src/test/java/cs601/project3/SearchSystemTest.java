package cs601.project3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs601.project3.http.HTTPClient;

public class SearchSystemTest {
	
	@Test
	public void testCorrectGetReviewSearch() {
		HTTPClient.PORT = 8080;
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
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/reviewsearch", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/reviewsearch?unused=params", ""));
	}
	
	@Test
	public void testCorrectGetFind() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>ASIN Search</title></head>");
		html.append("<body>");
		html.append("<form action=\"/find\" method=\"post\">");
		html.append("Review search:<br/>");
		html.append("<input type=\"text\" name=\"asin\"/><br/>");
		html.append("<input type=\"submit\" value=\"Search\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/find", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/find?unused=params", ""));
	}
	
	@Test
	public void testCorrectPostReviewSearch() {
		HTTPClient.PORT = 8080;
	}

	@Test
	public void testCorrectPostFind() {
		HTTPClient.PORT = 8080;
	}
	
	@Test
	public void testBadRequest() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>"); 
		html.append("<head><title>400 Bad Request</title></head>");
		html.append("<body>");
		html.append("<p>Invalid request.</p>");
		html.append("</body>");
		html.append("</html>");
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/badrequest", ""));
	}
	
	@Test
	public void testMethodNotFound() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>405 Method Not Allowed</title></head>");
		html.append("<body>");
		html.append("<p>Page not supported.</p>");
		html.append("</body>");
		html.append("</html>");
		assertEquals(html.toString(), HTTPClient.connect("localhost", "PUT", "/", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "DELETE", "/", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "PUT", "/reviewsearch", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "DELETE", "/reviewsearch", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "PUT", "/find", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "DELETE", "/find", ""));
	}
	
	@Test
	public void testPageNotFound() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>404 Not Found</title></head>");
		html.append("<body>");
		html.append("<p>Page not found.</p>");
		html.append("</body>");
		html.append("</html>");
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/", ""));
		assertEquals(html.toString(), HTTPClient.connect("localhost", "GET", "/unknownpath", ""));
	}
}