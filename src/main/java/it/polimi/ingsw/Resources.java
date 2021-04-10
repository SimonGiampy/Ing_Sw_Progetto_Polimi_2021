package it.polimi.ingsw;

public enum Resources {

	COIN("\uD83D\uDCB0"), // 💰
	SERVANT("\uD83D\uDE47"), // 🙇
	SHIELD("\uD83D\uDEE1"), // 🛡
	STONE("\uD83D\uDDFF"), // 🗿
	EMPTY("  "); // indicates an empty slot resources in the warehouse depot
	
	public static final String VICTORY_POINTS = "\uD83C\uDFC6"; // 🏆
	
	private final String unicode;
	
	Resources(String code) {
		this.unicode = code;
	}
	
	
	@Override
	public String toString() {
		return this.unicode;
	}
}
