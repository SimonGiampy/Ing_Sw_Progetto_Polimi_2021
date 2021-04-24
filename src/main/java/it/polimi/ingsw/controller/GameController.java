package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.ResourcesList;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GameController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	private Lobby lobby;
	
	//TODO: new approach: instantiate this class before the rest and use it to handle clients login
	public GameController(Lobby lobby) {
		this.lobby = lobby;
	}

	public void setVirtualViews(HashMap<String, VirtualView> virtualViewMap) {
		mechanics = new GameMechanicsMultiPlayer(this, virtualViewMap.size());
		this.virtualViewMap = virtualViewMap;
		
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
		
	}

	private void initState(Message receivedMessage, VirtualView virtualView){
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST: {
				initialResourcesHandler((ResourcesList) receivedMessage);
			} break;

			default:
		}
	}

	private void initialResourcesHandler(ResourcesList message){
		mechanics.assignInitialAdvantage(message.getResourcesList(),1); //TODO: need a method to select player by his nickname
	}

	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case INTERACTION_WITH_MARKET:
			case BUY_CARD:
			default:
		}
	}

	private void loginState(Message receivedMessage){

	}

	private void endGameState(Message receivedMessage){

	}

	public void onMessageReceived(Message receivedMessage){
		VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
		switch (gameState){
			case LOGIN -> loginState(receivedMessage);
			case INIT -> initState(receivedMessage,virtualView);
			case GAME -> inGameState(receivedMessage);
			case ENDGAME-> endGameState(receivedMessage);
			default -> throw new IllegalStateException("Unexpected value: " + gameState);
		}
	}
	

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
