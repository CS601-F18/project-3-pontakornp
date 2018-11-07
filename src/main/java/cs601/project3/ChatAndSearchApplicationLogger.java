package cs601.project3;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author pontakornp
 *
 * Logs information in the process of running chat and search applications
 * 
 */
public class ChatAndSearchApplicationLogger {
	private static Logger LOGGER = null;
	private static Handler fileHandler  = null;
	public static void initialize(String logName, String logFile)	{
		LOGGER = Logger.getLogger(logName);
		try	{
			fileHandler = new FileHandler(logFile);
		} catch(IOException ioe)	{
			System.out.println("Filer handler error");
		}
		LOGGER.addHandler(fileHandler);
		LOGGER.log(Level.INFO, "Logger Name: " + logName + "   |    LogFile: " + logFile, 0);
	}
	public static void write(Level level, String msg, int thrown) {
		LOGGER.log(level, msg, thrown);
	}
}