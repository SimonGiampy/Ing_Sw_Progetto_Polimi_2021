package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


public class Nickname extends ViewObservable implements SceneController {

	@FXML public Button confirm;
	@FXML public TextField nickname;
	@FXML public ImageView nicknameValid;

	public void onMouseClicked(){
		String nick=nickname.getText();
		new Thread(() -> notifyObserver(obs -> obs.onUpdateNickname(nick))).start();
	}

	@FXML public void initialize(){

	}
}
