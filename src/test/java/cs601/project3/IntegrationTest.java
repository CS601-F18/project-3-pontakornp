package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import cs601.project3.handler.ChatHandler;
import cs601.project3.handler.FindHandler;
import cs601.project3.handler.Handler;
import cs601.project3.handler.ReviewSearchHandler;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

/**
 * 
 * @author pontakornp
 *
 * Performs JUnit integration test for chat and search applications
 *
 */
public class IntegrationTest {
	
	@Test
	public void testGetReviewSearchHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("GET");
		req.setPath("/reviewsearch");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new ReviewSearchHandler();
		handler.handle(req, resp);
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
		String htmlString = html.toString();
		String headers = resp.getHeaders();
		String page = resp.getPage();
		assertEquals("HTTP/1.0 200 OK\n", headers);
		assertEquals(htmlString, page);
	}
	
	@Test
	public void testPostReviewSearchHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("POST");
		req.setPath("/reviewsearch");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		queryStringMap.put("query", "they");
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new ReviewSearchHandler();
		handler.handle(req, resp);
		String headers = resp.getHeaders();
		assertEquals("HTTP/1.0 200 OK\n", headers);
	}
	
	@Test
	public void testGetFindHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("GET");
		req.setPath("/find");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new FindHandler();
		handler.handle(req, resp);
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
		String htmlString = html.toString();
		String headers = resp.getHeaders();
		String page = resp.getPage();
		assertEquals("HTTP/1.0 200 OK\n", headers);
		assertEquals(htmlString, page);
	}
	
	@Test
	public void testPostFindHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("POST");
		req.setPath("/find");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		queryStringMap.put("asin", "b0002syc5o");
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new FindHandler();
		handler.handle(req, resp);
		String headers = resp.getHeaders();
		assertEquals("HTTP/1.0 200 OK\n", headers);
	}
	
	@Test
	public void testGetSlackBotHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("GET");
		req.setPath("/slackbot");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new ChatHandler();
		handler.handle(req, resp);
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
		String headers = resp.getHeaders();
		String page = resp.getPage();
		assertEquals("HTTP/1.0 200 OK\n", headers);
		assertEquals(htmlString, page);
	}
	
	@Test
	public void testPostSlackBotHandler() {
		//mock request
		HTTPRequest req = new HTTPRequest();
		req.setMethod("POST");
		req.setPath("/slackbot");
		HashMap<String, String> queryStringMap = new HashMap<String, String>();
		queryStringMap.put("message", "test");
		req.setQueryStringMap(queryStringMap);
		req.setStatusCode(200);
		HTTPResponse resp = new HTTPResponse();
		Handler handler = new ChatHandler();
		handler.handle(req, resp);
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Send Slack Message</title></head>");
		html.append("<body>");
		html.append("<p>Message sent!</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		String headers = resp.getHeaders();
		String page = resp.getPage();
		assertEquals("HTTP/1.0 200 OK\n", headers);
		assertEquals(htmlString, page);
	}
}