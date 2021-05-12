package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.awt.*;

public class Nickname extends ViewObservable implements SceneController {

	@FXML public Button confirm;
	@FXML public TextField nickname;
	@FXML public ImageView nicknameValid;
	@FXML public Button button;

	public void onMouseClicked(){
		String nick=nickname.getText();
		new Thread(() -> notifyObserver(obs -> obs.onUpdateNickname(nick))).start();
	}

	@FXML public void initialize(){

	}
}
