package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.gui.GUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.HashMap;

public class Connection extends ViewObservable implements SceneController {

	@FXML public Label addressWrong;
	@FXML public Label portWrong;
	@FXML Button connect;
	@FXML TextField address;
	@FXML TextField port;
	@FXML Button offline;

	private GUI gui;

	@FXML
	public void initialize() {
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 10);
		EventHandler<KeyEvent> event = keyEvent -> {
			if (keyEvent.getCode() == KeyCode.ENTER) {
				connection();
			}
		};
		address.setOnKeyPressed(event);
		port.setOnKeyPressed(event);
		connect.setOnMouseClicked(e -> connection());
		offline.setOnMouseClicked(this::offline);
	}
	
	/**
	 * triggers connection to the server. Adds default values if not written. Default values = localhost, 25000
	 */
	void connection() {
		
		
		
		HashMap<String, String> serverInfo = new HashMap<>();
		
		String address = this.address.getText();
		String port = this.port.getText();
		boolean validInput;
		
		if (address.equals("") || address.equals("localhost")) {
			serverInfo.put("address", "localhost");
			addressWrong.setVisible(false);
			validInput = true;
		} else if (ClientSideController.isValidIpAddress(address)) {
			serverInfo.put("address", address);
			addressWrong.setVisible(false);
			validInput = true;
		} else {
			addressWrong.setVisible(true);
			validInput = false;
		}
		
		if (ClientSideController.isValidPort(port)) {
			serverInfo.put("port", port);
			portWrong.setVisible(false);
		} else if (port.equals("")) {
			serverInfo.put("port", "25000");
			portWrong.setVisible(false);
		} else {
			portWrong.setVisible(true);
			validInput = false;
		}
		
		if (!validInput) {
			this.address.setText("");
			this.port.setText("");
		} else {
			ClientSideController clientController = new ClientSideController(gui);
			gui.attach(clientController);
			this.attach(clientController);
			notifyObserver(obs -> obs.onUpdateServerInfo(serverInfo));
		}
		
	}
	
	/**
	 * when the button for creating an offline match is clicked
	 * @param event click event
	 */
	public void offline(MouseEvent event) {
		FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource("nickname.fxml"));
		try {
			((Node) event.getSource()).getScene().setRoot(fxmlLoader.load());
		} catch (IOException e) {
			Thread.currentThread().interrupt();
		}

		Nickname nick = fxmlLoader.getController();
		nick.setGui(gui);
	}
	
	/**
	 * setter for the gui needed for the Nickname class
	 * @param gui GUI
	 */
	public void setGui(GUI gui) {
		this.gui = gui;
	}
}
