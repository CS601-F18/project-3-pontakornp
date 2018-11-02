package cs601.project3;

public class ReviewSearchHandler implements Handler{
	
	public void handle(String method, String path, String query) {
		//determine get or post
		if(method == "GET") {
			showHTMLform();
		} else { // method == "POST"
			
		}
	}
	
	public String showHTMLform() {
		String html = "<html> " + 
				"<head><title>TEST</title></head>" + 
				"<body>" + 
					"<form>" +
						"Review search:<br>" + 
						"<input type='text' name='term'><br>" +
						"<input type='submit' value='Search'>" +
					"</form>" +
				"</body>" + 
				"</html>";
		return html;
	}
}
