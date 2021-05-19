package it.polimi.ingsw.model.util;

import java.io.Serializable;

public enum Resources implements Serializable {

	COIN(Unicode.YELLOW_BRIGHT+"\uD83D\uDCB0"+Unicode.RESET,"/assets/board/Coin.png"), // 💰
	SERVANT(Unicode.PURPLE_BOLD+"\uD83D\uDC64"+Unicode.RESET, "/assets/board/Servant.png"), // 🙇
	SHIELD(Unicode.BLUE_BOLD+"\uD83D\uDEE1"+Unicode.RESET, "/assets/board/Shield.png"), // 🛡
	STONE(Unicode.BLACK_BOLD+"\uD83D\uDDFF"+Unicode.RESET, "/assets/board/Stone.png"), // 🗿 █
	FREE_CHOICE(Unicode.BLACK_BOLD+"\u2753"+Unicode.RESET, ""),
	EMPTY("  ", ""); // indicates an empty slot resources in the warehouse depot
	
	public static final String VICTORY_POINTS = Unicode.YELLOW_BOLD+"\uD83C\uDFC6"+Unicode.RESET; // 🏆
	public final String path;

	private final String unicode;
	
	Resources(String code, String path) {
		this.unicode = code;
		this.path = path;
	}
	
	
	@Override
	public String toString() {
		return this.unicode;
	}
}
