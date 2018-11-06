package cs601.project3.invertedindex;

import cs601.project3.Config;

public class InvertedIndexSingleton {
	private static InvertedIndexSingleton INSTANCE;
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
		if(INSTANCE == null) {
			INSTANCE = new InvertedIndexSingleton();
		}
		return INSTANCE;
	}
	public InvertedIndex getReviewInvertedIndex() {
		return reviewIndex;
	}
	public InvertedIndex getQAInvertedIndex() {
		return qaIndex;
	}
}