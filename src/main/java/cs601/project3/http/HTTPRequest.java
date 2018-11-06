package cs601.project3.http;

public class HTTPRequest {
	private String method;
	private String path;
	private String queryString;
	private int statusCode;
	
	public HTTPRequest() {
		this.method = "";
		this.path = "";
		this.queryString = "";
		this.statusCode = 200;
	}
	
	public HTTPRequest(String method, String path, String queryString, int statusCode) {
		this.method = method;
		this.path = path;
		this.queryString = queryString;
		this.statusCode = statusCode;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getQueryString() {
		return queryString;
	}
	
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
