package cs601.project3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs601.project3.http.HTTPClient;

public class ChatSystemTest {
	
	@Test
	public void testCorrectGetSlackBot() {
		HTTPClient.PORT = 9090;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Send Slack Message</title></head>");
		html.append("<body>");
		html.append("<form action=\"/slackbot\" method=\"post\">");
		html.append("Message:<br/>");
		html.append("<input type=\"text\" name=\"message\"/><br/>");
		html.append("<input type=\"submit\" value=\"Send\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/slackbot", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/slackbot?unused=params", ""));
	}
	
	@Test
	public void testCorrectPostSlackBot() {
		HTTPClient.PORT = 9090;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Send Slack Message</title></head>");
		html.append("<body>");
		html.append("<p>Message sent!</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/slackbot", "message=test1"));
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/slackbot", "message=test2&unused=params"));
	}
	
	@Test
	public void testBadRequest() {
		HTTPClient.PORT = 9090;
		StringBuilder html = new StringBuilder();
		html.append("<html>"); 
		html.append("<head><title>400 Bad Request</title></head>");
		html.append("<body>");
		html.append("<p>Invalid request.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/slackbot", "messag=test"));
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/slackbot", "message=same&message=test"));
	}
	
	@Test
	public void testMethodNotFound() {
		HTTPClient.PORT = 9090;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>405 Method Not Allowed</title></head>");
		html.append("<body>");
		html.append("<p>Page not supported.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "PUT", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "DELETE", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "PUT", "/slackbot", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "DELETE", "/slackbot", ""));
	}
	
	@Test
	public void testPageNotFound() {
		HTTPClient.PORT = 9090;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>404 Not Found</title></head>");
		html.append("<body>");
		html.append("<p>Page not found.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/unknownpath", ""));
	}
}
