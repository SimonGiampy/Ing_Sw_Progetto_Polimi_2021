package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LobbyAccess extends ViewObservable {

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

	public ArrayList<String> lobbies;


	@FXML
	public void initialize(){
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNum);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNick);
		btnConfirmConfig.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmConfig);
		ObservableList<String> list = FXCollections.observableArrayList(lobbies);
		this.lobbyList.setItems(list);
	}

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
		lobbies = lobbyList;
	}
}
