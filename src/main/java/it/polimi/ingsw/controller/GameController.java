package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.singleplayer.Token;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class GameController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private Map<String, VirtualView> virtualViewMap;

	public GameController(String gameConfiguration){
		readInformation("game_configuration_complete.xml");
		this.virtualViewMap= new HashMap<>();
		setGameState(GameState.INIT);
	}

	private void initState(Message receivedMessage, VirtualView virtualView){
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST:
			case INPUT_SELECTION:
			default:
		}
	}

	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case INTERACTION_WITH_MARKET:
			case BUY_CARD:
			case INPUT_SELECTION:
			default:
		}
	}

	public void onMessageReceived(Message receivedMessage){
		VirtualView virtualView= virtualViewMap.get(receivedMessage.getNickname());
		switch (gameState){
			case INIT:
		}
	}



	public void readInformation(String gameConfiguration){

		String fileName= gameConfiguration;
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

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
