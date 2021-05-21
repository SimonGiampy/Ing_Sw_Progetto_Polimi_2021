package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.gui.GUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerTabs extends ViewObservable implements SceneController{

	@FXML TabPane tabPane;
	@FXML Tab commonBoardTab;
	@FXML AnchorPane commonPane;
	private HashMap <String, Board> playersMap;
	private HashMap <Board, Tab> tabMap;
	private CommonBoard commonBoard;
	private String playerNickname;

	public void instantiateTabs(ArrayList<String> nicknameList, String playerNickname) {

		this.playerNickname = playerNickname;
		Node node;
		try {
			FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/commonBoard.fxml"));
			node = loader.load();
			commonPane.getChildren().setAll(node);
			SceneController controller = loader.getController();
			commonBoard = (CommonBoard) controller;
			commonBoard.addAllObservers(observers);
			commonBoardTab.setGraphic(new Label("Common Board"));
			commonBoardTab.getGraphic().setStyle("-fx-text-fill: #337ab7;");
		} catch (IOException e) {
			e.printStackTrace();
		}
		commonBoardTab.setOnSelectionChanged(e -> {
			if(commonBoardTab.isSelected())
				commonBoardTab.getGraphic().setStyle("-fx-text-fill: #337ab7;");
		});

		playersMap = new HashMap<>();
		tabMap = new HashMap<>();

		for (String s : nicknameList) {

			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();
			if(s.equals(playerNickname)) {
				tab.setGraphic(new Label("Your Board"));
			}
			else {
				tab.setGraphic(new Label(s + "'s Board"));
			}
			tab.getGraphic().setStyle("-fx-text-fill: #337ab7;");
			try {
				FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/it/polimi/ingsw/view/gui/board.fxml"));
				node = loader.load();
				pane.getChildren().setAll(node);
				SceneController controller = loader.getController();
				Board board = (Board) controller;
				board.addAllObservers(observers);
				if(!s.equals(playerNickname))
					board.setOpponentBoard();
				playersMap.put(s, board);
				tabMap.put(board, tab);
			} catch (IOException e) {
				e.printStackTrace();
			}
			tab.setContent(pane);
			tabPane.getTabs().add(tab);

			tab.setOnSelectionChanged(e -> {
				if(tab.isSelected())
					tab.getGraphic().setStyle("-fx-text-fill: #337ab7;");
			});
			
		}
		playersMap.get(nicknameList.get(0)).setStartingPlayer();
	}

	public void update(String nickname, ReducedFaithTrack track){
		Board board = playersMap.get(nickname);
		board.updateCrossCoords(track.getCurrentPosition());
		setTabToRead(board);
	}

	public void update(String nickname, ReducedWarehouseDepot depot){
		Board board = playersMap.get(nickname);
		board.setDepot(depot);
		setTabToRead(board);
	}

	public void update(String nickname, ReducedStrongbox strongbox){
		Board board = playersMap.get(nickname);
		board.setStrongbox(strongbox);
		setTabToRead(board);
	}

	public void update(String nickname, ArrayList<ReducedLeaderCard> leaderCards){
		Board board = playersMap.get(nickname);
		if(nickname.equals(playerNickname)) {
			board.setMyLeaderCards(leaderCards);
		}
		else{
			board.setOthersLeaderCards(leaderCards);
		}
		setTabToRead(board);
	}

	public void update(String nickname, ReducedCardProductionManagement cardProductionManagement){
		Board board = playersMap.get(nickname);
		board.setCardManager(cardProductionManagement);
		setTabToRead(board);
	}

	public void update(String nickname, boolean value){
		Board board = playersMap.get(nickname);
		System.out.println(nickname);
		board.setActProductions(value);
		setTabToRead(board);
	}

	public void update(boolean value){
		commonBoard.setButtonVisible(value);
	}

	public void updateActions(ArrayList<PlayerActions> actions){
		commonBoard.setButtonVisible(actions);
	}

	public void update(ArrayList<DevelopmentCard> cards){
		commonBoard.setGreen(cards);
	}

	public void update(ReducedDevelopmentCardsDeck deck){
		commonBoard.setDeck(deck);
		commonBoardTab.getGraphic().setStyle("-fx-text-fill: #d43f3a;");
	}

	public void update(ReducedMarket market){
		commonBoard.setMarket(market);
		commonBoardTab.getGraphic().setStyle("-fx-text-fill: #d43f3a;");
	}

	public void setTabToRead(Board board){
		Tab tab = tabMap.get(board);
		tab.getGraphic().setStyle("-fx-text-fill: #d43f3a;");
	}

	public void testSwitch(){
		//code for switch focus
		tabPane.getSelectionModel().select(playersMap.size());
	}

	@FXML public void initialize(){

	}
}
