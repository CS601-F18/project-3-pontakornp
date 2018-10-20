package cs601.project3;

/**
 * 
 * @author pontakornp
 * 
 * This class represents customer engagement
 * which is the parent class for review and qa classes.
 * 
 * Contains getter and setter method for ASIN, product identifier.
 *
 */
public class CustomerEngagement {
	protected String asin;
	
	public CustomerEngagement() {
		this.asin = "";
	}
	
	public CustomerEngagement(String asin) {
		this.asin = asin;
	}
	
	public String getASIN() {
		return asin;
	}
	
	public void setASIN(String asin) {
		this.asin = asin;
	}
	
	public String toString() {
		return "ASIN: " + asin;
	}
}