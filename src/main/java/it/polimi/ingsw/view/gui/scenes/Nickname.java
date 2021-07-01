package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.OfflineController;
import it.polimi.ingsw.controller.ServerSideController;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.OfflineVirtualView;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.util.HashMap;

public class Nickname extends ViewObservable implements SceneController {

	@FXML private Button confirm;
	@FXML private TextField nickname;
	@FXML private ImageView nicknameValid;
	
	private GUI gui;
	private boolean offline = false;

	/**
	 * Handles the click on the confirmation button. Its behavior is differentiated based on the game mode (offline or online)
	 */
	public void confirmation() {
		String nick = nickname.getText();
		if (!offline) {
			if (!nick.equals("")) notifyObserver(obs -> obs.onUpdateNickname(nick));
			else nicknameValid.setVisible(true);
		} else {
			ServerSideController serverSideController = new ServerSideController();
			OfflineVirtualView view = new OfflineVirtualView(gui);
			
			HashMap<String, VirtualView> map = new HashMap<>();
			map.put(nick, view);
			
			gui.setPlayerNickname(nick);
			
			OfflineController controller = new OfflineController(gui, serverSideController, nick);
			gui.attach(controller);
			this.attach(controller);
			serverSideController.setVirtualViews(map);
		}
		
	}

	@FXML
	public void initialize(){
		nickname.setOnKeyPressed(keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				confirmation();
			}
		});
		confirm.setOnMouseClicked(event -> confirmation());
	}

	/**
	 * it sets visible nickname invalid imageView
	 */
	public void setInvalid() {
		nicknameValid.setVisible(true);
	}

	/**
	 * getter for nickname text
	 * @return the nickname chosen by the player
	 */
	public String getNickname() {
		return nickname.getText();
	}
	
	/**
	 * needed for having an instance of the GUI in this class, for the offline mode
	 * @param gui gui
	 */
	public void setGui(GUI gui) {
		this.gui = gui;
		this.offline = true;
	}
}
