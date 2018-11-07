package cs601.project3;

import java.io.BufferedReader;
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
 * Manages config file of chat and search applications
 * 
 */
public class Config {
	private String reviewFileName;
	private String qaFileName;
	private String reviewType;
	private String qaType;
	private String token;
	private String channel;
	
	public void setReviewFileName(String reviewFileName) {
		this.reviewFileName = reviewFileName;
	}
	
	public String getReviewFileName() {
		return reviewFileName;
	}
	
	public void setQAFileName(String qaFileName) {
		this.qaFileName = qaFileName;
	}
	
	public String getQAFileName() {
		return qaFileName;
	}
	
	public void setReviewType(String reviewType) {
		this.reviewType = reviewType;
	}
	
	public String getReviewType() {
		return reviewType;
	}
	
	public void setQAType(String qaType) {
		this.qaType = qaType;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getQAType() {
		return qaType;
	}
	/**
	 * Set variables from the config.json file and assign them to variables in this class.
	 */
	public boolean setVariables() {
		Charset cs = Charset.forName("ISO-8859-1");
		Path path = Paths.get("config.json");
		Config config = new Config();
		try(
			BufferedReader reader = Files.newBufferedReader(path, cs);
		) {
			String line;
			Gson gson = new Gson();
			while((line = reader.readLine()) != null) {
				try {
					config = gson.fromJson(line, Config.class); // parse variables from config.json file to config object
					this.reviewFileName = config.reviewFileName;
					this.qaFileName = config.qaFileName;
					this.reviewType = config.reviewType;
					this.qaType = config.qaType;
					this.token = config.token;
					this.channel = config.channel;
				} catch(JsonSyntaxException jse) {
					// skip
				}
			}
		}
		catch(IOException ioe) {
			System.out.println("Please try again with correct config file.");
			return false;
		}
		return true;
	}
}
