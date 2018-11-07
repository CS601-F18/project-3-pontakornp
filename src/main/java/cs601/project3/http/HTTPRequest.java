package cs601.project3.http;

import java.util.HashMap;

/**
 * 
 * @author pontakornp
 *
 * HTTP Request stores method, path, query string map, and status code of the request
 *
 */
public class HTTPRequest {
	private String method;
	private String path;
	private HashMap<String, String> queryStringMap;
	private int statusCode;
	
	public HTTPRequest() {
		this.method = "";
		this.path = "";
		this.queryStringMap = new HashMap<String, String>();
		this.statusCode = 200;
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