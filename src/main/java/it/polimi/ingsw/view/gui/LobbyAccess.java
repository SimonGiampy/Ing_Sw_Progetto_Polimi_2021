package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
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

	
	private int idVersion;


	

	public void onCreateLobby(Event event){
		new Thread(() -> notifyObserver(obs -> obs.onUpdateLobbyAccess(0,idVersion))).start();


		/*
		Stage stage= new Stage();
		FXMLLoader fxmlLoader= new FXMLLoader();
		try {
			Pane root= fxmlLoader.load(getClass().getResource("/fxml/numberOfPlayers.fxml").openStream());
			stage.setScene(new Scene(root,720,1080));
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

		 */
		//fieldNumPlayers.setVisible(true);

	}

	public void onJoinLobby(Event event){

	}

	public void onConfirmNum(Event event){
		//fieldNumPlayers.g
		new Thread(() -> notifyObserver(obs -> obs.onUpdatePlayersNumber(1))).start();

	}

	public void onConfirmNick(Event event){

	}

	public void onConfirmConfig(Event event){

	}

	public void update(ArrayList<String> lobbyList, int idVersion){
		ObservableList<String> lobbies = FXCollections.observableList(lobbyList);
		this.lobbyList.setItems(lobbies);
		this.idVersion=idVersion;

	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		btnCreateLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onCreateLobby);
		btnJoinLobby.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onJoinLobby);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNum);
		btnConfirmNum.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmNick);
		btnConfirmConfig.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfirmConfig);
		
	}
}
