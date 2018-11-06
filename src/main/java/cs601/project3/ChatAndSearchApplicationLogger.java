package cs601.project3;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatAndSearchApplicationLogger {
	private static Logger LOGGER = null;
	private static Handler fileHandler  = null;
//	private static Formatter simpleFormatter = null;
	
	public static void initialize(String logName, String logFile)	{
		LOGGER = Logger.getLogger(logName);
		try	{
			fileHandler = new FileHandler(logFile);
		} catch(IOException ioe)	{
			System.out.println("Filer handler error");
		}
		
//		fileHandler.setFormatter(simpleFormatter);
		
		LOGGER.addHandler(fileHandler);
		
		LOGGER.log(Level.INFO, "Logger Name: " + logName + "   |    LogFile: " + logFile, 0);
	}
	
	public static void write(Level level, String msg, int thrown) {
		LOGGER.log(level, msg, thrown);
	}
}
