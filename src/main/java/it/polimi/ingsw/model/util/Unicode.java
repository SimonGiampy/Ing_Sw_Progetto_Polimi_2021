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

	// Dot for Dev cards
	GREEN_DOT("\033[1;32m●"+Unicode.RESET),
	BLUE_DOT("\033[1;34m●"+Unicode.RESET),
	YELLOW_DOT("\033[1;33m●"+Unicode.RESET),
	PURPLE_DOT("\033[1;35m●"+Unicode.RESET),
	DOT("●"),

	//Escape codes for colors and text formatting
	
	BOLD("\033[1m"), //Bold text
	ITALIC("\033[3m"), //Italic text
	UNDERLINE("\033[4m"), //Underlined text
	
	// Regular Colors
	ANSI_BLACK("\033[0;30m"),   // BLACK
	ANSI_RED("\033[0;31m"),     // RED
	ANSI_GREEN("\033[0;32m"),   // GREEN
	ANSI_YELLOW("\033[0;33m"),  // YELLOW
	ANSI_BLUE("\033[0;34m"),    // BLUE
	ANSI_PURPLE("\033[0;35m"),  // PURPLE
	ANSI_CYAN("\033[0;36m"),    // CYAN
	ANSI_WHITE("\033[0;37m"),   // WHITE

	// Bold Colors
	BLACK_BOLD("\033[1;30m"),  // BLACK
	RED_BOLD("\033[1;31m"),    // RED
	GREEN_BOLD("\033[1;32m"),  // GREEN
	YELLOW_BOLD("\033[1;33m"), // YELLOW
	BLUE_BOLD("\033[1;34m"),   // BLUE
	PURPLE_BOLD("\033[1;35m"), // PURPLE
	CYAN_BOLD("\033[1;36m"),   // CYAN
	WHITE_BOLD("\033[1;37m"),  // WHITE

	// Underline Colors
	BLACK_UNDERLINED("\033[4;30m"),  // BLACK
	RED_UNDERLINED("\033[4;31m"),    // RED
	GREEN_UNDERLINED("\033[4;32m"),  // GREEN
	YELLOW_UNDERLINED("\033[4;33m"), // YELLOW
	BLUE_UNDERLINED("\033[4;34m"),   // BLUE
	PURPLE_UNDERLINED("\033[4;35m"), // PURPLE
	CYAN_UNDERLINED("\033[4;36m"),   // CYAN
	WHITE_UNDERLINED("\033[4;37m"),  // WHITE

	// Background Colors
	BLACK_BACKGROUND("\033[40m"),  // BLACK
	RED_BACKGROUND("\033[41m"),    // RED
	GREEN_BACKGROUND("\033[42m"),  // GREEN
	YELLOW_BACKGROUND("\033[43m"), // YELLOW
	BLUE_BACKGROUND("\033[44m"),   // BLUE
	PURPLE_BACKGROUND("\033[45m"), // PURPLE
	CYAN_BACKGROUND("\033[46m"),   // CYAN
	WHITE_BACKGROUND("\033[47m"),  // WHITE

	// High Intensity Colors
	BLACK_BRIGHT("\033[0;90m"),  // BLACK
	RED_BRIGHT("\033[0;91m"),    // RED
	GREEN_BRIGHT("\033[0;92m"),  // GREEN
	YELLOW_BRIGHT("\033[0;93m"), // YELLOW
	BLUE_BRIGHT("\033[0;94m"),   // BLUE
	PURPLE_BRIGHT("\033[0;95m"), // PURPLE
	CYAN_BRIGHT("\033[0;96m"),   // CYAN
	WHITE_BRIGHT("\033[0;97m"),  // WHITE

	// Bold High Intensity Colors
	BLACK_BOLD_BRIGHT("\033[1;90m"), // BLACK
	RED_BOLD_BRIGHT("\033[1;91m"),   // RED
	GREEN_BOLD_BRIGHT("\033[1;92m"), // GREEN
	YELLOW_BOLD_BRIGHT("\033[1;93m"),// YELLOW
	BLUE_BOLD_BRIGHT("\033[1;94m"),  // BLUE
	PURPLE_BOLD_BRIGHT("\033[1;95m"),// PURPLE
	CYAN_BOLD_BRIGHT("\033[1;96m"),  // CYAN
	WHITE_BOLD_BRIGHT("\033[1;97m"), // WHITE

	// High Intensity backgrounds colors
	BLACK_BACKGROUND_BRIGHT("\033[0;100m"),	// BLACK
	RED_BACKGROUND_BRIGHT("\033[0;101m"),	// RED
	GREEN_BACKGROUND_BRIGHT("\033[0;102m"),	// GREEN
	YELLOW_BACKGROUND_BRIGHT("\033[0;103m"),// YELLOW
	BLUE_BACKGROUND_BRIGHT("\033[0;104m"),	// BLUE
	PURPLE_BACKGROUND_BRIGHT("\033[0;105m"),// PURPLE
	CYAN_BACKGROUND_BRIGHT("\033[0;106m"),  // CYAN
	WHITE_BACKGROUND_BRIGHT("\033[0;107m"); // WHITE

	// Reset
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