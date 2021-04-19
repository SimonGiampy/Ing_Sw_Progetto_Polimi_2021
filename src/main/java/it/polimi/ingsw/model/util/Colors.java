package it.polimi.ingsw.model.util;

public enum Colors {
	
	GREEN(0, Unicode.GREEN_BOLD.toString()),
	BLUE(1, Unicode.BLUE_BOLD.toString()),
	YELLOW(2, Unicode.YELLOW_BRIGHT.toString()),
	PURPLE(3, Unicode.PURPLE_BOLD.toString());
	
	public final int colorNumber; // for each color it is associated a number, used in the common cards deck
	public final String colorCode;
	
	Colors(int colorNumber, String colorCode) {
		this.colorNumber = colorNumber;
		this.colorCode= colorCode;
	}
	
	public int getColorNumber() {
		return colorNumber;
	}
	public String getColorCode(){return colorCode;}
	

}
