package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;


public class Nickname extends ViewObservable implements SceneController {

	@FXML private Button confirm;
	@FXML private TextField nickname;
	@FXML private ImageView nicknameValid;

	public void confirmation(){
		String nick = nickname.getText();
		if (!nick.equals("")) notifyObserver(obs -> obs.onUpdateNickname(nick));
		else nicknameValid.setVisible(true);
	}

	@FXML public void initialize(){
		nickname.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				confirmation();
			}
		});
		confirm.setOnMouseClicked(event -> confirmation());
	}
	
	public void setInvalid() {
		nicknameValid.setVisible(true);
	}

	public String getNickname() {
		return nickname.getText();
	}
}
