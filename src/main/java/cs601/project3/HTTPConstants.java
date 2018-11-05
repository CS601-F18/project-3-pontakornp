package cs601.project3;

import java.util.*;

public class HTTPConstants{
	public static final String OK_HEADER = "HTTP/1.0 200 OK\n" +
			"\r\n";
	public static final String BAD_REQUEST_HEADER = "HTTP/1.0 400 Bad Request\n" +
			"\r\n";
	public static final String NOT_FOUND_HEADER = "HTTP/1.0 404 Not Found\n" +
			"\r\n";
	public static final String METHOD_NOT_ALLOWED_HEADER = "HTTP/1.0 405 Method Not Allowed\n" +
			"\r\n";
	//reference: https://stackoverflow.com/questions/1005073/initialization-of-an-arraylist-in-one-line
	public static final List<String>  headers = Arrays.asList(
			"a-im", 
			"accept", 
			"accept-charset", 
			"accept-encoding", 
			"accept-language", 
			"accept-datetime", 
			"access-control-request-method", 
			"access-control-request-headers", 
			"authorization", 
			"cache-control", 
			"connection", 
			"content-length", 
			"content-md5", 
			"content-type", 
			"cookie", 
			"date", 
			"expect", 
			"forwarded", 
			"from", 
			"host", 
			"if-match", 
			"if-modified-since", 
			"if-none-match", 
			"if-range", 
			"if-unmodified-since", 
			"max-forwards", 
			"origin", 
			"pragma", 
			"proxy-authorization", 
			"range", 
			"referer", 
			"te", 
			"user-agent", 
			"upgrade", 
			"via",
			"upgrade-insecure-requests"
	);
}
