package cs601.project3;

public class InvertedIndexSingleton {
	private static InvertedIndexSingleton instance;
	private InvertedIndex reviewIndex;
	private InvertedIndex qaIndex;
	
	private InvertedIndexSingleton() {
		AmazonFileHandling fileHandling = new AmazonFileHandling();
		Config config = new Config();
		config.setVariables();
		reviewIndex = fileHandling.readFile(config.getReviewFileName(), config.getReviewType());
		qaIndex = fileHandling.readFile(config.getQAFileName(), config.getQAType());
	}
	
	public static InvertedIndexSingleton getInstance() {
		if(instance == null) {
			instance = new InvertedIndexSingleton();
		}
		return instance;
	}
	public InvertedIndex getReviewInvertedIndex() {
		return reviewIndex;
	}
	public InvertedIndex getQAInvertedIndex() {
		return qaIndex;
	}
}