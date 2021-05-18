package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.util.PlayerActions;
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
import java.util.Objects;

public class ServerSideController {

	private final GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	
	private final Lobby lobby;
	private ArrayList<String> nicknameList;
	private final boolean[] gameReady; //
	private final boolean[] initResources;
	//private boolean endGame;


	private TurnController turnController;
	
	private final int numberOfPlayers;
	
	/**
	 * Constructor of the class that handles game logic and interacts with the players
	 * @param lobby that created this match
	 * @param numberOfPlayers in the match
	 */
	public ServerSideController(Lobby lobby, int numberOfPlayers) {
		this.lobby = lobby;
		this.numberOfPlayers = numberOfPlayers;
		if(numberOfPlayers==1) {
			mechanics = new GameMechanicsSinglePlayer(numberOfPlayers);
		} else {
			mechanics = new GameMechanicsMultiPlayer(numberOfPlayers);
		}
		
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
	
	/**
	 * method called by the lobby: this is a message switcher that executes different things based on the current state of the game
	 * @param receivedMessage message from the client handler
	 */
	public void onMessageReceived(Message receivedMessage){
		switch (gameState){
			case CONFIG -> configHandler(receivedMessage);
			case INIT -> initState(receivedMessage);
			case GAME -> inGameState(receivedMessage);
			default -> throw new IllegalStateException("Unexpected value: " + gameState);
		}
	}
	
	/**
	 * sets the game configuration
	 * @param config message
	 */
	private void configHandler(Message config) {
		if (config != null && config.getMessageType() == MessageType.GAME_CONFIG_REPLY) {
			GameConfigReply gameConfigReply = (GameConfigReply) config;
			setGameConfig(gameConfigReply.getGameConfiguration());
		}
	}
	
	/**
	 * set the game configuration
	 * @param path standard or full path of custom config
	 */
	public void setGameConfig(String path) {
		if (path.equalsIgnoreCase("standard")) {
			String fileName = "game_configuration_complete.xml";
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
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
	 * it starts the pregame (ask for initial resources)
	 */
	public void startPreGame(){
		turnController = new TurnController(virtualViewMap, this, nicknameList, mechanics);
		gameState = GameState.INIT;
		lobby.matchStart();
		
		VirtualView vv = virtualViewMap.get(nicknameList.get(0));
		vv.showGenericMessage("You are the first player! Wait!");
		vv.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck())); //TODO: just for test
		if(numberOfPlayers == 1) { //single player
			controllerAskLeaders();
		} else { // multiplayer
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
	 * it starts the game (start a new turn)
	 */
	private void startGame() {
		gameState = GameState.GAME;
		
		//TODO: STOP CHEATING!
		Resources[] resources={Resources.COIN,Resources.STONE,Resources.STONE,Resources.SHIELD,Resources.SHIELD,Resources.SHIELD};
		mechanics.getPlayer(0).getPlayersWarehouseDepot().setDepotForDebugging(resources);
		
		turnController.startTurn();
	}

	/**
	 * switches messages while the game state is set on INIT
	 * @param receivedMessage incoming message from the client
	 */
	private void initState(Message receivedMessage) {
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST-> initialResourcesHandler((ResourcesList) receivedMessage);
			case LEADER_SELECTION -> leaderSelectionHandler((LeaderSelection) receivedMessage);
		}
	}
	
	/**
	 * asks the player to choose the leader cards to use in game
	 */
	private void controllerAskLeaders() {
		for (int i = 0; i < numberOfPlayers; i++) {
			ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[0],false,false,false,
					mechanics.getPlayer(i).getLeaderCards()[0].getIdNumber()));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[1],false,false,false,
					mechanics.getPlayer(i).getLeaderCards()[1].getIdNumber()));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[2],false,false,false,
					mechanics.getPlayer(i).getLeaderCards()[2].getIdNumber()));
			leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(i).getLeaderCards()[3],false,false,false,
					mechanics.getPlayer(i).getLeaderCards()[3].getIdNumber()));
			VirtualView view = virtualViewMap.get(nicknameList.get(i));
			view.askInitLeaders(nicknameList.get(i), leaderCards);
		}
	}

	/**
	 * it handles initial Resources phase, adds incoming resources to the depot and moves the marker in the faith track (only third and fourth player).
	 * When all the players are ready,ask for leader cards selection
	 * @param message contains the initial resources chosen by the client
	 */
	private void initialResourcesHandler(ResourcesList message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.assignInitialAdvantage(message.getResourcesList(),playerIndex);
		initResources[playerIndex-1]=true;
		if(allTrue(initResources)){
			VirtualView view;
			for (int i = 0; i < nicknameList.size(); i++) {
				view=virtualViewMap.get(nicknameList.get(i));
				for (int j = 0; j < nicknameList.size(); j++) {
					if(i==j) {
						view.showGenericMessage("These are your Depot and your Faith Track now!");
					}
					else {
						view.showGenericMessage("These are " + nicknameList.get(j)+"'s Depot and Faith Track!");
					}
					view.showDepot(nicknameList.get(i), new ReducedWarehouseDepot(mechanics.getPlayer(j).getPlayersWarehouseDepot()));
					view.showFaithTrack(nicknameList.get(i), new ReducedFaithTrack(mechanics.getPlayer(j).getPlayerFaithTrack()));
				}
			}
			controllerAskLeaders();
		}
	}

	/**
	 * it handles initial leader cards selection, when all players are ready starts the game
	 * @param message is a message from the client
	 */
	private void leaderSelectionHandler(LeaderSelection message) {
		int playerIndex= nicknameList.indexOf(message.getNickname());
		VirtualView view= virtualViewMap.get(message.getNickname());
		mechanics.getPlayer(playerIndex).chooseTwoLeaders(message.getLeaderSelection().get(0),message.getLeaderSelection().get(1));
		ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
		view.showGenericMessage("Your Leader Cards now!");
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[0],false,false,false,
				mechanics.getPlayer(playerIndex).getLeaderCards()[0].getIdNumber()));
		leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[1],false,false,false,
				mechanics.getPlayer(playerIndex).getLeaderCards()[1].getIdNumber()));
		view.showLeaderCards(nicknameList.get(playerIndex), leaderCards);
		gameReady[playerIndex]=true;
		if(allTrue(gameReady))
			startGame();

	}

	/**
	 * switches incoming messages while the match is going on, after the initial phase (GAME state)
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
	 * it handles generic action selection, and then replies
	 * @param message the chosen action from the client
	 */
	private void actionReplyHandler(ActionReply message){
		VirtualView view= virtualViewMap.get(message.getNickname());
		int playerIndex=nicknameList.indexOf(message.getNickname());
		switch (message.getSelectedAction()){
			case MARKET -> view.askMarketAction(new ReducedMarket(mechanics.getMarket()));
			case BUY_CARD -> view.askBuyCardAction(new ArrayList<>
				(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherAllPlayersResources(),
						mechanics.getPlayer(playerIndex).getPlayersCardManager())), false);
			case PRODUCTIONS -> {
				view.showPlayerCardsAndProduction(new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
				sendBoxes(view,playerIndex,false); //it sends player boxes before production
				view.askProductionAction(mechanics.getPlayer(playerIndex).getPlayersCardManager().availableProductions());
			}
			case LEADER -> {
				ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
				Player player = mechanics.getPlayer(playerIndex);
				ArrayList<PlayerActions> leaderActions=player.checkAvailableLeaderActions();
				boolean checkLeader1=leaderActions.contains(PlayerActions.PLAY_LEADER_1);
				boolean checkLeader2=leaderActions.contains(PlayerActions.PLAY_LEADER_2);
				leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[0], player.isActiveAbilityLeader1(), player.isDiscardedLeader1(),checkLeader1,
						player.getLeaderCards()[0].getIdNumber()));
				leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[1], player.isActiveAbilityLeader2(), player.isDiscardedLeader2(),checkLeader2,
						player.getLeaderCards()[1].getIdNumber()));
				view.askLeaderAction(nicknameList.get(playerIndex), leaderCards);
			}
		}

	}
	
	/**
	 * Initial interaction with the market: the client moves a marble in the market
	 * @param message containing information regarding the movements in the market
	 */
	private void marketInteraction(MarketInteraction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());

		mechanics.getPlayer(playerIndex).interactWithMarket(message.getWhich(),message.getWhere());
		ResourceDeck deck = mechanics.getPlayer(playerIndex).getPlayersResourceDeck();

		for (String s : nicknameList) {
			view = virtualViewMap.get(s);
			view.showGenericMessage("This is the market after player interaction!");
			view.showMarket(new ReducedMarket(mechanics.getMarket()));
		}

		if(deck.isWhiteAbility1Activated() && deck.getWhiteMarblesTaken() > 0) {
			if (deck.isWhiteAbility2Activated()) { // both white marbles abilities are activated
				view.askWhiteMarbleChoice(deck.getFromWhiteMarble1(), deck.getFromWhiteMarble2(), deck.getWhiteMarblesInput1(),
						deck.getWhiteMarblesInput2(), deck.getWhiteMarblesTaken());
			} else { // automatically converts the white marbles with a single leader activated
				marketConvert(new WhiteMarbleReply(message.getNickname(), deck.getRightNumberOfActivations(), 0));
			}
		} else { // no white marble leader activated
			marketConvert(new WhiteMarbleReply(message.getNickname(),0,0));
		}
	}

	/**
	 * converts a list of resources containing white marbles, in the case 1 or 2 white marble leaders are activated
	 * @param message containing the number of activations
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
		if(deck.getFaithPoint() != 0) {
			mechanics.getPlayer(playerIndex).getPlayerFaithTrack().moveMarker(deck.getFaithPoint());
			view.showFaithTrack(message.getNickname(), new ReducedFaithTrack(mechanics.getPlayer(playerIndex).getPlayerFaithTrack()));
			deck.setFaithPoint(0);
			checkVaticanReport();
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
			for (int i = 0; i < numberOfPlayers; i++) {
				if(i != playerIndex) {
					mechanics.getPlayer(i).getPlayerFaithTrack().moveMarker(n);
					virtualViewMap.get(nicknameList.get(i)).showGenericMessage(message.getNickname()+ " discarded " + n +
							" resources, you get " + n + " faith points" );
					virtualViewMap.get(nicknameList.get(i)).showFaithTrack(nicknameList.get(i),
							new ReducedFaithTrack(mechanics.getPlayer(i).getPlayerFaithTrack()));
				}
			}
			checkVaticanReport();
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
			view.showPlayerCardsAndProduction(new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
			for (String s : nicknameList) {
				view = virtualViewMap.get(s);
				view.showGenericMessage("This is the Development Cards Deck now!");
				view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			}
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
		} else { //incorrect
			view.askBuyCardAction(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherAllPlayersResources(),
					management), true);
		}
		
	}

	/**
	 * it handles a generic production interaction
	 * @param message containing the list of chosen productions
	 */
	private void productionHandler(ProductionSelection message) {
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());

		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		if(cardProductionsManagement.isSelectedProductionAvailable(message.getSelectedProductions())) {
			cardProductionsManagement.setSelectedInput(message.getSelectedProductions());

			if (cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()) > 0) {
				// free user choices for input resources
				view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()));
			} else if (cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()) > 0) {
				// free user choices for output resources
				view.askFreeOutput(cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()));
			} else { // no free choices present
				int[] inputResources = new int[]{0, 0, 0, 0};
				int[] outPutResources = new int[]{0, 0, 0, 0};
				mechanics.getPlayer(playerIndex).activateProduction(message.getSelectedProductions(), inputResources, outPutResources);
				sendBoxes(view, playerIndex,true);
				turnController.setTurnPhase(TurnPhase.MAIN_ACTION);

			}
		} else { // productions cannot be completed
			view.showPlayerCardsAndProduction(new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
			view.askProductionAction(cardProductionsManagement.availableProductions());
		}
	}
	
	/**
	 * shows depot and strongbox
	 * @param view of the player
	 * @param playerIndex index
	 */
	private void sendBoxes(VirtualView view, int playerIndex, boolean forAll){
		//view.showGenericMessage("Your Boxes now!");
		if(forAll){
			for (String s : nicknameList) {
				view = virtualViewMap.get(s);
				view.showDepot(s, new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()));
				view.showStrongBox(s, new ReducedStrongbox(mechanics.getPlayer(playerIndex).getMyStrongbox()));
			}
		}
		else {
			view.showDepot(nicknameList.get(playerIndex), new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()));
			view.showStrongBox(nicknameList.get(playerIndex), new ReducedStrongbox(mechanics.getPlayer(playerIndex).getMyStrongbox()));
		}

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
				int[] outputResources = new int[]{0, 0, 0, 0};
				mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(), putResources(message), outputResources);
				sendBoxes(view,playerIndex,true);
				turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
			}
		}
		else view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(cardProductionsManagement.getSelectedInput()));

	}
	
	/**
	 * transforms a message containing a list of resources into an array of integers
	 * @param message containing a list of resources
	 * @return an array of integers
	 */
	private int[] putResources(ResourcesList message){
		int[] outputResources = new int[]{0,0,0,0};
		for (int i = 0; i < message.getResourcesList().size(); i++) {
			if(message.getResourcesList().get(i) == Resources.COIN){
				outputResources[0]=message.getResourcesNumber().get(i);
			} else if(message.getResourcesList().get(i) == Resources.SERVANT){
				outputResources[1]=message.getResourcesNumber().get(i);
			} else if(message.getResourcesList().get(i) == Resources.SHIELD){
				outputResources[2]=message.getResourcesNumber().get(i);
			} else if(message.getResourcesList().get(i) == Resources.STONE){
				outputResources[3]=message.getResourcesNumber().get(i);
			}
		}
		return outputResources;
	}
	
	/**
	 * handles free choices in output
	 * @param message containing a list of resources
	 */
	private void freeOutputHandler(ResourcesList message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(),
				cardProductionsManagement.getInputResources(), putResources(message));

		sendBoxes(view,playerIndex,true);
		turnController.setTurnPhase(TurnPhase.MAIN_ACTION);


	}

	/**
	 * The client chooses what to do with its leader cards
	 * @param message action describing the usage of the leader card (play or discard or nothing)
	 */
	private void leaderActionHandler(LeaderAction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		String nickname = nicknameList.get(playerIndex);

		if(message.getAction()==1) { // play leader
			mechanics.getPlayer(playerIndex).activateLeaderCard(message.getSelectedLeader());

			for (String s : nicknameList) {
				VirtualView view = virtualViewMap.get(s);

				if (!s.equals(nickname)) {
					ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();

					view.showGenericMessage(nickname + " played this Leader Card :");
					leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[message.getSelectedLeader()],
							true, false, false,
							mechanics.getPlayer(playerIndex).getLeaderCards()[message.getSelectedLeader()].getIdNumber()));
					view.showLeaderCards(nicknameList.get(playerIndex), leaderCards);
				} else {
					view.showGenericMessage("Leader Card successfully played!");
				}
			}
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);

		} else if(message.getAction()==2){ // discard leader

			mechanics.getPlayer(playerIndex).discardLeaderCard(message.getSelectedLeader());
			for (String s : nicknameList) {
				VirtualView view = virtualViewMap.get(s);

				if (!s.equals(nickname)) {
					ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();

					view.showGenericMessage(nickname + " discarded this Leader Card:");
					leaderCards.add(new ReducedLeaderCard(mechanics.getPlayer(playerIndex).getLeaderCards()[message.getSelectedLeader()],
							false, false, false,
							mechanics.getPlayer(playerIndex).getLeaderCards()[message.getSelectedLeader()].getIdNumber()));
					view.showLeaderCards(nicknameList.get(playerIndex), leaderCards);
				} else {
					view.showGenericMessage("Leader Card successfully discarded!");
				}
			}
			checkVaticanReport();
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);
			
		} else if(turnController.isMainActionDone()){ // nothing, next turn
			turnController.setTurnPhase(TurnPhase.END_TURN);
		}

		else turnController.turnAskAction();

	}

	/**
	 * Checks the vatican reports and notifies every player with their track
	 */
	private void checkVaticanReport(){
		boolean[] check = new boolean[4];
		boolean flag = true;
		boolean check2 = false;
		String nickname = "";
		do {
			for (int i = 0; i < numberOfPlayers; i++) {
				check[i] = mechanics.getPlayer(i).getPlayerFaithTrack().checkActivationVaticanReport(mechanics.getLastReportClaimed());
				if (check[i]) {
					check2 = true;
					nickname = nicknameList.get(i);
				}
			}
			for (int i = 0; i < numberOfPlayers && check2; i++) {
				mechanics.getPlayer(i).getPlayerFaithTrack().checkVaticanReport(mechanics.getLastReportClaimed());
			}

			if(check2) mechanics.increaseLastReportClaimed();

			for(int i = 0; i < numberOfPlayers; i++){
				if(mechanics.getPlayer(i).getPlayerFaithTrack().getLastReportClaimed() != mechanics.getLastReportClaimed())
					flag = false;
			}
		}while(!flag);

		for(int i = 0; i < numberOfPlayers && check2; i++) {
			String nick;
			if(nicknameList.get(i).equals(nickname))
				nick = "You";
			else
				nick = nickname;
			virtualViewMap.get(nicknameList.get(i)).showGenericMessage(nick + " triggered Vatican Report" +
					" n." + (mechanics.getLastReportClaimed()) + "!");
			virtualViewMap.get(nicknameList.get(i)).showFaithTrack(nickname, new ReducedFaithTrack(mechanics.getPlayer(i).getPlayerFaithTrack()));
		}
	}

	public Lobby getLobby() {
		return lobby;
	}

}
