package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;

import javafx.scene.image.ImageView;

public class NumberOfPlayers extends ViewObservable implements SceneController {

	@FXML public ImageView onePlayerGlow;
	@FXML public ImageView onePlayerNormal;
	@FXML public ImageView twoPlayersNormal;
	@FXML public ImageView twoPlayersGlow;
	@FXML public ImageView threePlayersNormal;
	@FXML public ImageView threePlayersGlow;
	@FXML public ImageView fourPlayersNormal;
	@FXML public ImageView fourPlayersGlow;

	public void onMouseClickedOne(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(1))).start();
	}

	public void onMouseClickedTwo(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(2))).start();
	}

	public void onMouseClickedThree(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(3))).start();
	}

	public void onMouseClickedOFour(){
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(4))).start();
	}

	public void onMouseEnteredOne(){
		//onePlayerNormal.setVisible(false);
		onePlayerGlow.setVisible(true);

	}

	public void onMouseExitedOne(){
		onePlayerGlow.setVisible(false);
		//onePlayerNormal.setVisible(true);
	}

	public void onMouseEnteredTwo(){
		twoPlayersGlow.setVisible(true);
	}

	public void onMouseExitedTwo(){
		twoPlayersGlow.setVisible(false);
		//onePlayerNormal.setVisible(true);
	}

	public void onMouseEnteredThree(){
		threePlayersGlow.setVisible(true);
	}

	public void onMouseExitedThree(){
		threePlayersGlow.setVisible(false);
		//onePlayerNormal.setVisible(true);
	}

	public void onMouseEnteredFour(){
		fourPlayersGlow.setVisible(true);
	}

	public void onMouseExitedFour(){
		fourPlayersGlow.setVisible(false);
		//onePlayerNormal.setVisible(true);
	}

	@FXML public void initialize(){

	}


}
