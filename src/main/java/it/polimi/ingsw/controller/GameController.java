package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.*;

public class GameController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	
	//TODO: new approach: instantiate this class before the rest and use it to handle clients login
	public GameController() {
	
	}

	public GameController(HashMap<String, VirtualView> virtualViewMap) {
		mechanics = new GameMechanicsMultiPlayer(this, virtualViewMap.size());
		readGameConfig("game_configuration_complete.xml");
		this.virtualViewMap = virtualViewMap;
		
		setGameState(GameState.INIT);
	}
	
	public void readGameConfig(String gameConfiguration) {
		String fileName = gameConfiguration;
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
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
				//mechanics.assignInitialAdvantage();
			} break;

			default:
		}
	}

	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case INTERACTION_WITH_MARKET:
			case BUY_CARD:
			default:
		}
	}

	public void onMessageReceived(Message receivedMessage){
		VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
		switch (gameState){
			case INIT:
		}
	}
	

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
