package cs601.project3.http;

public class HTTPResponse {
	private String headers;
	private String page;

	public String getHeaders() {
		return headers;
	}

	public void setHeader(String headers) {
		this.headers = headers;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
