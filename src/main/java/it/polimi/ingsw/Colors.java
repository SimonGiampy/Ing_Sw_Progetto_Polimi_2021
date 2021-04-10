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
	

}
