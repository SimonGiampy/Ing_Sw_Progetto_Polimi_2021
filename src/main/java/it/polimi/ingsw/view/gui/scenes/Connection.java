package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.observers.ViewObservable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashMap;

public class Connection extends ViewObservable implements SceneController {

	@FXML public Label addressWrong;
	@FXML public Label portWrong;
	@FXML Button connect;
	@FXML TextField address;
	@FXML TextField port;
	
	@FXML
	public void initialize() {
		EventHandler<KeyEvent> event = keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				connection();
			}
		};
		address.setOnKeyPressed(event);
		port.setOnKeyPressed(event);
		connect.setOnMouseClicked(e -> connection());
	}
	
	/**
	 * triggers connection to the server. Adds default values if not written. Default values = localhost, 25000
	 */
	void connection() {
		HashMap<String, String> serverInfo = new HashMap<>();
		
		String address = this.address.getText();
		String port = this.port.getText();
		
		
		if (address.equals("") || address.equals("localhost")) {
			serverInfo.put("address", "localhost");
			addressWrong.setVisible(false);
		} else {
			boolean isValidIpAddress = ClientSideController.isValidIpAddress(address);
			if (isValidIpAddress) {
				serverInfo.put("address", address);
				addressWrong.setVisible(false);
			} else {
				addressWrong.setVisible(true);
			}
		}
		if (ClientSideController.isValidPort(port)) {
			serverInfo.put("port", port);
			portWrong.setVisible(false);
		} else if (port.equals("")) {
			serverInfo.put("port", "25000");
			portWrong.setVisible(false);
		} else
			portWrong.setVisible(true);
		
		notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
	}
}
