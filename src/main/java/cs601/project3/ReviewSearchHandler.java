package cs601.project3;

public class ReviewSearchHandler implements Handler{

	public void handle(HTTPRequest req, HTTPResponse resp) {
		//determine get or post
		if(req.getMethod().equals("GET")) {
			System.out.println("GET");
			resp.setHeader(getHeader());
			resp.setPage(getForm());
		} else { // method == "POST"
			System.out.println("POST");
		}
	}
	
	private String getHeader() {
		String headers = "HTTP/1.0 200 OK\n";
		return headers;
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
