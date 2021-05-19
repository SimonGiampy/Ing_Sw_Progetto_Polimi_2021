package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class NumberOfPlayers extends ViewObservable implements SceneController {

	@FXML public ImageView onePlayerNormal;
	@FXML public ImageView twoPlayersNormal;;
	@FXML public ImageView threePlayersNormal;
	@FXML public ImageView fourPlayersNormal;

	@FXML
	void onMouseClickedOne(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(1))).start();
	}

	public void onMouseClickedTwo(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(2))).start();
	}

	public void onMouseClickedThree(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(3))).start();
	}

	public void onMouseClickedFour(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(4))).start();
	}

	public void onMouseEnteredOne(){

		setShadow(0.5,onePlayerNormal);
	}

	public void onMouseExitedOne(){
		setShadow(0,onePlayerNormal);
	}

	public void onMouseEnteredTwo(){
		setShadow(0.5,twoPlayersNormal);
	}

	public void onMouseExitedTwo(){
		setShadow(0,twoPlayersNormal);
	}

	public void onMouseEnteredThree(){
		setShadow(0.5,threePlayersNormal);
	}

	public void onMouseExitedThree(){
		setShadow(0,threePlayersNormal);
	}

	public void onMouseEnteredFour(){
		setShadow(0.5,fourPlayersNormal);
	}

	public void onMouseExitedFour(){
		setShadow(0,fourPlayersNormal);
	}

	public void setShadow(double value, ImageView img){
		DropShadow dropShadow= new DropShadow();
		dropShadow.setSpread(value);
		dropShadow.setColor(Color.WHITE);
		img.setEffect(dropShadow);
	}

	@FXML public void initialize(){

	}


}
