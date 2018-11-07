package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import cs601.project3.handler.Handler;
import cs601.project3.handler.ReviewSearchHandler;
import cs601.project3.http.HTTPClient;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

public class SearchUnitTest {
	
	@Test
	public void testCorrectReviewSearchStatusCode() {
		HTTPClient.PORT = 8080;
		String htmlString = "HTTP/1.0 200 OK";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/reviewsearch?unused=params", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/reviewsearch", "query=acclimates"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/reviewsearch", "query=the"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/reviewsearch", "query=test123"));
	}
	
	@Test
	public void testCorrectFindStatusCode() {
		HTTPClient.PORT = 8080;
		String htmlString = "HTTP/1.0 200 OK";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/find", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/find?unused=params", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/find", "asin=B000OQGG6M"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/find", "asin=b0002syc5o"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/find", "asin=test123"));
	}
	
	@Test
	public void testBadRequestStatusCode() {
		HTTPClient.PORT = 8080;
		String htmlString = "HTTP/1.0 400 Bad Request";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/reviewsearch", "quer=test"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/reviewsearch", "query=tree&query=shop"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/find", "ain=test123"));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "POST", "/find", "asin=b0002syc5o&asin=test123"));
	}
	
	@Test
	public void testMethodNotAllowedStatusCode() {
		HTTPClient.PORT = 8080;
		String htmlString = "HTTP/1.0 405 Method Not Allowed";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "PUT", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "DELETE", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "PUT", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "DELETE", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "PUT", "/find", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "DELETE", "/find", ""));
	}
	
	@Test
	public void testNotFoundStatusCode() {
		HTTPClient.PORT = 8080;
		String htmlString = "HTTP/1.0 404 Not Found";
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/", ""));
		assertEquals(htmlString, HTTPClient.getRequestLine("localhost", "GET", "/unknownpath", ""));
	}
	
//	@Test
//	public void testReviewSearchIsParamKeyValid() {
//		HTTPRequest req = new HTTPRequest();
//		req.setMethod("GET");
//		req.setPath("/reviewsearch");
//		HashMap<String, String> queryStringMap = new HashMap<String, String>();
//		queryStringMap.put("query", "they");
//		req.setQueryStringMap(queryStringMap);
//		req.setStatusCode(200);
//		HTTPResponse resp = new HTTPResponse();
//		Handler handler = new ReviewSearchHandler();
//		handler.
//	}
}
