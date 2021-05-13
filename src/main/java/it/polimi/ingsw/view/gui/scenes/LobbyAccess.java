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

import java.util.ArrayList;

public class LobbyAccess extends ViewObservable implements SceneController {

	@FXML private Button btnCreateLobby;
	@FXML private ListView<String> lobbyList;
	@FXML private Button btnJoinLobby;
	@FXML private Label lobbyInvalid;
	
	private int idVersion;
	private int selectedLobby;
	

	public void onCreateLobby(Event event){
		//new Thread(() -> notifyObserver(obs -> obs.onUpdateLobbyAccess(0,idVersion))).start();
		notifyObserver(obs -> obs.onUpdateLobbyAccess(0,idVersion));
	}

	public void onJoinLobby(Event event){
		//new Thread(() -> notifyObserver(obs -> obs.onUpdateLobbyAccess(selectedLobby, idVersion))).start();
		notifyObserver(obs -> obs.onUpdateLobbyAccess(selectedLobby, idVersion));
	}

	public void onSelectLobby(Event event){
		btnJoinLobby.setVisible(true);
		selectedLobby = lobbyList.getSelectionModel().getSelectedIndex() + 1;
	}

	public void update(ArrayList<String> lobbyList, int idVersion){
		ObservableList<String> lobbies = FXCollections.observableList(lobbyList);
		this.lobbyList.setItems(lobbies);
		this.idVersion=idVersion;
		btnJoinLobby.setVisible(false);
		if (lobbyList.size() > 0) {
			this.lobbyList.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectLobby);
		}
	}
	
	public void setLobbyInvalid() {
		lobbyInvalid.setVisible(true);
	}
	
	@FXML
	public void initialize() {
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		//lobbyList.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectLobby);
	}
}
