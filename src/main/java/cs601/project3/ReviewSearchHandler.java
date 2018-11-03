package cs601.project3;

public class ReviewSearchHandler implements Handler{

	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		if(req.getMethod() == "GET") {
			resp.setPage(getForm());
		} else { // method == "POST"
			
		}
	}
	
	private String getForm() {
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
