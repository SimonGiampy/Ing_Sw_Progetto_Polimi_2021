package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.network.messages.login.GameConfigReply;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ServerSideController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;



	private Lobby lobby;
	private ArrayList<String> nicknameList;
	private final boolean[] gameReady; //
	private final boolean[] initResources;
	private boolean endGame;


	private TurnController turnController;
	
	private int numberOfPlayers;
	
	public ServerSideController(Lobby lobby, int numberOfPlayers) {
		this.lobby = lobby;
		this.numberOfPlayers = numberOfPlayers;
		if(numberOfPlayers==1)
			mechanics=new GameMechanicsSinglePlayer(this,numberOfPlayers);
		else
			mechanics = new GameMechanicsMultiPlayer(this, numberOfPlayers);
		initResources= new boolean[numberOfPlayers-1];
		gameReady= new boolean[numberOfPlayers];
		gameState = GameState.CONFIG;
	}
	
	
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
	private void startGame() {
		gameState = GameState.GAME;
		Resources[] resources={Resources.COIN,Resources.STONE,Resources.STONE,Resources.SHIELD,Resources.SHIELD,Resources.SHIELD};
		mechanics.getPlayer(0).getPlayersWarehouseDepot().setDepotForDebugging(resources);
		turnController.startTurn();
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
		if(numberOfPlayers==1) {
			controllerAskLeaders();
		}

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

	private void controllerAskLeaders(){
		for (int i = 0; i < numberOfPlayers; i++) {
			ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[0]));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[1]));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[2]));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[3]));
			VirtualView view = virtualViewMap.get(nicknameList.get(i));
			view.askInitLeaders(leaderCards);
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
			controllerAskLeaders();
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

	}

	/**
	 * it handles messages while the game state is set a GAME
	 * @param receivedMessage is a message from the client
	 */
	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case ACTION_REPLY-> actionReplyHandler((ActionReply) receivedMessage);
			case INTERACTION_WITH_MARKET-> marketInteraction((MarketInteraction) receivedMessage);
			case WHITE_MARBLE_REPLY -> marketConvert((WhiteMarbleReply) receivedMessage);
			case BUY_CARD-> buyCardHandler((BuyCard) receivedMessage);
			case DEPOT_INTERACTION -> depotInteractionHandler((DepotInteraction) receivedMessage);
			case PRODUCTION_SELECTION -> productionHandler((ProductionSelection) receivedMessage);
			case LEADER_ACTION -> leaderActionHandler((LeaderAction) receivedMessage);
			case RESOURCES_LIST ->{
				if (((ResourcesList)receivedMessage).getFlag()==1)
					freeInputHandler((ResourcesList)receivedMessage);
				else if(((ResourcesList)receivedMessage).getFlag()==2)
					freeOutputHandler((ResourcesList) receivedMessage);
			}
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
			case MARKET -> view.askMarketAction(new ReducedMarket(mechanics.getMarket()));
			case BUY_CARD -> view.askBuyCardAction(new ArrayList<>
				(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherAllPlayersResources(),
						mechanics.getPlayer(playerIndex).getPlayersCardManager())), false);
			case PRODUCTIONS -> view.askProductionAction(mechanics.getPlayer(playerIndex).getPlayersCardManager().availableProduction());
			case LEADER -> {
				ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[0]));
				leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[1]));
				view.askLeaderAction(leaderCards);
			}
		}

	}

	private void marketInteraction(MarketInteraction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());

		mechanics.getPlayer(playerIndex).interactWithMarket(message.getWhich(),message.getWhere());
		ResourceDeck deck = mechanics.getPlayer(playerIndex).getPlayersResourceDeck();

		if(deck.isWhiteAbility1Activated() && deck.getWhiteMarblesTaken() > 0) {
			if (deck.isWhiteAbility2Activated()) {
				view.askWhiteMarbleChoice(deck.getFromWhiteMarble1(), deck.getFromWhiteMarble2(), deck.getWhiteMarblesInput1(),
						deck.getWhiteMarblesInput2(), deck.getWhiteMarblesTaken());
			}
			else{
				marketConvert(new WhiteMarbleReply(message.getNickname(), deck.getRightNumberOfActivations(), 0));
			}
		}
		else{
			marketConvert(new WhiteMarbleReply(message.getNickname(),0,0));
		}
	}

	/**
	 * it handles market interaction
	 * @param message interaction with market: initial market message
	 */
	private void marketConvert(WhiteMarbleReply message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		ResourceDeck deck = mechanics.getPlayer(playerIndex).getPlayersResourceDeck();

		try {
			mechanics.getPlayer(playerIndex).convertMarketOutput(message.getLeader1(), message.getLeader2());
		} catch (InvalidUserRequestException e) {
			view.askWhiteMarbleChoice(deck.getFromWhiteMarble1(), deck.getFromWhiteMarble2(), deck.getWhiteMarblesInput1(),
					deck.getWhiteMarblesInput2(), deck.getWhiteMarblesTaken());
			return;
		}

		//moves the resources automatically to the additional depots if possible
		mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot().moveResourcesToAdditionalDepots();
		//sends confirmation of the completed action
		view.replyDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()),
				true,true,true);
	}
	
	/**
	 * handles the cycle of the interaction of the player with its warehouse depot
	 * @param message depot interaction: a single move on the board
	 */
	private void depotInteractionHandler(DepotInteraction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		
		if(message.isConfirmed()){ //if the player confirmed its actions
			int n = mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot().discardResourcesAfterUserConfirmation();
			for (int i = 0; i < numberOfPlayers && i != playerIndex; i++) {
				mechanics.getPlayer(i).getPlayerFaithTrack().moveMarker(n);
				virtualViewMap.get(nicknameList.get(i)).showFaithTrack(new ReducedFaithTrack(mechanics.getPlayer(i).getPlayerFaithTrack()));
			}
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION); //next turn
		} else {
			//if the player did something else with its depot
			WarehouseDepot depot = mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot();
			boolean confirmable = false;
			if (message.getToWhere().equals("depot")) {
				try {
					confirmable = depot.moveResourcesToDepot(message.getFromWhere(), message.getOrigin(), message.getDestination());
				} catch (InvalidUserRequestException e) {
					view.replyDepot(new ReducedWarehouseDepot(depot), false, message.isConfirmed(), false);
					return;
				}
			} else if (message.getToWhere().equals("deck")) {
				try {
					confirmable = depot.moveResourcesBackToDeck(message.getOrigin());
				} catch (InvalidUserRequestException e) {
					view.replyDepot(new ReducedWarehouseDepot(depot), false, message.isConfirmed(), false);
					return;
				}
			}
			//sends the client the result of their actions
			view.replyDepot(new ReducedWarehouseDepot(depot), false, confirmable, true);
		}
	}

	/**
	 * Checks if the selected card from the client is buyable. If it is buyable, then it places the card in the selected slot, otherwise
	 *      it replies telling the client that the slot was wrong.
	 * @param message buy card message containing the parameters of the chosen card
	 */
	private void buyCardHandler(BuyCard message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement management = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		if (management.checkStackLevel(message.getSlot()) == message.getLevel() - 1) { // correct
			mechanics.getPlayer(playerIndex).buyNewDevCard(message.getLevel(),message.getColor(),message.getSlot());
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
		} else { //incorrect
			view.askBuyCardAction(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherAllPlayersResources(),
					management), true);
		}
		
	}

	/**
	 * it handles production interaction
	 * @param message is a message from the client
	 */
	private void productionHandler(ProductionSelection message) {
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		sendBoxes(view,playerIndex); //it sends player boxes before production

		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		cardProductionsManagement.setSelectedInput(message.getSelectedProductions());

		if (cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()) > 0)
			view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()));

		else if (cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()) > 0)
			view.askFreeOutput(cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()));

		else {
			try {
				int[] inputResources = new int[]{0, 0, 0, 0};
				int[] outPutResources = new int[]{0, 0, 0, 0};
				mechanics.getPlayer(playerIndex).activateProduction(message.getSelectedProductions(), inputResources, outPutResources);
				sendBoxes(view,playerIndex);
				turnController.setTurnPhase(TurnPhase.MAIN_ACTION);

			} catch (InvalidInputException e) {
				view.askProductionAction(cardProductionsManagement.availableProduction());
			}
		}
	}

	private void sendBoxes(VirtualView view, int playerIndex){
		view.showGenericMessage("Your Boxes now!");
		view.showDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()));
		view.showStrongBox(new ReducedStrongbox(mechanics.getPlayer(playerIndex).getMyStrongbox()));

	}

	private void freeInputHandler(ResourcesList message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		if(cardProductionsManagement.checkFreeInput(cardProductionsManagement.getSelectedInput(),putResources(message))) {
			if (cardProductionsManagement.numberOfFreeChoicesInOutputProductions(cardProductionsManagement.getSelectedInput()) > 0) {
					cardProductionsManagement.setInputResources(putResources(message));
					view.askFreeOutput(cardProductionsManagement.numberOfFreeChoicesInOutputProductions(cardProductionsManagement.getSelectedInput()));
			} else {
				try {
					int[] outputResources = new int[]{0, 0, 0, 0};
					mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(), putResources(message), outputResources);
					sendBoxes(view,playerIndex);
					turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
				} catch (InvalidInputException e) {
					view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(cardProductionsManagement.getSelectedInput()));
				}
			}
		}
		else view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(cardProductionsManagement.getSelectedInput()));

	}

	private int[] putResources(ResourcesList message){
		int[] outputResources= new int[]{0,0,0,0};
		for (int i = 0; i < message.getResourcesList().size(); i++) {
			if(message.getResourcesList().get(i)==Resources.COIN){
				outputResources[0]=message.getResourcesNumber().get(i);
			}
			if(message.getResourcesList().get(i)==Resources.SERVANT){
				outputResources[1]=message.getResourcesNumber().get(i);
			}
			if(message.getResourcesList().get(i)==Resources.SHIELD){
				outputResources[2]=message.getResourcesNumber().get(i);
			}
			if(message.getResourcesList().get(i)==Resources.STONE){
				outputResources[3]=message.getResourcesNumber().get(i);
			}
		}
		return outputResources;
	}
	private void freeOutputHandler(ResourcesList message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		try {
			mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(),
					cardProductionsManagement.getInputResources(), putResources(message));

			sendBoxes(view,playerIndex);
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}


	}

	/**
	 * The client chooses what to do with its leader cards
	 * @param message is a message from the client
	 */
	private void leaderActionHandler(LeaderAction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		if(message.getAction()==1) { // play leader
			mechanics.getPlayer(playerIndex).activateLeaderCard(message.getSelectedLeader());
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);
			
		} else if(message.getAction()==2){ // discard leader
			mechanics.getPlayer(playerIndex).discardLeaderCard(message.getSelectedLeader());
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);
			
		} else if(turnController.isMainActionDone()){ // nothing, next turn
			turnController.setTurnPhase(TurnPhase.END_TURN);
		}

		else turnController.turnAskAction();

	}

	

	
	private void endGameState(Message receivedMessage) {

	}
	
	/**
	 * TODO: called from the lobby when the game must be halted since a player left the game
	 */
	public void haltGame() {
	
	}
	public Lobby getLobby() {
		return lobby;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
}
