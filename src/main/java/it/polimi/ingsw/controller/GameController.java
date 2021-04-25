package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.client2server.InteractionWithMarket;
import it.polimi.ingsw.network.messages.game.client2server.LeaderSelection;
import it.polimi.ingsw.network.messages.game.client2server.ResourcesList;
import it.polimi.ingsw.network.messages.login.LobbyAccess;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class GameController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	private Lobby lobby;
	private ArrayList<String> nicknameList;
	
	public GameController(Lobby lobby) {
		this.lobby = lobby;
	}

	public void setVirtualViews(HashMap<String, VirtualView> virtualViewMap) {
		mechanics = new GameMechanicsMultiPlayer(this, virtualViewMap.size());
		this.virtualViewMap = virtualViewMap;
		nicknameList= new ArrayList<>(virtualViewMap.keySet());
		setGameState(GameState.LOGIN);
	}
	
	public void setGameConfig(String path) {
		if (path.equalsIgnoreCase("standard")) {
			String fileName = "game_configuration_complete.xml";
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			path = file.getAbsolutePath();
		}
		readGameConfig(path);
	}
	
	public void readGameConfig(String fullPath) {
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devCards, leaderCards, baseProduction, tiles, report);
		Collections.shuffle(nicknameList);

	}

	private void initState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST-> initialResourcesHandler((ResourcesList) receivedMessage);
			case LEADER_SELECTION -> leaderSelectionHandler((LeaderSelection) receivedMessage);
		}
	}

	private void initialResourcesHandler(ResourcesList message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.assignInitialAdvantage(message.getResourcesList(),playerIndex);
		//TODO: send updated Depot or just a string
	}

	private void leaderSelectionHandler(LeaderSelection message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.getPlayer(playerIndex).chooseTwoLeaders(message.getLeaderSelection().get(0),message.getLeaderSelection().get(1));
		//TODO: send update cards list
	}

	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case INTERACTION_WITH_MARKET:
			case BUY_CARD:
			default:
		}
	}

	private void marketInteractionHandler(InteractionWithMarket message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		if(message.getWhich()=="col")
			mechanics.getMarket().shiftCol(message.getWhere());
		else mechanics.getMarket().shiftRow(message.getWhere());
	}

	private void loginState(Message receivedMessage){

		}

	private void loginHandler(LobbyAccess message){
		message.getNickname();
	}

	private void endGameState(Message receivedMessage) {

	}
	
	/**
	 * called from the lobby when the game must be halted since a player left the game
	 */
	public void haltGame() {
	
	}

	public void onMessageReceived(Message receivedMessage){
		VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
		switch (gameState){
			case LOGIN -> loginState(receivedMessage);
			case INIT -> initState(receivedMessage);
			case GAME -> inGameState(receivedMessage);
			case ENDGAME-> endGameState(receivedMessage);
			default -> throw new IllegalStateException("Unexpected value: " + gameState);
		}
	}
	

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
