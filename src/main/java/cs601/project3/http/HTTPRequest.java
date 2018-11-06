package cs601.project3.http;

import java.util.HashMap;

public class HTTPRequest {
	private String method;
	private String path;
	private String queryString;
	private HashMap<String, String> queryStringMap;
	private int statusCode;
	
	public HTTPRequest() {
		this.method = "";
		this.path = "";
		this.queryString = "";
		this.queryStringMap = new HashMap<String, String>();
		this.statusCode = 200;
	}
	
	public HTTPRequest(String method, String path, String queryString, HashMap<String, String> queryStringMap, int statusCode) {
		this.method = method;
		this.path = path;
		this.queryString = queryString;
		this.queryStringMap = queryStringMap;
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
	
//	public String getQueryString() {
//		return queryString;
//	}
//	
//	public void setQueryString(String queryString) {
//		this.queryString = queryString;
//	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public HashMap<String, String> getQueryStringMap() {
		return queryStringMap;
	}
	
	public void setQueryStringMap(HashMap<String, String> queryStringMap) {
		this.queryStringMap = queryStringMap;
	}
}
