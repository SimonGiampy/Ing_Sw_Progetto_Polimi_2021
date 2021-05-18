package it.polimi.ingsw.view.gui;

public enum ResourcesGui {

	COIN("/it/polimi/ingsw/resources/assets/board/Coin.png"),
	SERVANT("/it/polimi/ingsw/resources/assets/board/Servant.png"),
	SHIELD("/it/polimi/ingsw/resources/assets/board/Shield.png"),
	STONE("/it/polimi/ingsw/resources/assets/board/Stone.png");

	public final String path;

	ResourcesGui(String path){
		this.path=path;
	}
}
