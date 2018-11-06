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

public class ChatHandler implements Handler{
	
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post]
		if(req.getMethod().equals("GET")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "GET Handle method: " + req.getMethod(), 0);
			doGet(resp);
		} else { // method == "POST"
			ChatAndSearchApplicationLogger.write(Level.INFO, "POST Handle method: " + req.getMethod(), 0);
			doPost(req, resp);
		}
	}
	
	private void doGet(HTTPResponse resp) {
		String html = "<html> " + 
				"<head><title>Send Slack Message</title></head>" + 
				"<body>" + 
					"<form action=\"/slackbot\" method=\"post\">" +
						"Message:<br/>" + 
						"<input type=\"text\" name=\"message\"/><br/>" +
						"<input type=\"submit\" value=\"Send\"/>" +
					"</form>" +
				"</body>" +
				"</html>";
		resp.setHeader(HTTPConstants.OK_HEADER);
		resp.setPage(html);
	}
	
	private void doPost(HTTPRequest req, HTTPResponse resp) {
		if(!isParamKeyValid(req, resp)) {
			return;
		}
		String text = getText(req);
		SlackPostMessageAPI slackAPI = new SlackPostMessageAPI(text);
		String url = slackAPI.getTargetUrl();
		ChatAndSearchApplicationLogger.write(Level.INFO, "Slack Target URL: " + url, 0);
		String urlParam = slackAPI.getUrlParameters();
		ChatAndSearchApplicationLogger.write(Level.INFO, "Slack URL Params: " + urlParam, 0);
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
		        ChatAndSearchApplicationLogger.write(Level.INFO, "Success, Slack API Response Code: " + connection.getResponseCode(), 0);
		    } else {
		    	inputStream = connection.getErrorStream();
		    	resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
		    	resp.setPage(getErrorResponse());
		    	ChatAndSearchApplicationLogger.write(Level.INFO, "Bad Request, Slack API Response Code: " + connection.getResponseCode(), 0);
		    }
		} catch (IOException e) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Connection Error, Status Code: " + 400, 1);
			resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
			resp.setPage(getErrorResponse());
		}
	}
	
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("message") || req.getQueryStringMap().get("message").equals("")) {
			ChatAndSearchApplicationLogger.write(Level.INFO, "Query string map not conain main param or value is null", 0);
			req.setStatusCode(400);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
	}
	
	private String getText(HTTPRequest req) {
		String text = "";
		String value = req.getQueryStringMap().get("message");
		ChatAndSearchApplicationLogger.write(Level.INFO, "Main param's value: " + value, 0);
		try {
			text = "Nat: " + value; // to distinguish Project three is coming from this user
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			ChatAndSearchApplicationLogger.write(Level.WARNING, "Unable to encode value" + value, 1);
		}
		return text;
	}
	
	private String getSuccessResponse() {
		String html = "<html> " + 
				"<head><title>Send Slack Message</title></head>" + 
				"<body>" + 
					"<p>Message sent!</p>" +
				"</body>" +
				"</html>";
		return html;
	}
	
	private String getErrorResponse() {
		String html = "<html> " + 
				"<head><title>Send Slack Message</title></head>" + 
				"<body>" + 
					"<p>There's some problem when sending message. Please try again.</p>" +
				"</body>" +
				"</html>";
		return html;
	}
}
