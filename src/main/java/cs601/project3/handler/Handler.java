package cs601.project3.handler;

import cs601.project3.http.HTTPRequest;
import cs601.project3.http.HTTPResponse;

/**
 * 
 * @author pontakornp
 *
 * Handler interface provides handle method.
 * Used for specific handler to implement and modify handle method do a specific task according to the request of the application.
 *
 */
public interface Handler {
	
	/**
	 * 
	 * Called by a HTTPSever to handle specific task of the application.
	 */
	public void handle(HTTPRequest req, HTTPResponse resp);

}
