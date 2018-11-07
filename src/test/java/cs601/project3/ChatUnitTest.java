package cs601.project3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cs601.project3.http.HTTPClient;

public class ChatUnitTest {
	
	@Test
	public void testCorrectSlackBotStatusCode() {
		HTTPClient.PORT = 9090;
		String htmlString = "HTTP/1.0 200 OK";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/slackbot", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/slackbot?unused=params", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/slackbot", "message=test1"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/slackbot", "message=test2&unused=params"));
	}

	@Test
	public void testBadRequestStatusCode() {
		HTTPClient.PORT = 9090;
		String htmlString = "HTTP/1.0 400 Bad Request";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/slackbot", "messag=test"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/slackbot", "message=same&message=test"));
	}
	
	@Test
	public void testMethodNotAllowedStatusCode() {
		HTTPClient.PORT = 9090;
		String htmlString = "HTTP/1.0 405 Method Not Allowed";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "PUT", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "DELETE", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "PUT", "/slackbot", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "DELETE", "/slackbot", ""));
	}
	
	@Test
	public void testNotFoundStatusCode() {
		HTTPClient.PORT = 9090;
		String htmlString = "HTTP/1.0 404 Not Found";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/unknownpath", ""));
	}
}
