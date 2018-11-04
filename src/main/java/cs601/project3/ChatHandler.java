package cs601.project3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

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
	
	private String getSlackURL(String text) {
		SlackPostMessageAPI slackAPI = new SlackPostMessageAPI(text);
		String url = slackAPI.getUrl();
		return url;
		
	}
	private String sendMessage(HTTPRequest req, HTTPResponse resp) {
		
		
		int firstSignIndex = req.getQueryString().indexOf("=");
		String key = req.getQueryString().substring(0, firstSignIndex);
		String value = req.getQueryString().substring(firstSignIndex + 1);
		System.out.println(key + ":" + value);
		// check if query string valid
		if(!key.equals("message") || value.equals("")) {
			System.out.println("Wrong message. Please try again.");
			return getForm();
		}
		String url = getSlackURL(value);
//		try {
//			URL obj = new URL(url);
//			HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
////			byte[] compressedData = compress(value);
//			//add request header
//			connection.setRequestMethod("POST");
//		    connection.addRequestProperty("Accept", "application/json");
//		    connection.addRequestProperty("Connection", "close");
//		    connection.addRequestProperty("Content-Encoding", "gzip"); // We gzip our request
//		    connection.addRequestProperty("Content-Length", String.valueOf(compressedData.length));
//		    connection.setRequestProperty("Content-Type", "application/json"); // We send our data in JSON 
//		} catch (IOException e) {
//			System.out.println("Connection fail.");
//		}
//		
		return getSuccessResponse();
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
