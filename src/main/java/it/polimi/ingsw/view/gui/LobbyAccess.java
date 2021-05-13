package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public class LobbyAccess extends ViewObservable implements SceneController {
	
	//TODO: modify this controller so that it just handles the entrance in a lobby or its creation (with confirmation labels)

	@FXML public Button btnCreateLobby;
	@FXML public ListView<String> lobbyList;
	@FXML public Button btnJoinLobby;

	
	private int idVersion;
	private int selectedLobby;
	

	public void onCreateLobby(Event event){
		new Thread(() -> notifyObserver(obs -> obs.onUpdateLobbyAccess(0,idVersion))).start();

	}

	public void onJoinLobby(Event event){
		new Thread(() -> notifyObserver(obs -> obs.onUpdateLobbyAccess(selectedLobby, idVersion))).start();
	}

	public void onSelectLobby(Event event){
		btnJoinLobby.setVisible(true);
		selectedLobby = lobbyList.getSelectionModel().getSelectedIndex();
	}

	public void update(ArrayList<String> lobbyList, int idVersion){
		lobbyList.add("merda");
		lobbyList.add("mca");
		ObservableList<String> lobbies = FXCollections.observableList(lobbyList);
		this.lobbyList.setItems(lobbies);
		this.idVersion=idVersion;

	}
	
	@FXML
	public void initialize() {
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		lobbyList.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onSelectLobby);
	}
}
