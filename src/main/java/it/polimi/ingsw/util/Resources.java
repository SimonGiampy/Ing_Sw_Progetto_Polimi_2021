package it.polimi.ingsw.util;

import it.polimi.ingsw.Unicode;

public enum Resources {

	COIN(Unicode.YELLOW_BRIGHT+"\uD83D\uDCB0"+Unicode.RESET), // 💰
	SERVANT(Unicode.PURPLE_BOLD+"\uD83D\uDC64"+Unicode.RESET), // 🙇
	SHIELD(Unicode.BLUE_BOLD+"\uD83D\uDEE1"+Unicode.RESET), // 🛡
	STONE(Unicode.BLACK_BOLD+"\uD83D\uDDFF"+Unicode.RESET), // 🗿 █
	FREE_CHOICE(Unicode.BLACK_BOLD+"\u2753"+Unicode.RESET),
	EMPTY("  "); // indicates an empty slot resources in the warehouse depot
	
	public static final String VICTORY_POINTS = Unicode.YELLOW_BOLD+"\uD83C\uDFC6"+Unicode.RESET; // 🏆
	
	private final String unicode;
	
	Resources(String code) {
		this.unicode = code;
	}
	
	
	@Override
	public String toString() {
		return this.unicode;
	}
}
