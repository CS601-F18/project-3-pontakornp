package cs601.project3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author pontakornp
 *  
 * Contains methods for handling file operations.
 * Important methods contained are for check arguments, read from a file, and receive user input commands.
 * Helper method contained is for check for file existence.
 * 
 */
public class AmazonFileHandling {
	
	/**
	 * Checks whether command line input is valid
	 * 
	 * @param args - expects arguments from the command line
	 * @return true or false
	 */
	public boolean isInputValid(String[] args) {
		String msg = "";
		if(!args[0].equals("-reviews") || !args[2].equals("-qa")) {
			msg += "Incorrect input format.\n";
		}
		if (args[1] == "" || !isFileExist(args[1])) {
			msg += "Incorrect Review file name.\n";
		}
		if (args[3] == "" || !isFileExist(args[3])) {
			msg += "Incorrect Q&A file name.\n";
		}
		if(!msg.equals("")) {
			msg += "Please try again.";
			System.out.println(msg);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * Reads from a file in the project directory given the file name, and return void
	 * @param fileName - expects the name of the input file
	 * @param customerEngagmentType - expects either "review" or "qa"
	 */
	public InvertedIndex readFile(String fileName, String customerEngagementType) {
		Charset cs = Charset.forName("ISO-8859-1");
		Path path = Paths.get(fileName);
		InvertedIndex index = new InvertedIndex();
		try(
			BufferedReader reader = Files.newBufferedReader(path, cs);
		) {
			String line;
			Gson gson = new Gson();
			while((line = reader.readLine()) != null) {
				try {
					if(customerEngagementType == "review") {
						Review review = gson.fromJson(line, Review.class); // parse json to Review object
						index.addToMap(review, customerEngagementType); // add review to map in inverted index
					} else {
						QA qa = gson.fromJson(line, QA.class); // parse json to QA object
						index.addToMap(qa, customerEngagementType); // add qa to map in inverted index
					}
				} catch(JsonSyntaxException jse) {
					// skip
				}
			}
			index.sortTermMap();
			return index;
		}
		catch(IOException ioe) {
			System.out.println("Please try again with correct input.");
		}
		return index;
	}
	
	/**
	 * 
	 * @param line - expects line of input from a user
	 * @param reviewIndex - expects InvertedIndex object that has review data
	 * @param qaIndex - expects InvertedIndex object that has qa data
	 */
	public void execute(String line, InvertedIndex reviewIndex, InvertedIndex qaIndex) {
		if(line == "") {
			System.out.println("Please try again with the correct format.\n");
			return;
		} else if(line.equals("help")) {
			System.out.println("Command List:\n"
					+ "\tfind <asin> - print all review and qa lists e.g. find 1234567890\n"
					+ "\treviewsearch <term> - print all review lists that contain term e.g. reviewsearch hello\n"
					+ "\tqasearch <term> - print all review lists that contain term e.g. qasearch hello\n"
					+ "\treviewpartialsearch <term> - print all review lists that contain partially matched term e.g. reviewpartialsearch hello\n"
					+ "\tqapartialsearch <term> - print all review lists that contain partially matched term e.g. qapartialsearch hello\n"
			);
			return;
		} else if(line.equals("exit")) {
			System.out.println("Exit program.\n");
			return;
		}
		String[] parts = line.split(" ");
		if(parts.length != 2) {
			System.out.println("Please try again with the correct format.\n");
			return;
		}
		String command = parts[0].toLowerCase();
		String text = parts[1].replaceAll("[^A-Za-z0-9]", "").toLowerCase();  // remove all non-alphanumeric characters
		if(command.equals("find")) {
			boolean isReviewFound = reviewIndex.find(text);
			boolean isQAFound = qaIndex.find(text);
			if(!isReviewFound && !isQAFound) {
				System.out.println("ASIN is not found. Please try to find other ASIN.\n");
			}
		} else if(command.equals("reviewsearch")) {
			if(!reviewIndex.search(text)) {
				System.out.println("Search term is not found. Please try other search term.\n");
			}
		} else if(command.equals("qasearch")) {
			if(!qaIndex.search(text)) {
				System.out.println("Search term is not found. Please try other search term.\n");
			}
		} else if(command.equals("reviewpartialsearch")) {
			if(!reviewIndex.partialSearch(text)) {
				System.out.println("Search term is not found. Please try other search term.\n");
			}
		} else if(command.equals("qapartialsearch")) {
			if(!qaIndex.partialSearch(text)) {
				System.out.println("Search term is not found. Please try other search term.\n");
			}
		} else {
			System.out.println("Please try again with the correct format.\n");
		}
	}
	
	/**
	 * 
	 * helper method - check whether file exists or not from the filename
	 * @param filename
	 * @return
	 */
	private boolean isFileExist(String fileName) {
		if(!(new File(fileName).exists())) {
			return false;
		}
		return true;
	}
}