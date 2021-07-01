package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class LobbyAccess extends ViewObservable implements SceneController {

	@FXML private Button btnCreateLobby;
	@FXML private ListView<String> lobbyList;
	@FXML private Button btnJoinLobby;
	@FXML private Label lobbyInvalid;
	
	private int idVersion;
	private int selectedLobby;
	
	/**
	 * the user creates a lobby
	 * @param event click event
	 */
	public void onCreateLobby(Event event) {
		notifyObserver(obs -> obs.onUpdateLobbyAccess(0,idVersion));
	}
	
	/**
	 * the user joins a lobby
	 * @param event click event
	 */
	public void onJoinLobby(Event event){
		notifyObserver(obs -> obs.onUpdateLobbyAccess(selectedLobby, idVersion));
	}
	
	/**
	 * the user selects a lobby from the given list
	 * @param event click event
	 */
	public void onSelectLobby(Event event){
		btnJoinLobby.setVisible(true);
		selectedLobby = lobbyList.getSelectionModel().getSelectedIndex() + 1;
	}
	
	/**
	 * updates the list of lobbies available on screen
	 * @param lobbyList list of lobby descriptions
	 * @param idVersion version of the list incoming from the server
	 */
	public void update(ArrayList<String> lobbyList, int idVersion){
		ObservableList<String> lobbies = FXCollections.observableList(lobbyList);
		this.lobbyList.setItems(lobbies);
		this.idVersion=idVersion;
		btnJoinLobby.setVisible(false);
		if (lobbyList.size() > 0) {
			this.lobbyList.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectLobby);
		}
	}
	
	/**
	 * sets the lobby flag not valid
	 */
	public void setLobbyInvalid() {
		lobbyInvalid.setVisible(true);
	}
	
	/**
	 * button click listeners
	 */
	@FXML
	public void initialize() {
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		Font.loadFont(getClass().getResourceAsStream("/assets/font/Caveat-Regular.ttf"), 10);
	}
}
