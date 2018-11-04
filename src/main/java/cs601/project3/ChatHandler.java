package cs601.project3;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import java.io.*;

import javax.net.ssl.HttpsURLConnection;

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
	
	private String sendMessage(HTTPRequest req, HTTPResponse resp) {
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
		SlackPostMessageAPI slackAPI = new SlackPostMessageAPI(value);
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
		    InputStream inputStream = connection.getInputStream();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		    StringBuilder response = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    reader.close();
		    return response.toString();
		    
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
						"Message:<br>" + 
						"<input type=\"text\" name=\"message\"><br>" +
						"<input type=\"submit\" value=\"Send\">" +
					"</form>" +
				"</body>" +
				"</html>";
		return html;
	}
}
