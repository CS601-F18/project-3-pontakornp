package cs601.project3;

public class HTTPRequest {
	private String method;
	private String path;
	private String query;
	
	public HTTPRequest(String method, String path, String query) {
		this.method = method;
		this.path = path;
		this.query = query;
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
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
}
