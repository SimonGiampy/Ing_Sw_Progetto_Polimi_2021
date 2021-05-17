package it.polimi.ingsw.view.gui;

public enum MarblesGui {

	WHITE("/it/polimi/ingsw/resources/assets/board/White_Marble.png"),
	RED("/it/polimi/ingsw/resources/assets/board/Red_Marble.png"),
	PURPLE("/it/polimi/ingsw/resources/assets/board/Purple_Marble.png"),
	BLUE("/it/polimi/ingsw/resources/assets/board/Blue_Marble.png"),
	GREY("/it/polimi/ingsw/resources/assets/board/Grey_Marble.png"),
	YELLOW("/it/polimi/ingsw/resources/assets/board/Yellow_Marble.png");


	public final String path;

	MarblesGui(String path){
		this.path=path;
	}


}
