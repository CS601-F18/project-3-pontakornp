package cs601.project3.invertedindex;

/**
 * 
 * @author pontakornp
 * 
 * This class represents review which is the child of customer engagement class.
 * It contains reviewer ID, review text, and overall score as additional variable of its parent.
 *
 */
public class Review extends CustomerEngagement{
	private String reviewerID;
	private String reviewText;
	private double overall;
	
	public Review() {
		super("");
		reviewerID = "";
		reviewText = "";
		overall = 0.0;
	}
	
	public Review(String asin, String reviewerID, String reviewText, double overall) {
		super(asin);
		this.reviewerID = reviewerID;
		this.reviewText = reviewText;
		this.overall = overall;
	}
	
	public String getReviewerID() {
		return this.reviewerID;
	}
	
	public void setReviewerID(String reviewerID) {
		this.reviewerID = reviewerID;
	}
	
	public String getReviewText() {
		return this.reviewText;
	}
	
	public void setReviewText(String value) {
		this.reviewText = value;
	}
	
	public double getOverall() {
		return this.overall;
	}
	
	public void setOverall(double overall) {
		this.overall = overall;
	}
	
	public String toString() {
		return "Reviewer ID: " + reviewerID + "\n"
				+ "ASIN: " + asin + "\n"
				+ "Review text: " + reviewText + "\n"
				+ "Overall score: " + overall + "\n";
	}
}