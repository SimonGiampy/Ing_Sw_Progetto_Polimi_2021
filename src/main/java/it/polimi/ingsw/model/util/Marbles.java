package it.polimi.ingsw.model.util;

public enum Marbles {
	
	WHITE("\u001B[37m","/assets/board/White_Marble.png"), //white color
	RED("\u001B[31m","/assets/board/Red_Marble.png"), //red color
	PURPLE("\u001B[35m","/assets/board/Purple_Marble.png"), //purple color
	BLUE("\u001B[34m","/assets/board/Blue_Marble.png"), //blue color
	GREY("\033[0;30m","/assets/board/Grey_Marble.png"), //black color
	YELLOW("\u001B[33m","/assets/board/Yellow_Marble.png"); //yellow color

	
	public final String colorCode;
	public final String path;
	
	
	
	Marbles(String colorCode, String path) {
		this.colorCode = colorCode;
		this.path=path;
	}
}
