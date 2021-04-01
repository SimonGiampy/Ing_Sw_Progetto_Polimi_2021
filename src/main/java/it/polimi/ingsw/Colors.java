package it.polimi.ingsw;

public enum Colors {
	
	GREEN(0),
	BLUE(1),
	YELLOW(2),
	PURPLE(3);
	
	public final int colorNumber; // for each color it is associated a number, used in the common cards deck
	
	Colors(int colorNumber) {
		this.colorNumber = colorNumber;
	}
	
	public int getColorNumber() {
		return colorNumber;
	}
	
	/**
	 * probably useless function
	 * @param colorNumber the number
	 * @return the corresponding color
	 */
	public static Colors getColorFromNumber(int colorNumber) {
		return switch (colorNumber) {
			case 0 -> GREEN;
			case 1 -> BLUE;
			case 2 -> YELLOW;
			case 3 -> PURPLE;
			default -> throw new IllegalStateException("invalid color number");
		};
	}
}
