package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyAccess extends ViewObservable implements Initializable, SceneController {

	@FXML public Button btnCreateLobby;
	@FXML public ListView<String> lobbyList;
	@FXML public Button btnJoinLobby;
	@FXML public TextField fieldNickname;
	@FXML public Label lblNumPlayers;
	@FXML public TextField fieldNumPlayers;
	@FXML public Label lblNickname;
	@FXML public TextField fieldConfig;
	@FXML public Button btnConfirmNum;
	@FXML public Button btnConfirmNick;
	@FXML public Button btnConfirmConfig;

	public List<String> lobbiesDescription;


	

	public void onCreateLobby(Event event){

	}

	public void onJoinLobby(Event event){

	}

	public void onConfirmNum(Event event){

	}

	public void onConfirmNick(Event event){

	}

	public void onConfirmConfig(Event event){

	}

	public void update(ArrayList<String> lobbyList){
		ObservableList<String> lobbies = FXCollections.observableList(lobbyList);
		this.lobbyList.setItems(lobbies);

	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNum);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNick);
		btnConfirmConfig.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmConfig);
		/*lobbyList.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				ObservableList<String> strings = FXCollections.observableList(lobbiesDescription);
				lobbyList.setItems(strings);
			}
		});

		 */
		
		// this two lines crash the program for some reason, when put in the initialize method
		//ObservableList<String> list = FXCollections.observableList(lobbies);
		//lobbyList.setItems(list);
		
	}
}
