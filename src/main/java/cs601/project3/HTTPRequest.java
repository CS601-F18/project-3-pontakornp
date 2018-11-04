package cs601.project3;

public class HTTPRequest {
	private String method;
	private String serverName;
	private String path;
	private String queryString;
	
	public HTTPRequest() {
		this.method = "";
		this.serverName = "";
		this.path = "";
		this.queryString = "";
	}
	public HTTPRequest(String method, String serverName, String path, String queryString) {
		this.method = method;
		this.serverName = serverName;
		this.path = path;
		this.queryString = queryString;
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
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
}
