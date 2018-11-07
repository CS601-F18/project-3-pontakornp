package cs601.project3.handler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;

import cs601.project3.ChatAndSearchApplicationLogger;
import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

/**
 * 
 * @author pontakornp
 *
 * Handles requests that will send chat to Slack application
 * 
 */
public class ChatHandler implements Handler{
	/**
	 * Sends request/response to appropriate method
	 */
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post method
		if(req.getMethod().equals("GET")) {
			doGet(resp);
		} else { // method == "POST"
			doPost(req, resp);
		}
	}
	
	/**
	 * Update the response to have a web page for user to input message to post to Slack application
	 * 
	 * @param resp - Http response
	 */
	private void doGet(HTTPResponse resp) {
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
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html.toString());
	}
	
	/**
	 * 
	 * Post a message to Slakc application and update the response to have a web page show the result of posting
	 * 
	 * @param req - Http request
	 * @param resp - Http resposne
	 */
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		String text = getText(req);
		SlackPostMessageAPI slackAPI = new SlackPostMessageAPI(text);
		String url = slackAPI.getTargetUrl();
		String urlParam = slackAPI.getUrlParameters();
		try {
			URL obj = new URL(url);
			HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
			//add request header
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
		    connection.setRequestProperty("Authorization", "Bearer " + slackAPI.getToken());
		    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // send data in encoded url form
		    //send data
		    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
		    outputStream.writeBytes(urlParam);
		    outputStream.close();
		    //get response
		    
		    //reference: https://stackoverflow.com/questions/613307/read-error-response-body-in-java
		    InputStream inputStream;
		    if (connection.getResponseCode() < HttpsURLConnection.HTTP_BAD_REQUEST) {
		        inputStream = connection.getInputStream();
		        resp.setHeader(HTTPConstants.OK_HEADER);
		        resp.setPage(getSuccessResponse());
		    } else {
		    	inputStream = connection.getErrorStream();
		    	resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
		    	resp.setPage(getErrorResponse());
		    }
		} catch (IOException e) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Connection Error, Status Code: " + 400, 1);
			resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
			resp.setPage(getErrorResponse());
		}
	}
	
	/**
	 * Checks if there's a duplicated valid parameter
	 * 
	 * @param req - Http Request
	 * @param resp - Http Response
	 * @return true or false
	 */
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("message") || req.getQueryStringMap().get("message").equals("")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map does not contain main param or value is null", 0);
			req.setStatusCode(400);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
	
	/**
	 * Helper method for doPost method that return the text to be posted to Slack application
	 * 
	 * @param req - Http request
	 * @return text to be posted to Slack
	 */
	private String getText(HTTPRequest req) {
		String text = "";
		String value = req.getQueryStringMap().get("message");
		try {
			text = "Nat: " + value; // to distinguish Project three is coming from this user
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Unable to encode value" + value, 1);
		}
		return text;
	}
	
	/**
	 * Html tag when Slack message is successfully sent.
	 * 
	 * @return html string
	 */
	private String getSuccessResponse() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Send Slack Message</title></head>");
		html.append("<body>");
		html.append("<p>Message sent!</p>");
		html.append("</body>");
		html.append("</html>");
		return html.toString();
	}
	
	/**
	 * Html tag when Slack message has failed to be sent.
	 * 
	 * @return html string
	 */
	private String getErrorResponse() {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Send Slack Message</title></head>");
		html.append("<body>");
		html.append("<p>There's some problem when sending message. Please try again.</p>");
		html.append("</body>");
		html.append("</html>");
		return html.toString();
	}
}