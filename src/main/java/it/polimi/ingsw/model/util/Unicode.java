package it.polimi.ingsw.model.util;

public enum Unicode {

	//Useful unicode codes
	//Box for tiles and cards
	HORIZONTAL("\u2500"),
	VERTICAL("\u2502"),
	TOP_LEFT("\u250c"),
	TOP_RIGHT("\u2510"),
	BOTTOM_LEFT("\u2514"),
	BOTTOM_RIGHT("\u2518"),
	T_SHAPE("\u252c"),			//┬
	REVERSE_T_SHAPE("\u2534"),	//┴
	CROSS_MARKER("\u256c"),
	CROSS2("\u271E"),

	TICK("\u2714"),
	CROSS_REPORT("\u274c"),
	
	DOT("●"),

	//Escape codes for colors and text formatting
	
	UNDERLINE("\033[4m"), //Underlined text
	
	ANSI_RED("\033[0;31m"),     // RED
	ANSI_GREEN("\033[0;32m"),   // GREEN
	ANSI_YELLOW("\033[0;33m"),  // YELLOW
	
	ANSI_CYAN("\033[0;36m"),    // CYAN


	// Bold Colors
	BLACK_BOLD("\033[1;30m"),  // BLACK
	RED_BOLD("\033[1;31m"),    // RED
	GREEN_BOLD("\033[1;32m"),  // GREEN
	YELLOW_BOLD("\033[1;33m"), // YELLOW
	BLUE_BOLD("\033[1;34m"),   // BLUE
	PURPLE_BOLD("\033[1;35m"), // PURPLE
	
	YELLOW_BRIGHT("\033[0;93m"), // YELLOW
	
	WHITE_BACKGROUND_BRIGHT("\033[0;107m"); // WHITE
	
    public static final String RESET = "\033[0m";  // Text Reset

	public final String code;
	Unicode(String code) {
		this.code = code;
	}

	@Override
	public String toString(){
		return code;
	}
}