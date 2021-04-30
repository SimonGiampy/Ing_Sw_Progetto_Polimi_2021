package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.ReducedFaithTrack;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.network.messages.game.server2client.LeaderShow;
import it.polimi.ingsw.network.messages.login.GameConfigReply;
import it.polimi.ingsw.network.messages.login.GameConfigRequest;
import it.polimi.ingsw.network.messages.login.LobbyAccess;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ServerSideController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	private Lobby lobby;
	private ArrayList<String> nicknameList;
	private final boolean[] gameReady; //
	private final boolean[] initResources;


	private TurnController turnController;
	
	private int numberOfPlayers;
	
	public ServerSideController(Lobby lobby, int numberOfPlayers) {
		this.lobby = lobby;
		this.numberOfPlayers = numberOfPlayers;
		mechanics = new GameMechanicsMultiPlayer(this, numberOfPlayers);
		initResources= new boolean[numberOfPlayers-1];
		gameReady= new boolean[numberOfPlayers];
		gameState = GameState.CONFIG;
	}

	//TODO: fix game mechanics instantiation order
	
	/**
	 * Method called just before the game starts, when all the players are ready to play
	 * @param virtualViewMap combinations of nicknames and their virtual views
	 */
	public void setVirtualViews(HashMap<String, VirtualView> virtualViewMap) {
		this.virtualViewMap = virtualViewMap;
		nicknameList = new ArrayList<>(virtualViewMap.keySet());
		Collections.shuffle(nicknameList);
	}
	
	public void onMessageReceived(Message receivedMessage){
		switch (gameState){
			case CONFIG -> configHandler(receivedMessage);
			case INIT -> initState(receivedMessage);
			case GAME -> inGameState(receivedMessage);
			case ENDGAME -> endGameState(receivedMessage);
			default -> throw new IllegalStateException("Unexpected value: " + gameState);
		}
	}
	
	private void configHandler(Message config) {
		if (config != null && config.getMessageType() == MessageType.GAME_CONFIG_REPLY) {
			GameConfigReply gameConfigReply = (GameConfigReply) config;
			setGameConfig(gameConfigReply.getGameConfiguration());
		}
	}
	
	public void setGameConfig(String path) {
		if (path.equalsIgnoreCase("standard")) {
			String fileName = "game_configuration_complete.xml";
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource(fileName).getFile());
			path = file.getAbsolutePath();
		}
		XMLParser parser = new XMLParser(path);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devCards, leaderCards, baseProduction, tiles, report);
		
		Lobby.LOGGER.info("Game configuration has been read and applied to the lobby settings");
		
		startPreGame();
	}

	/**
	 * it checks if all values in a boolean array are true
	 * @param values is the array of boolean
	 * @return true if all values are true
	 */
	private static boolean allTrue(boolean[] values){
		for (boolean value : values) {
			if (!value)
				return false;
		}
		return true;
	}

	/**
	 * it starts the game (start a new turn)
	 */
	private void startGame(){
		gameState = GameState.GAME;
		//need a broadcast message
		turnController.newTurn();
	}

	/**
	 * it starts the pregame (ask for initial resources)
	 */
	public void startPreGame(){
		turnController = new TurnController(virtualViewMap, this, nicknameList, mechanics);
		gameState = GameState.INIT;
		lobby.matchStart();
		
		VirtualView vv = virtualViewMap.get(nicknameList.get(0));
		vv.showGenericMessage("You are the first player! Wait!");
		if(numberOfPlayers==1) //TODO: singleplayer
			initResources[0]=true;
		else {
			for (int i = 1; i < numberOfPlayers; i++) {
				VirtualView view = virtualViewMap.get(nicknameList.get(i));
				if (i == 3)
					view.askInitResources(2);
				else
					view.askInitResources(1);
			}
		}
	}

	/**
	 * it handles messages while the game state is set a INIT
	 * @param receivedMessage is the message received by the client
	 */
	private void initState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST-> initialResourcesHandler((ResourcesList) receivedMessage);
			case LEADER_SELECTION -> leaderSelectionHandler((LeaderSelection) receivedMessage);
		}
	}

	/**
	 * it handles initial Resources phase, adds incoming resources to the depot and moves the marker in the faith track (only third and fourth player).
	 * When all the players are ready,ask for leader cards selection
	 * @param message is a message from the client
	 */
	private void initialResourcesHandler(ResourcesList message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.assignInitialAdvantage(message.getResourcesList(),playerIndex);
		initResources[playerIndex-1]=true;
		VirtualView view=virtualViewMap.get(message.getNickname());
		view.showGenericMessage("This is your depot now!");
		view.showDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()));
		if(playerIndex==2 || playerIndex==3){
			view.showGenericMessage("This is your Faith Track now!");
			view.showFaithTrack(new ReducedFaithTrack(mechanics.getPlayer(playerIndex).getPlayerFaithTrack()));
		}

		if(allTrue(initResources)){
			for (int i = 0; i < numberOfPlayers; i++) {
				ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[0]));
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[1]));
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[2]));
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[3]));
				view=virtualViewMap.get(nicknameList.get(i));
				view.askInitLeaders(leaderCards);
			}
		}
	}

	/**
	 * it handles initial leader cards selection, when all players are ready starts the game
	 * @param message is a message from the client
	 */
	private void leaderSelectionHandler(LeaderSelection message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		VirtualView view= virtualViewMap.get(message.getNickname());
		mechanics.getPlayer(playerIndex).chooseTwoLeaders(message.getLeaderSelection().get(0),message.getLeaderSelection().get(1));
		ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
		view.showGenericMessage("Your Leader Cards now!");
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[0]));
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[1]));
		view.showLeaderCards(leaderCards);
		gameReady[playerIndex]=true;
		if(allTrue(gameReady))
			startGame();
		//TODO: send update cards list

	}

	/**
	 * it handles messages while the game state is set a GAME
	 * @param receivedMessage is a message from the client
	 */
	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case ACTION_REPLY-> actionReplyHandler((ActionReply) receivedMessage);
			case INTERACTION_WITH_MARKET-> marketInteractionHandler((InteractionWithMarket) receivedMessage);
			case BUY_CARD-> buyCardHandler((BuyCard) receivedMessage);
			case DEPOT_INTERACTION -> depotInteractionHandler((DepotInteraction) receivedMessage);
			case PRODUCTION_SELECTION -> productionHandler((ProductionSelection) receivedMessage);
			case LEADER_ACTION -> leaderActionHandler((LeaderAction) receivedMessage);
		}
	}

	/**
	 * it handles action selection, sends the corresponding message to the client
	 * @param message is a message from the client
	 */
	private void actionReplyHandler(ActionReply message){
		VirtualView view= virtualViewMap.get(message.getNickname());
		int playerIndex=nicknameList.indexOf(message.getNickname());
		switch (message.getSelectedAction()){
			case MARKET-> {
				view.askMarketAction(new ReducedMarket(mechanics.getMarket(),mechanics.getPlayer(playerIndex).getPlayersResourceDeck()));
			}
			case BUY_CARD-> {
				view.askBuyCardAction(new ArrayList<>
					(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherAllPlayersResources(),
							mechanics.getPlayer(playerIndex).getPlayersCardManager())));

			}
			case PRODUCTIONS -> {
				view.askProductionAction(mechanics.getPlayer(playerIndex).getPlayersCardManager().availableProduction());

			}
			case LEADER -> {
				ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[0]));
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[1]));
				view.askLeaderAction(leaderCards);

			}
		}

	}

	/**
	 * it handles market interaction
	 * @param message is a message from the client
	 */
	private void marketInteractionHandler(InteractionWithMarket message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		VirtualView view= virtualViewMap.get(message.getNickname());
		try {
			mechanics.getPlayer(playerIndex).interactWithMarket(message.getWhich(),message.getWhere(),message.getQuantity1(), message.getQuantity2());
			view.replyDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()),true,false,true);
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();

		}

		//turnController.setPhaseType(PhaseType.MAIN_ACTION); TODO: move this after depot interaction
	}

	private void depotInteractionHandler(DepotInteraction message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		VirtualView view= virtualViewMap.get(message.getNickname());
		if(message.isConfirmed()){
			//TODO: end turn and discard left resources
		}
		else {
			// TODO: depot interaction

		}
	}

	/**
	 * it handles buy card interaction
	 * @param message is a message from the client
	 */
	private void buyCardHandler(BuyCard message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		/* // to move in cli
		if (mechanics.getPlayer(playerIndex).getPlayersCardManager().checkStackLevel(message.getSlot())==message.getLevel()-1) {

			view = virtualViewMap.get(message.getNickname());
			//view.askBuyCardAction(); TODO: need a method to take only available cards to send
		}
		else
		 */
		 mechanics.getPlayer(playerIndex).buyNewDevCard(message.getLevel(),message.getColor(),message.getSlot());
		turnController.setPhaseType(PhaseType.MAIN_ACTION);
	}

	/**
	 * it handles production interaction
	 * @param message is a message from the client
	 */
	private void productionHandler(ProductionSelection message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		int[] inputResources = new int[]{0,0,0,0};
		int[] outPutResources= new int[]{0,0,0,0};
		if(message.getResourcesInputNumber() != null)
			for (int i = 0; i < message.getResourcesInputList().size(); i++) {
				if(message.getResourcesInputList().get(i)==Resources.COIN){
					inputResources[0]=message.getResourcesInputNumber().get(i);
				}
				if(message.getResourcesInputList().get(i)==Resources.SERVANT){
					inputResources[1]=message.getResourcesInputNumber().get(i);
				}
				if(message.getResourcesInputList().get(i)==Resources.SHIELD){
					inputResources[2]=message.getResourcesInputNumber().get(i);
				}
				if(message.getResourcesInputList().get(i)==Resources.STONE){
					inputResources[3]=message.getResourcesInputNumber().get(i);
				}
			}
		if(message.getResourcesOutputNumber()!=null){
			for (int i = 0; i < message.getResourcesOutputList().size(); i++) {
				if(message.getResourcesOutputList().get(i)==Resources.COIN){
					outPutResources[0]=message.getResourcesOutputNumber().get(i);
				}
				if(message.getResourcesOutputList().get(i)==Resources.SERVANT){
					outPutResources[1]=message.getResourcesOutputNumber().get(i);
				}
				if(message.getResourcesOutputList().get(i)==Resources.SHIELD){
					outPutResources[2]=message.getResourcesOutputNumber().get(i);
				}
				if(message.getResourcesOutputList().get(i)==Resources.STONE){
					outPutResources[3]=message.getResourcesOutputNumber().get(i);
				}
			}
		}
		mechanics.getPlayer(playerIndex).activateProduction(message.getSelectedProductions(),inputResources,outPutResources);
		turnController.setPhaseType(PhaseType.MAIN_ACTION);
	}

	/**
	 * it handles leader card action
	 * @param message is a message from the client
	 */
	private void leaderActionHandler(LeaderAction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		if(message.getAction()==1){
			mechanics.getPlayer(playerIndex).activateLeaderCard(message.getSelectedLeader());
			turnController.setPhaseType(PhaseType.LEADER_ACTION);
		}

		else if(message.getAction()==2){
			mechanics.getPlayer(playerIndex).discardLeaderCard(message.getSelectedLeader());
			turnController.setPhaseType(PhaseType.LEADER_ACTION);
		}

		else if(turnController.isMainActionDone()){
			turnController.setPhaseType(PhaseType.END_TURN);
		}

	}

	

	
	private void endGameState(Message receivedMessage) {

	}
	
	/**
	 * TODO: called from the lobby when the game must be halted since a player left the game
	 */
	public void haltGame() {
	
	}

	
	

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}