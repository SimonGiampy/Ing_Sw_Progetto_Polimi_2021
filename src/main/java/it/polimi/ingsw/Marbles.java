package it.polimi.ingsw;

public enum Marbles {
	
	WHITE("\u001B[37m"), //white color
	RED("\u001B[31m"), //red color
	VIOLET("\u001B[35m"), //purple color
	BLUE("\u001B[34m"), //blue color
	GREY("\033[0;30m"), //black color
	YELLOW("\u001B[33m"); //yellow color
	
	public final String colorCode;
	
	private String resetColor = "\033[0m";
	
	Marbles(String colorCode) {
		this.colorCode = colorCode;
	}
}
