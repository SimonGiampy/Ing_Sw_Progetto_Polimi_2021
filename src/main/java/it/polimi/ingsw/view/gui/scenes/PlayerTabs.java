package it.polimi.ingsw.view.gui.scenes;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.TokenType;
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
	private HashMap <String, Board> playersMap; // maps every player to its board
	private HashMap <Board, Tab> tabMap; //maps every player board on the screen to its corresponding tab
	private CommonBoard commonBoard; // shared board among the players
	private String playerNickname;
	
	private static final String hexa_blue_color = "-fx-text-fill: #337ab7;";
	private static final String hexa_red_color = "-fx-text-fill: #d43f3a;";

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
			commonBoardTab.getGraphic().setStyle(hexa_blue_color);
		} catch (IOException e) {
			e.printStackTrace();
		}
		commonBoardTab.setOnSelectionChanged(e -> {
			if(commonBoardTab.isSelected())
				commonBoardTab.getGraphic().setStyle(hexa_blue_color);
		});

		playersMap = new HashMap<>();
		tabMap = new HashMap<>();

		for (String s : nicknameList) {

			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();
			if(s.equals(playerNickname)) {
				tab.setGraphic(new Label("Your Board"));
			} else {
				tab.setGraphic(new Label(s + "'s Board"));
			}
			tab.getGraphic().setStyle(hexa_blue_color);
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
					tab.getGraphic().setStyle(hexa_blue_color);
			});
		}
		playersMap.get(nicknameList.get(0)).setStartingPlayer();
		//tabPane.getSelectionModel().select(tabMap.get(playersMap.get(playerNickname)));
		
		commonBoard.setPlayerBoard(playersMap.get(playerNickname));
		
		//sets the single player mode
		if (nicknameList.size() == 1) {
			playersMap.get(nicknameList.get(0)).setSinglePlayerMode();
		}
	}

	public void update(String nickname){
		Board board = playersMap.get(nickname);
		//board.setAvailableLeaderActions();
	}

	public void update(String nickname, ReducedFaithTrack track){
		Board board = playersMap.get(nickname);
		board.updateCrossCoords(track.isLorenzosTrack(), track.getCurrentPosition());
		setTabToRead(board);
	}

	public void update(String nickname, ReducedWarehouseDepot depot){
		Board board = playersMap.get(nickname);
		board.updateDepots(depot);
		setTabToRead(board);
	}

	public void update(String nickname, ReducedStrongbox strongbox){
		Board board = playersMap.get(nickname);
		board.updateStrongbox(strongbox);
		setTabToRead(board);
	}

	public void update(String nickname, ArrayList<ReducedLeaderCard> leaderCards){
		Board board = playersMap.get(nickname);
		if(nickname.equals(playerNickname)) {
			board.setMyLeaderCards(leaderCards);
			if (board.isMainActionDone() && !commonBoard.leaderCardsDone) {
				commonBoard.setEndTurnVisible(true);
				board.resetMainActionDone();
			}
			if((leaderCards.get(0).isDiscarded() || leaderCards.get(0).isAbilitiesActivated()) &&
					(leaderCards.get(1).isDiscarded() || leaderCards.get(1).isAbilitiesActivated())){
				commonBoard.leaderCardsDone=true;
				commonBoard.setEndTurnVisible(false);
			}

		} else {
			board.setOthersLeaderCards(leaderCards);
		}
		setTabToRead(board);
	}

	/*
	public void disableCommonBoardButtons(){
		commonBoard.setEndTurnVisible();
	}
	*/
	public void update(String nickname, ReducedCardProductionManagement cardProductionManagement){
		Board board = playersMap.get(nickname);
		board.updateDevCardsSlots(cardProductionManagement);
		setTabToRead(board);
	}

	public void update(String nickname, boolean activateProductions){
		Board board = playersMap.get(nickname);
		board.setActProductions(activateProductions);
		setTabToRead(board);
	}

	public void disableCommonBoardButtons() {
		commonBoard.disableButtons();
	}

	public void updateActions(String nickname, ArrayList<PlayerActions> actions){
		commonBoard.setButtonVisible(actions);
		Board board = playersMap.get(nickname);
		if(actions.contains(PlayerActions.PRODUCTIONS))
			board.setActProductions(true);
		setTabToRead(board);

	}

	public void update(TokenType type, Colors color){
		Board board = playersMap.get(playerNickname);
		board.updateToken(type,color);
	}

	public void updateProduction(String nickname, ArrayList<Productions> productions){
		Board board = playersMap.get(nickname);
		board.showAvailableProductions(productions);
		board.setProducingState(true);
	}

	public void update(ArrayList<DevelopmentCard> cards, boolean wrong){
		commonBoard.setGreen(cards,wrong);
	}

	public void update(ReducedDevelopmentCardsDeck deck){
		commonBoard.setDeck(deck);
		if(!commonBoardTab.isSelected())
			commonBoardTab.getGraphic().setStyle(hexa_red_color);
	}

	public void update(ReducedMarket market){
		commonBoard.setMarket(market);
		if(!commonBoardTab.isSelected())
			commonBoardTab.getGraphic().setStyle(hexa_red_color);
	}

	public void setTabToRead(Board board){
		Tab tab = tabMap.get(board);
		if(!tab.isSelected())
			tab.getGraphic().setStyle(hexa_red_color);
	}

	@FXML public void initialize(){

	}
	
	public void depotInteraction(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		Board board = playersMap.get(playerNickname);
		board.depotInteraction(depot, initialMove, confirmationAvailable, inputValid);
	}
}
