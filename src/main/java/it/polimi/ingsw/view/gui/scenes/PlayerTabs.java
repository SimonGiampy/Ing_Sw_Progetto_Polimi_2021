package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.network.messages.game.server2client.FaithTrackShow;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PlayerTabs extends ViewObservable implements SceneController{

	@FXML TabPane tabPane;
	@FXML  Tab commonBoardTab;
	@FXML AnchorPane commonPane;
	private HashMap <String, Board> playersMap;
	public CommonBoard commonBoard;

	public void instantiateTabs(ArrayList<String> nicknameList) {

		Node node;
		try {
			FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/commonBoard.fxml"));
			node = loader.load();
			commonPane.getChildren().setAll(node);
			SceneController controller = loader.getController();
			commonBoard = (CommonBoard) controller;
			commonBoard.addAllObservers(observers);
		} catch (IOException e) {
			e.printStackTrace();
		}

		playersMap = new HashMap<>();

		for (String s : nicknameList) {

			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();
			tab.setText(s + "'s Board");
			try {
				FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/board.fxml"));
				node = loader.load();
				pane.getChildren().setAll(node);
				SceneController controller = loader.getController();
				Board board = (Board) controller;
				board.addAllObservers(observers);
				playersMap.put(s, board);
			} catch (IOException e) {
				e.printStackTrace();
			}
			tab.setContent(pane);
			tabPane.getTabs().add(tab);

			//test
			tab.setOnSelectionChanged(e -> {
				if(tab.isSelected())
					tab.setStyle("-fx-text-base-color: green;");
			});
		}
	}

	public void update(String nickname, ReducedFaithTrack track){
		Board board = playersMap.get(nickname);
		board.updateCrossCoords(track.getCurrentPosition());
	}

	public void update(String nickname, ReducedWarehouseDepot depot){
		Board board = playersMap.get(nickname);
		board.setDepot(depot);
	}

	public void update(String nickname, ReducedStrongbox strongbox){
		Board board = playersMap.get(nickname);
		board.setStrongbox(strongbox);
	}

	public void update(String nickname, ArrayList<ReducedLeaderCard> leaderCards){
		Board board = playersMap.get(nickname);
		board.setLeaderCards(leaderCards);
	}

	public void update(String nickname, ReducedCardProductionManagement cardProductionManagement){
		Board board = playersMap.get(nickname);
		board.setCardManager(cardProductionManagement);
	}

	public void update(ReducedDevelopmentCardsDeck deck){
		commonBoard.setDeck(deck);
	}

	public void update(ReducedMarket market){
		commonBoard.setMarket(market);
	}

	public void testSwitch(){
		//code for switch focus
		tabPane.getSelectionModel().select(playersMap.size());
	}

	@FXML public void initialize(){

	}
}
