package cs601.project3.invertedindex;

/**
 * 
 * @author pontakornp
 * 
 * This class keeps track of the frequency of term 
 * for each customer engagement objects.
 * 
 * Contains customer engagement object and term frequency.
 *
 */
public class CustomerEngagementFrequency implements Comparable<CustomerEngagementFrequency> {
	private CustomerEngagement ce;
	private int freq;
	
	public CustomerEngagementFrequency(CustomerEngagement ce, int freq) {
		this.ce = ce;
		this.freq = freq;
	}
	public void setCustomerEngagement(CustomerEngagement ce) {
		this.ce = ce;
	}
	public CustomerEngagement getCE() {
		return ce;
	}
	
	public void setFreq(int freq) {
		this.freq = freq;
	}

	public Integer getFreq() {
		return freq;
	}
	
	public void incrementFreq() {
		freq = freq + 1;
	}
	
	public String toString() {
		return ce.toString();
	}
	
	/**
	 * 
	 * Overrides compareTo method from Comparable class.
	 * When use with Collections.sort(), 
	 * this method will help sort list of customer engagement frequency object 
	 * by term frequency of customer engagement in descending order 
	 * i.e. customer engagement with more frequency will appear in the list first.
	 */
	public int compareTo(CustomerEngagementFrequency cef) {
		int compareFreq = ((CustomerEngagementFrequency)cef).getFreq();
		return compareFreq - freq; // use for sorting in descending order
	}
}