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
		//determine get or post
		resp.setHeader(HTTPConstants.OK_HEADER);
		if(req.getMethod().equals("GET")) {
			System.out.println("GET");
			resp.setPage(getForm());
		} else { // method == "POST"
			System.out.println("POST");
			resp.setPage(sendMessage(req, resp));
		}
	}
	
	private String getText(HTTPRequest req) {
		String text = "";
		System.out.println(req.getQueryString());
		int firstSignIndex = req.getQueryString().indexOf("=");
		String key = req.getQueryString().substring(0, firstSignIndex);
		String value = req.getQueryString().substring(firstSignIndex + 1);
		System.out.println(key + ":" + value);
		// check if query string valid
		if(!key.equals("message") || value.equals("")) {
			System.out.println("Wrong message. Please try again.");
			return getForm();
		}
		 // to distinguish Project three is coming from this user
		try {
			text = "Nat: " + value;
			text = URLEncoder.encode(text, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			System.out.println("Encoded error");
		}
		return text;
	}
	private String sendMessage(HTTPRequest req, HTTPResponse resp) {
//		System.out.println(req.getQueryString());
//		int firstSignIndex = req.getQueryString().indexOf("=");
//		String key = req.getQueryString().substring(0, firstSignIndex);
//		String value = req.getQueryString().substring(firstSignIndex + 1);
//		System.out.println(key + ":" + value);
//		// check if query string valid
//		if(!key.equals("message") || value.equals("")) {
//			System.out.println("Wrong message. Please try again.");
//			return getForm();
//		}
//		String message = "Nat: " + value; // to distinguish Project three is coming from this user
//		try {
//			String encodedMessage = URLEncoder.encode(message, "UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			System.out.println("Encoded error");
//		}
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
		        return getSuccessResponse();
//		        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//			    StringBuilder response = new StringBuilder();
//			    String line;
//			    while ((line = reader.readLine()) != null) {
//			      response.append(line);
//			      response.append('\r');
//			    }
//			    reader.close();
//			    return response.toString();
		    } else {
		    	inputStream = connection.getErrorStream();
		    	System.out.println("Bad Request.");
		    	return getErrorResponse();
		    }
		    
		    
//		    return getSuccessResponse();
		} catch (IOException e) {
			System.out.println("Connection fail.");
			return getErrorResponse();
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
	
	private String getForm() {
		String html = "<html> " + 
				"<head><title>Send Slack Message</title></head>" + 
				"<body>" + 
					"<form action\"/slackbot\" method=\"post\">" +
						"Message:<br/>" + 
						"<input type=\"text\" name=\"message\"/><br/>" +
						"<input type=\"submit\" value=\"Send\"/>" +
					"</form>" +
				"</body>" +
				"</html>";
		return html;
	}
}
