package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.observers.ViewObservable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PlayerTabs extends ViewObservable implements SceneController{

	@FXML TabPane tabPane;
	@FXML Tab commonBoard;
	@FXML AnchorPane commonPane;
	HashMap<Tab, AnchorPane> playersMap;

	public void instantiateTabs(ArrayList<String> nicknameList) {

		Node node;
		try {
			node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/it/polimi/ingsw/view/gui/commonBoard.fxml")));
			commonPane.getChildren().setAll(node);
		} catch (IOException e) {
			e.printStackTrace();
		}

		playersMap = new HashMap<>();

		//commonPane.getChildren().setAll( (Node) FXMLLoader.load(Objects.requireNonNull(getClass()
		//		.getResource("it/polimi/ingsw/view/gui/commonBoard.fxml"))));
		/*
		try {
			commonBoard.setContent(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("commonBoard.fxml"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		 */


		for (String s : nicknameList) {

			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();//(AnchorPane) FXMLLoader.load("personalBoard.fxml");
			tab.setText(s + "'s Board");
			tab.setContent(pane);
			playersMap.put(tab, pane);
			tabPane.getTabs().add(tab);

		}
	}

	@FXML public void initialize(){

	}
}
