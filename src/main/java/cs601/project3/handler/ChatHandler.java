package cs601.project3.handler;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import cs601.project3.http.HTTPConstants;
import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

public class ChatHandler implements Handler{
	
	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post]
		if(req.getMethod().equals("GET")) {
			System.out.println("GET");
			doGet(resp);
		} else { // method == "POST"
			System.out.println("POST");;
			doPost(req, resp);
		}
	}
	
	private String getText(HTTPRequest req) {
		String text = "";
		String value = req.getQueryStringMap().get("message");
		try {
			text = "Nat: " + value; // to distinguish Project three is coming from this user
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Encoded error");
		}
		return text;
	}
	
	private boolean isParamKeyValid(HTTPRequest req, HTTPResponse resp) {
		if (!req.getQueryStringMap().containsKey("message") || req.getQueryStringMap().get("message").equals("")) {
			req.setStatusCode(400);
			Handler handler = new ErrorHandler();
			handler.handle(req, resp);
			return false;
		}
		return true;
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
		String urlParam = slackAPI.getUrlParameters();
		System.out.println(url);
		try {
			URL obj = new URL(url);
			HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
//			byte[] compressedData = compress(value);
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
		        System.out.println("Success.");
		        resp.setHeader(HTTPConstants.OK_HEADER);
		        resp.setPage(getSuccessResponse());
		    } else {
		    	inputStream = connection.getErrorStream();
		    	System.out.println("Bad Request.");
		    	resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
		    	resp.setPage(getErrorResponse());
		    }
		} catch (IOException e) {
			System.out.println("Connection fail.");
			resp.setHeader(HTTPConstants.BAD_REQUEST_HEADER);
			resp.setPage(getErrorResponse());
		}
		
		
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
