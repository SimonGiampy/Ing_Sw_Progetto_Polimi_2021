package it.polimi.ingsw.view.gui.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerTabs {

	@FXML TabPane tabPane;
	@FXML Tab commonBoard;
	@FXML Tab playerBoard;
	@FXML AnchorPane playerPane;
	@FXML AnchorPane commonPane;
	HashMap<Tab, AnchorPane> playersMap;

	public PlayerTabs(ArrayList<String> nicknameList, int numberOfPlayers){

		playersMap = new HashMap<>();
		playersMap.put(playerBoard, playerPane);

		for(int i = 1; i < numberOfPlayers; i++){

			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();

			pane.setMinHeight(0.0);
			pane.setMinWidth(0.0);
			pane.setPrefHeight(1920.0);
			pane.setPrefWidth(1050.0);

			tab.setClosable(false);
			tab.setText(nicknameList.get(i) + "'s Board");
			tab.setContent(pane);
			playersMap.put(tab, pane);
		}

	}

	public void initialize(){

	}
}
