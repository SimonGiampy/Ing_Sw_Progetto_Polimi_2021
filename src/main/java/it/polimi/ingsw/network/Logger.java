package it.polimi.ingsw.network;

import it.polimi.ingsw.model.util.Unicode;

/**
 * custom logger class for ease of debugging and enabling / disabling output
 */
public class Logger {
	
	private static final boolean DEBUG_ACTIVE = true; // activate or deactivate logging in the game
	
	private final String className; // name of the class sending the log message
	
	private Logger(String className) {
		this.className = className;
	}
	
	public static Logger getLogger(String className) {
		return new Logger(className);
	}
	
	/**
	 * blue colored information text
	 * @param text to be displayed
	 */
	public void info(String text) {
		if (DEBUG_ACTIVE) {
			System.out.println(Unicode.ANSI_CYAN + this.className + ": LOG INFO = " + text + Unicode.RESET);
		}
	}
	
	/**
	 * red colored error text
	 * @param text to be displayed
	 */
	public void error(String text) {
		if (DEBUG_ACTIVE) {
			System.out.println(Unicode.ANSI_RED + this.className + ": LOG ERROR = " + text + Unicode.RESET);
		}
	}
	
}
