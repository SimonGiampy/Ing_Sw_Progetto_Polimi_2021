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
	
	private boolean leaderCardsDone;
	
	private static final String hexa_blue_color = "-fx-text-fill: #337ab7;";
	private static final String hexa_red_color = "-fx-text-fill: #d43f3a;";
	
	/**
	 * Creates the tabs on top of the interface. Sets their content based on who is the player that is playing (player that owns the board or
	 * another player in the lobby. Initializes the common board, sets the selection changes on the tabs and their colors.
	 * For every tab in the tabPane, it creates and instantiates one board, relative to a player in the game.
	 * @param nicknameList list of the players connected to the game
	 * @param playerNickname the name of the player who owns the client on which is running this game
	 */
	public void instantiateTabs(ArrayList<String> nicknameList, String playerNickname) {

		this.playerNickname = playerNickname;
		Node node;
		try {
			//common board instantiation
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
		//changes the color of the tab when the user chooses another tab
		commonBoardTab.setOnSelectionChanged(e -> {
			if(commonBoardTab.isSelected())
				commonBoardTab.getGraphic().setStyle(hexa_blue_color);
		});

		playersMap = new HashMap<>();
		tabMap = new HashMap<>();

		for (String s : nicknameList) { // for every player connected in the game
			//instantiate oen tab per player
			Tab tab = new Tab();
			AnchorPane pane = new AnchorPane();
			if(s.equals(playerNickname)) {
				tab.setGraphic(new Label("Your Board"));
			} else {
				tab.setGraphic(new Label(s + "'s Board"));
			}
			tab.getGraphic().setStyle(hexa_blue_color);
			try {
				// creates the board for every player and sets the necessary values
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
		
		playersMap.get(nicknameList.get(0)).setStartingPlayer(); // starting player gets the inkwell
		//tabPane.getSelectionModel().select(tabMap.get(playersMap.get(playerNickname)));
		
		commonBoard.setPlayerBoard(playersMap.get(playerNickname)); // refers to the player of this client app
		
		//sets the single player mode
		if (nicknameList.size() == 1) {
			playersMap.get(nicknameList.get(0)).setSinglePlayerMode();
		}
	}
	
	/**
	 * updates the faith track
	 * @param nickname of the relative player
	 * @param track from the server
	 */
	public void update(String nickname, ReducedFaithTrack track){
		Board board = playersMap.get(nickname);
		board.updateFaithTrack(track);
		setTabToRead(board);
	}
	
	/**
	 * updates the warehouse depot
	 * @param nickname of the relative player
	 * @param depot from the server
	 */
	public void update(String nickname, ReducedWarehouseDepot depot){
		Board board = playersMap.get(nickname);
		board.updateDepots(depot);
		setTabToRead(board);
	}
	
	/**
	 * updates the strongbox resources
	 * @param nickname of the relative player
	 * @param strongbox from the server
	 */
	public void update(String nickname, ReducedStrongbox strongbox){
		Board board = playersMap.get(nickname);
		board.updateStrongbox(strongbox);
		setTabToRead(board);
	}
	
	/**
	 * updates the leader cards and their actions buttons
	 * @param nickname of the relative player
	 * @param leaderCards from the server
	 */
	public void update(String nickname, ArrayList<ReducedLeaderCard> leaderCards){
		Board board = playersMap.get(nickname);
		if(nickname.equals(playerNickname)) { // this player owns the leader cards
			board.setMyLeaderCards(leaderCards);
			if (board.isMainActionDone() && !leaderCardsDone) { // turn is finished
				commonBoard.setEndTurnVisible(true);
				board.resetMainActionDone();
			}
			//sets the flags for the leader actions
			if((leaderCards.get(0).isDiscarded() || leaderCards.get(0).isAbilitiesActivated()) &&
					(leaderCards.get(1).isDiscarded() || leaderCards.get(1).isAbilitiesActivated())){
				leaderCardsDone = true;
				commonBoard.setEndTurnVisible(false);
			}

		} else {
			board.setOthersLeaderCards(leaderCards); // other players leader cards are obscured
		}
		setTabToRead(board);
	}
	
	/**
	 * updates the development cards in the player slots
	 * @param nickname of the relative player
	 * @param cardProductionManagement from the server
	 */
	public void update(String nickname, ReducedCardProductionManagement cardProductionManagement){
		Board board = playersMap.get(nickname);
		board.updateDevCardsSlots(cardProductionManagement);
		setTabToRead(board);
	}
	
	/**
	 * updates productions button interaction
	 * @param nickname of the relative player
	 * @param activateProductions indicates if the button is to be shown or not
	 */
	public void update(String nickname, boolean activateProductions){
		Board board = playersMap.get(nickname);
		board.setActProductions(activateProductions);
		setTabToRead(board);
	}
	
	/**
	 * disables the common board interaction buttons
	 */
	public void disableCommonBoardButtons() {
		commonBoard.disableButtons();
	}
	
	/**
	 * updates the visibility of the buttons for the various player actions
	 * @param nickname of the relative player
	 * @param actions that the player can do
	 */
	public void updateActions(String nickname, ArrayList<PlayerActions> actions){
		commonBoard.setButtonVisible(actions);
		Board board = playersMap.get(nickname);
		if(actions.contains(PlayerActions.PRODUCTIONS))
			board.setActProductions(true);
		setTabToRead(board);
	}
	
	/**
	 * updates the token for the single player mode
	 * @param type of token
	 * @param color of token
	 */
	public void update(TokenType type, Colors color){
		Board board = playersMap.get(playerNickname);
		board.updateToken(type,color);
	}
	
	/**
	 * updates the visibility of the shadows over the elements that can be produced
	 * @param nickname of the relative player
	 * @param productions list of productions that the player can do
	 */
	public void updateProduction(String nickname, ArrayList<Productions> productions){
		Board board = playersMap.get(nickname);
		board.showAvailableProductions(productions);
		board.setProducingState(true);
	}
	
	/**
	 * updates the shadow on the dev card in the common cards deck
	 * @param cards development cards
	 * @param wrong if the previous choice was incorrect
	 */
	public void update(ArrayList<DevelopmentCard> cards, boolean wrong){
		commonBoard.setBuyableCardsEffect(cards,wrong);
	}
	
	/**
	 * updates the dev cards deck
	 * @param deck sent from the server
	 */
	public void update(ReducedDevelopmentCardsDeck deck){
		commonBoard.setDeck(deck);
		if(!commonBoardTab.isSelected())
			commonBoardTab.getGraphic().setStyle(hexa_red_color);
	}
	
	/**
	 * updates the market and the marbles sent from the server
	 * @param market new disposition
	 */
	public void update(ReducedMarket market){
		commonBoard.setMarket(market);
		if(!commonBoardTab.isSelected())
			commonBoardTab.getGraphic().setStyle(hexa_red_color);
	}
	
	/**
	 * sets a tab to be read, by changing the color of the string in the tab
	 * @param board to be read
	 */
	public void setTabToRead(Board board){
		Tab tab = tabMap.get(board);
		if(!tab.isSelected())
			tab.getGraphic().setStyle(hexa_red_color);
	}
	
	/**
	 * handles the depot interaction, and sends the relevant data to the Board class so it completes the interaction
	 * @param depot sent from the server
	 * @param initialMove if the images and drag and drop functionality needs to be set
	 * @param confirmationAvailable if the confirmation button needs to be enabled or not
	 * @param inputValid if the previous move was valid
	 */
	public void depotInteraction(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		Board board = playersMap.get(playerNickname);
		board.depotInteraction(depot, initialMove, confirmationAvailable, inputValid);
	}
}
