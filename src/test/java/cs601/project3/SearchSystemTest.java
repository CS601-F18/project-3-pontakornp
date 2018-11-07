package cs601.project3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import cs601.project3.http.HTTPClient;

/**
 * 
 * @author pontakornp
 *
 * Performs JUnit system test for search application
 */
public class SearchSystemTest {
	
	@Test
	public void testCorrectGetReviewSearch() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Review Search</title></head>");
		html.append("<body>");
		html.append("<form action=\"/reviewsearch\" method=\"post\">");
		html.append("Review search:<br/>");
		html.append("<input type=\"text\" name=\"query\"/><br/>");
		html.append("<input type=\"submit\" value=\"Search\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/reviewsearch?unused=params", ""));
	}
	
	@Test
	public void testCorrectGetFind() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>ASIN Search</title></head>");
		html.append("<body>");
		html.append("<form action=\"/find\" method=\"post\">");
		html.append("ASIN search:<br/>");
		html.append("<input type=\"text\" name=\"asin\"/><br/>");
		html.append("<input type=\"submit\" value=\"Search\"/>");
		html.append("</form>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/find", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/find?unused=params", ""));
	}
	
	@Test
	public void testCorrectPostReviewSearch() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>"); 
		html.append("<head><title>Review Search Results</title></head>"); 
		html.append("<body>"); 
		html.append("<p>Search term: acclimates</p>");
		html.append("<table style=\"width:100%\">");
		html.append("<tr>");
		html.append("<th style=\"border: 1px solid #dddddd;\">ASIN</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Reviewer ID</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Review Text</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Overall Score</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Number of Occurences</th>");
	    html.append("</tr>");
	    html.append("<tr>");
		html.append("<td style=\"border: 1px solid #dddddd;\">B0002OKCXE</td>");
	    html.append("<td style=\"border: 1px solid #dddddd;\">A1VS1LL905P5L5</td>");
	    html.append("<td style=\"border: 1px solid #dddddd;\">It seems that this keyboard acts a little like those gold fish you bring home in a plastic bag from the pet shop.  You have to put the whole bag in the tank until it acclimates to it's new surroundings.  I went from wanting to love the thing for the sheer coolness factor....to boxing it up to send back when it wouldn't sync to my Axim x50v....to giving it another chance and after it was good and ready to work flawlessly, I now love it again.  The keyboard is smaller than standard but very useable.  It's great for writing a longer e-mail, inputting contacts, or writing a letter in word, when you don't want to peck at the screen with your stylus endlessly.  The construction feels solid and opening and closing is a snap.  Pun intended. The only major flaw is with the cradle that holds your PDA.  It is removable which is the desired use to space the PDA a little further away from you.  But the feet that clip it to the keyboard just don't want to let go.  The plastic it's made out of doesn't seem especially robust and I feel certain it will snap off eventually.  Also the curved lower part of the cradle that holds your Axim, et al, is a little under developed and if bumped will readily jettison what it's holding.  Other than that it works very well and never ceases to make me grin when everyone passing your table at Starbucks has to stop and ask you about it. I previously gave it one star, but now that it's acclimated and working I give it 4 only because of the cradle shortcoming or else it would have been a full 5.  I know it shows one star, but Amazon wholdn't allow me to edit the scale.</td>");
	    html.append("<td style=\"border: 1px solid #dddddd;\">1.0</td>");
	    html.append("<td style=\"border: 1px solid #dddddd;\">1</td>");
    	html.append("</tr>");
    	html.append("</table>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/reviewsearch", "query=acclimates"));
	}

	@Test
	public void testCorrectPostFind() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>Review and QA documents</title></head>");
		html.append("<body>");
		html.append("<p>Search ASIN: B000OQGG6M</p>");
		html.append("<table style=\"width:100%\">");
		html.append("<tr>");
		html.append("<th style=\"border: 1px solid #dddddd;\">ASIN</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Question</th>");
		html.append("<th style=\"border: 1px solid #dddddd;\">Answer</th>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"border: 1px solid #dddddd;\">B000OQGG6M</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">front facing video: Is it true this model doesn't have front facing video camera?</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">I got it 2 days ago from Amazon. &amp;gt;&amp;gt; Is it true this model doesn't have front facing video camera? NO front facing video camera &amp;gt;&amp;gt;Also does this use flash memory or a mini harddrive? I did not disassemble to find out, but I can heard little noise when extremely quiet and saving large files. So, I think it is mini harddrive.</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"border: 1px solid #dddddd;\">B000OQGG6M</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">Cingular HSDPA Setting: But is it gonna be a lot of trouble setting up HSDPA with Cingular?</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">HSDPA is enabled and works right out of the box. You can tell because the icon will show &quot;3G&quot; and &quot;H&quot; for HSDPA. If by some chance it does not. There is an &quot;HSDPA Switch&quot; icon in your Start/Settings/Connection menu to enable it. If unfilled, check the box and restart and you are fine. If after all this and you just see &quot;E&quot; then you are in an area that does not yet have HSDPA connectivity and you have defaulted down to the slower EDGE network speeds. It changes back and forth seemlessly as you travel so you don't have anything to configure once HSDPA is enabled. Cheers.</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td style=\"border: 1px solid #dddddd;\">B000OQGG6M</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">HTC Shift: Will Amazon.com offer pre-release of the new HTC Shift?</td>");
		html.append("<td style=\"border: 1px solid #dddddd;\">It doesn't have a GPS, nor FM radio, and needs to replace hard drive with flash drive to eliminate moving parts and increases battery life and needs full sized SD slot to accommodate new 32 gb sd cards http://blog.scifi.com/tech/archives/2007/08/23/toshiba_unleash_1.html. If HTC or any vender could produce a product like this it would have a killer product.</td>");
		html.append("</tr>");
		html.append("</table>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/find", "asin=B000OQGG6M"));
	}
	
	@Test
	public void testBadRequest() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>"); 
		html.append("<head><title>400 Bad Request</title></head>");
		html.append("<body>");
		html.append("<p>Invalid request.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/reviewsearch", "quer=test"));
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/reviewsearch", "query=tree&query=shop"));
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/find", "ain=test123"));
		assertEquals(htmlString, HTTPClient.connect("localhost", "POST", "/find", "asin=b0002syc5o&asin=test123"));
	}
	
	@Test
	public void testMethodNotAllowed() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>405 Method Not Allowed</title></head>");
		html.append("<body>");
		html.append("<p>Page not supported.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "PUT", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "DELETE", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "PUT", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "DELETE", "/reviewsearch", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "PUT", "/find", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "DELETE", "/find", ""));
	}
	
	@Test
	public void testNotFound() {
		HTTPClient.PORT = 8080;
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head><title>404 Not Found</title></head>");
		html.append("<body>");
		html.append("<p>Page not found.</p>");
		html.append("</body>");
		html.append("</html>");
		String htmlString = html.toString();
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/", ""));
		assertEquals(htmlString, HTTPClient.connect("localhost", "GET", "/unknownpath", ""));
	}
}