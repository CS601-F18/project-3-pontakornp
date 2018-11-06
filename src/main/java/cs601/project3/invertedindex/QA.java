package cs601.project3.invertedindex;

/**
 * 
 * @author pontakornp
 * 
 * This class represents qa which is the child of customer engagement class.
 * It contains question and answer as additional variables of its parent.
 *
 */
public class QA extends CustomerEngagement {
	private String question;
	private String answer;
	
	public QA() {
		super("");
		question = "";
		answer = "";
	}
	
	public QA(String asin, String question, String answer) {
		super(asin);
		this.question = question;
		this.answer = answer;
	}
	
	public String getQueston() {
		return question;
	}
	
//	public void setQuestion(String value) {
//		question = value;
//	}
	
	public String getAnswer() {
		return answer;
	}
	
//	public void setAnswer(String value) {
//		answer = value;
//	}
	
	public String toString() {
		return "ASIN: " + asin + "\n"
				+ "Question: " + question + "\n"
				+ "Answer: " + answer + "\n";
	}
}