package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.singleplayer.GameMechanicsSinglePlayer;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.parser.XMLParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ServerSideController {

	public GameMechanicsMultiPlayer getMechanics() {
		return mechanics;
	}

	private final GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	
	private final Lobby lobby;
	private ArrayList<String> nicknameList;
	private final boolean[] gameReady; //
	private final boolean[] initResources;


	private TurnController turnController;
	
	private final int numberOfPlayers;
	
	/**
	 * Constructor of the class that handles game logic and interacts with the players. Online game mode
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
		gameState = GameState.INIT;
	}
	
	/**
	 * offline controller for single player mode
	 */
	public ServerSideController() {
		this(null, 1);
	}

	/**
	 * Method called just before the game starts, when all the players are ready to play
	 * @param virtualViewMap combinations of nicknames and their virtual views
	 */
	public void setVirtualViews(HashMap<String, VirtualView> virtualViewMap) {
		this.virtualViewMap = virtualViewMap;
		nicknameList = new ArrayList<>(virtualViewMap.keySet());
		Collections.shuffle(nicknameList);
		
		setGameConfig();
		
		// sends a message to all the players containing a list of the nicknames the players in the match
		for(String s: nicknameList) {
			virtualViewMap.get(s).showMatchInfo(nicknameList);
		}
		if (lobby != null) {
			lobby.matchStart();
		}
		startPreGame();
	}
	
	/**
	 * method called by the lobby: this is a message switcher that executes different things based on the current state of the game
	 * @param receivedMessage message from the client handler
	 */
	public void onMessageReceived(Message receivedMessage){
		switch (gameState){
			case INIT -> initState(receivedMessage);
			case GAME -> inGameState(receivedMessage);
			default -> throw new IllegalStateException("Unexpected value: " + gameState);
		}
	}
	
	/**
	 * set the game configuration
	 */
	public void setGameConfig() {
		String fileName = "game_configuration_standard.xml";
		XMLParser parser = new XMLParser(fileName);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devCards, leaderCards, baseProduction, tiles, report);
		
		Lobby.LOGGER.info("Game configuration has been read and applied to the lobby settings");

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

		int j = 1;
		for(String s: nicknameList) {
			VirtualView view = virtualViewMap.get(s);
			view.showGenericMessage("You are player n. " + j);
			view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			view.showMarket(new ReducedMarket(mechanics.getMarket()));
			j++;
		}
		if(numberOfPlayers == 1) { //single player
			controllerAskLeaders();
			sendBoxes(0, false);
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
	protected void startGame() {
		gameState = GameState.GAME;
		
		//TODO: STOP CHEATING!
		/*
		ArrayList<Resources> set= new ArrayList<>();
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SERVANT);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.SHIELD);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		
		mechanics.getPlayer(0).getMyStrongbox().storeResources(set);

		*/
		//mechanics.getPlayer(0).getPlayersWarehouseDepot().setDepotForDebugging(resources);
		turnController.startTurn();
	}

	/**
	 * switches messages while the game state is set on INIT
	 * @param receivedMessage incoming message from the client
	 */
	protected void initState(Message receivedMessage) {
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST-> initialResourcesHandler((ResourcesList) receivedMessage);
			case LEADER_SELECTION -> leaderSelectionHandler((LeaderSelection) receivedMessage);
		}
	}
	
	/**
	 * asks the player to choose the leader cards to use in game
	 */
	protected void controllerAskLeaders() {
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
	 * When all the players are ready, ask for leader cards selection
	 * @param message contains the initial resources chosen by the client
	 */
	protected void initialResourcesHandler(ResourcesList message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.assignInitialAdvantage(message.getResourcesList(),playerIndex);
		initResources[playerIndex-1]=true;
		if(allTrue(initResources)){
			for (int i = 0; i < nicknameList.size(); i++) {
				for (int j = 0; j < nicknameList.size(); j++) {
					sendBoxes(nicknameList.indexOf(nicknameList.get(j)), true);
				}
			}
			sendFaithTracks();
			controllerAskLeaders();
		}
	}

	/**
	 * it handles initial leader cards selection, when all players are ready starts the game
	 * @param message is a message from the client
	 */
	protected void leaderSelectionHandler(LeaderSelection message) {
		int playerIndex= nicknameList.indexOf(message.getNickname());
		VirtualView view= virtualViewMap.get(message.getNickname());
		Player player = mechanics.getPlayer(playerIndex);
		player.chooseTwoLeaders(message.getLeaderSelection().get(0),message.getLeaderSelection().get(1));
		view.showLeaderCards(nicknameList.get(playerIndex), obtainLeadersFromPlayer(player));
		gameReady[playerIndex] = true;
		if(allTrue(gameReady))
			startGame();
	}

	/**
	 * switches incoming messages while the match is going on, after the initial phase (GAME state)
	 * @param receivedMessage is a message from the client
	 */
	protected void inGameState(Message receivedMessage){
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
	protected void actionReplyHandler(ActionReply message){
		VirtualView view= virtualViewMap.get(message.getNickname());
		int playerIndex=nicknameList.indexOf(message.getNickname());
		switch (message.getSelectedAction()){
			case MARKET -> view.askMarketAction(nicknameList.get(playerIndex),new ReducedMarket(mechanics.getMarket()));
			case BUY_CARD -> view.askBuyCardAction(nicknameList.get(playerIndex),new ArrayList<>
				(mechanics.getGameDevCardsDeck().buyableCards(mechanics.getPlayer(playerIndex).gatherResourcesWithDiscounts(),
						mechanics.getPlayer(playerIndex).getPlayersCardManager())), false);
			case PRODUCTIONS -> {
				view.showPlayerCardsAndProduction(nicknameList.get(playerIndex),
						new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
				sendBoxes(playerIndex,false); //it sends player boxes before production
				view.askProductionAction(nicknameList.get(playerIndex),mechanics.getPlayer(playerIndex).getPlayersCardManager().availableProductions());
			}
			case LEADER -> {
				Player player = mechanics.getPlayer(playerIndex);
				ArrayList<ReducedLeaderCard> leaderCards = obtainLeadersFromPlayer(player);
				view.askLeaderAction(nicknameList.get(playerIndex), leaderCards);
			}
		}

	}

	/**
	 * Initial interaction with the market: the client moves a marble in the market
	 * @param message containing information regarding the movements in the market
	 */
	protected void marketInteraction(MarketInteraction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());

		mechanics.getPlayer(playerIndex).interactWithMarket(message.getWhich(),message.getWhere());
		ResourceDeck deck = mechanics.getPlayer(playerIndex).getPlayersResourceDeck();

		for (String s : nicknameList) {
			view = virtualViewMap.get(s);
			view.showGenericMessage(message.getNickname()+ " went to the market!");
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
	protected void marketConvert(WhiteMarbleReply message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		ResourceDeck deck = mechanics.getPlayer(playerIndex).getPlayersResourceDeck();

		try {
			mechanics.getPlayer(playerIndex).convertMarketOutput(message.getLeader1(), message.getLeader2());
		} catch (InvalidUserRequestException e) {
			view.showError("Wrong number of activations! Choose different values");
			view.askWhiteMarbleChoice(deck.getFromWhiteMarble1(), deck.getFromWhiteMarble2(), deck.getWhiteMarblesInput1(),
					deck.getWhiteMarblesInput2(), deck.getWhiteMarblesTaken());
			return;
		}
		if(deck.getFaithPoint() != 0) {
			mechanics.getPlayer(playerIndex).getPlayerFaithTrack().moveMarker(deck.getFaithPoint());
			deck.setFaithPoint(0);
			if(!checkVaticanReport())
				sendFaithTracks();
		}
		
		//moves the resources automatically to the additional depots if possible
		//sends a message to the player if any resources were moved automatically
		int moved = mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot().moveResourcesToAdditionalDepots();
		if (moved > 0) {
			view.showGenericMessage(moved + " resource(s) have been moved automatically to the leaders' additional depots");
		}
		
		//sends confirmation of the completed action
		view.replyDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()),
				true,true,true);
	}
	
	/**
	 * handles the cycle of the interaction of the player with its warehouse depot
	 * @param message depot interaction: a single move on the board
	 */
	protected void depotInteractionHandler(DepotInteraction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		
		if(message.isConfirmed()){ //if the player confirmed its actions
			int n = mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot().discardResourcesAfterUserConfirmation();
			for (int i = 0; i < numberOfPlayers && n > 0; i++) {
				if(mechanics.getNumberOfPlayers() == 1){
					((GameMechanicsSinglePlayer) mechanics).getLorenzoFaithTrack().moveMarker(n);
					virtualViewMap.get(nicknameList.get(i)).showGenericMessage("You discarded " + n +
							" resources, Lorenzo gets " + n + " faith point(s)");
					if(((GameMechanicsSinglePlayer) mechanics).getLorenzoFaithTrack().isTrackFinished()) {
						turnController.sendWinner();
					}
				}
				else {
					if (i != playerIndex) {
						mechanics.getPlayer(i).getPlayerFaithTrack().moveMarker(n);
						virtualViewMap.get(nicknameList.get(i)).showGenericMessage(message.getNickname() + " discarded " + n +
								" resources, you get " + n + " faith point(s)");
					} else {
						virtualViewMap.get(nicknameList.get(i)).showGenericMessage("You discarded " + n +
								" resources, the other players get " + n + " faith point(s)");
					}
				}
			}
			if(!checkVaticanReport() && n > 0)
				sendFaithTracks();
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION); //next turn
		} else {
			//if the player did something else with its depot
			WarehouseDepot depot = mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot();
			boolean confirmable = false;
			if (message.getToWhere().equals("depot")) {
				try {
					confirmable = depot.moveResourcesToDepot(message.getFromWhere(), message.getOrigin(), message.getDestination());
				} catch (InvalidUserRequestException e) {
					view.replyDepot(new ReducedWarehouseDepot(depot), false, depot.isCombinationCorrect(), false);
					return;
				}
			} else if (message.getToWhere().equals("deck")) {
				try {
					confirmable = depot.moveResourcesBackToDeck(message.getOrigin());
				} catch (InvalidUserRequestException e) {
					view.replyDepot(new ReducedWarehouseDepot(depot), false, depot.isCombinationCorrect(), false);
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
	protected void buyCardHandler(BuyCard message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement management = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		if (management.checkStackLevel(message.getSlot()) == message.getLevel() - 1) { // correct
			mechanics.getPlayer(playerIndex).buyNewDevCard(message.getLevel(),message.getColor(),message.getSlot());
			for (String s : nicknameList) {
				view = virtualViewMap.get(s);
				view.showGenericMessage(nicknameList.get(playerIndex) + " bought a development card!");
				view.showPlayerCardsAndProduction(nicknameList.get(playerIndex),
						new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
				view.showCardsDeck(new ReducedDevelopmentCardsDeck(mechanics.getGameDevCardsDeck()));
			}
			sendBoxes(playerIndex, true);
			turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
		} else { //incorrect
			view.askBuyCardAction(nicknameList.get(playerIndex),mechanics.getGameDevCardsDeck().
					buyableCards(mechanics.getPlayer(playerIndex).gatherResourcesWithDiscounts(), management), true);
		}
		
	}

	/**
	 * it handles a generic production interaction
	 * @param message containing the list of chosen productions
	 */
	protected void productionHandler(ProductionSelection message) {
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		if(cardProductionsManagement.isSelectedProductionAvailable(message.getSelectedProductions())) { //if selected productions are available
			cardProductionsManagement.setSelectedInput(message.getSelectedProductions()); //set selected productions
			if (cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()) > 0) {
				// free user choices for input resources
				view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(message.getSelectedProductions()));
			} else if (cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()) > 0) {
				// free user choices for output resources
				cardProductionsManagement.setInputResources(new int[]{0,0,0,0});
				view.askFreeOutput(cardProductionsManagement.numberOfFreeChoicesInOutputProductions(message.getSelectedProductions()));
			} else { // no free choices present
				int[] inputResources = new int[]{0, 0, 0, 0};
				int[] outPutResources = new int[]{0, 0, 0, 0};
				boolean moved = mechanics.getPlayer(playerIndex).activateProduction(
						message.getSelectedProductions(), inputResources, outPutResources);
				sendBoxes(playerIndex,true);
				for(String s:nicknameList){
					virtualViewMap.get(s).showGenericMessage(nicknameList.get(playerIndex) + " activated the production");
				}
				if (moved) {
					if(!checkVaticanReport())
						sendFaithTracks();
				}
				turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
			}

		} else { // productions cannot be completed
			view.showError("Selected productions are not available contemporarily.");
			view.showPlayerCardsAndProduction(nicknameList.get(playerIndex),
					new ReducedCardProductionManagement(mechanics.getPlayer(playerIndex).getPlayersCardManager()));
			view.askProductionAction(nicknameList.get(playerIndex),cardProductionsManagement.availableProductions());
		}
	}
	
	/**
	 * shows depot and strongbox to the chosen player(s)
	 * @param playerIndex index of the current playing player
	 * @param forAll if it needs to be shown to all the players in the game
	 */
	protected void sendBoxes(int playerIndex, boolean forAll) {
		Player player = mechanics.getPlayer(playerIndex);
		VirtualView view;
		if (forAll) {
			for (String s : nicknameList) {
				view = virtualViewMap.get(s);
				view.showDepot(nicknameList.get(playerIndex), new ReducedWarehouseDepot(player.getPlayersWarehouseDepot()));
				view.showStrongBox(nicknameList.get(playerIndex), new ReducedStrongbox(player.getMyStrongbox()));
			}
		} else {
			view = virtualViewMap.get(nicknameList.get(playerIndex));
			view.showDepot(nicknameList.get(playerIndex), new ReducedWarehouseDepot(player.getPlayersWarehouseDepot()));
			view.showStrongBox(nicknameList.get(playerIndex), new ReducedStrongbox(player.getMyStrongbox()));
		}

	}
	
	/**
	 * checks the resources asked as free choices in input: if there are sufficient resources it goes forward
	 * @param message list of resources chosen by the user
	 */
	protected void freeInputHandler(ResourcesList message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		VirtualView view = virtualViewMap.get(message.getNickname());
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();

		if(cardProductionsManagement.checkFreeInput(cardProductionsManagement.getSelectedInput(),putResources(message))) {//if input resources are available

			if (cardProductionsManagement.numberOfFreeChoicesInOutputProductions(cardProductionsManagement.getSelectedInput()) > 0) {
				// if there are free choices in output, it asks them to the player
				cardProductionsManagement.setInputResources(putResources(message));
				view.askFreeOutput(cardProductionsManagement.numberOfFreeChoicesInOutputProductions(cardProductionsManagement.getSelectedInput()));
			} else { // if there aren't output productions
				int[] outputResources = new int[]{0, 0, 0, 0};
				mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(), putResources(message), outputResources);
				sendBoxes(playerIndex,true);
				turnController.setTurnPhase(TurnPhase.MAIN_ACTION);
			}
		} else { // resources are not sufficient
			view.showError("Your resources are not sufficient for this choice.");
			view.askFreeInput(cardProductionsManagement.numberOfFreeChoicesInInputProductions(cardProductionsManagement.getSelectedInput()));
		}

	}
	
	/**
	 * transforms a message containing a list of resources into an array of integers
	 * @param message containing a list of resources
	 * @return an array of integers
	 */
	protected int[] putResources(ResourcesList message){
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
	protected void freeOutputHandler(ResourcesList message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		
		CardProductionsManagement cardProductionsManagement = mechanics.getPlayer(playerIndex).getPlayersCardManager();
		boolean moved = mechanics.getPlayer(playerIndex).activateProduction(cardProductionsManagement.getSelectedInput(),
				cardProductionsManagement.getInputResources(), putResources(message));
		
		if (moved)
			if (!checkVaticanReport())
				sendFaithTracks();
		sendBoxes(playerIndex,true);
		for(String s:nicknameList){
			virtualViewMap.get(s).showGenericMessage(nicknameList.get(playerIndex) + " activated the production");
		}
		turnController.setTurnPhase(TurnPhase.MAIN_ACTION);

	}

	/**
	 * The client chooses what to do with its leader cards
	 * @param message action describing the usage of the leader card (play or discard or nothing)
	 */
	protected void leaderActionHandler(LeaderAction message){
		int playerIndex = nicknameList.indexOf(message.getNickname());
		String nickname = nicknameList.get(playerIndex);
		Player player = mechanics.getPlayer(playerIndex);

		if(message.getAction()==1) { // play leader
			player.activateLeaderCard(message.getSelectedLeader() - 1);

			for (String s : nicknameList) {

				VirtualView view = virtualViewMap.get(s);

				if (!s.equals(nickname)) {
					view.showGenericMessage(nickname + " played a Leader Card");
				} else {
					view.showGenericMessage("Leader Card successfully played!");
				}
				view.showLeaderCards(nicknameList.get(playerIndex), obtainLeadersFromPlayer(player));
			}
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);

		} else if(message.getAction()==2){ // discard leader

			player.discardLeaderCard(message.getSelectedLeader()-1);
			for (String s : nicknameList) {

				VirtualView view = virtualViewMap.get(s);

				if (!s.equals(nickname)) {
					view.showGenericMessage(nickname + " discarded a Leader Card");
				} else {
					view.showGenericMessage("Leader Card successfully discarded!");
				}
				view.showLeaderCards(nicknameList.get(playerIndex), obtainLeadersFromPlayer(player));
				view.showFaithTrack(nicknameList.get(playerIndex), new ReducedFaithTrack(player.getPlayerFaithTrack()));
			}
			checkVaticanReport();
			turnController.setTurnPhase(TurnPhase.LEADER_ACTION);
			
		} else if(turnController.isMainActionDone()){ // nothing, next turn
			turnController.setTurnPhase(TurnPhase.END_TURN);
		}

		else turnController.turnAskAction();

	}

	/**
	 * creates arrayList of leader cards to send
	 * @param player the owner of the cards
	 * @return arraylist od reduced leader cards
	 */
	public ArrayList<ReducedLeaderCard> obtainLeadersFromPlayer(Player player) {
		ArrayList<PlayerActions> leaderActions = player.checkAvailableLeaderActions();
		ArrayList<ReducedLeaderCard> leaderCards = new ArrayList<>();
		boolean checkLeader1 = leaderActions.contains(PlayerActions.PLAY_LEADER_1);
		boolean checkLeader2 = leaderActions.contains(PlayerActions.PLAY_LEADER_2);
		leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[0], player.isActiveAbilityLeader1(),
				player.isDiscardedLeader1(), checkLeader1, player.getLeaderCards()[0].getIdNumber()));
		leaderCards.add(new ReducedLeaderCard(player.getLeaderCards()[1], player.isActiveAbilityLeader2(),
				player.isDiscardedLeader2(), checkLeader2, player.getLeaderCards()[1].getIdNumber()));
		return leaderCards;
	}

	/**
	 * Checks the vatican reports and notifies every player with their track
	 */
	protected boolean checkVaticanReport(){
		boolean[] check = new boolean[4];
		boolean flag = true;
		boolean check2 = false;
		String nickname = "";
		boolean send = false;

		if(mechanics.getNumberOfPlayers() == 1){
			GameMechanicsSinglePlayer mec = (GameMechanicsSinglePlayer) mechanics;
			boolean lorenzoCheck = mec.getLorenzoFaithTrack().checkActivationVaticanReport(mec.getLastReportClaimed());
			boolean playerCheck = mec.getPlayer(0).getPlayerFaithTrack().checkActivationVaticanReport(mec.getLastReportClaimed());
			if(lorenzoCheck || playerCheck) {
				mec.getPlayer(0).getPlayerFaithTrack().checkVaticanReport(mec.getLastReportClaimed());
				mec.getLorenzoFaithTrack().checkVaticanReport(mec.getLastReportClaimed());
				mec.increaseLastReportClaimed();
				send = true;
				if (lorenzoCheck) {
					virtualViewMap.get(nicknameList.get(0)).showGenericMessage("Lorenzo triggered Vatican Report" +
							" n." + mechanics.getLastReportClaimed() + "!");
				} else {
					virtualViewMap.get(nicknameList.get(0)).showGenericMessage("You triggered Vatican Report" +
							" n." + mechanics.getLastReportClaimed() + "!");
				}
			}
		} else {
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

				if (check2) mechanics.increaseLastReportClaimed();

				for (int i = 0; i < numberOfPlayers; i++) {
					if (mechanics.getPlayer(i).getPlayerFaithTrack().getLastReportClaimed() != mechanics.getLastReportClaimed())
						flag = false;
				}
			} while (!flag);

			for (int i = 0; i < numberOfPlayers && check2; i++) {
				String nick;
				if (nicknameList.get(i).equals(nickname))
					nick = "You";
				else
					nick = nickname;
				virtualViewMap.get(nicknameList.get(i)).showGenericMessage(nick + " triggered Vatican Report" +
						" n." + (mechanics.getLastReportClaimed()) + "!");
				send = true;
			}
		}
		if(send)
			sendFaithTracks();
		return send;
	}

	/**
	 * it sends faith tracks to all the players
	 */
	protected void sendFaithTracks(){
		for(String s: nicknameList){
			VirtualView view = virtualViewMap.get(s);
			for(int i = 0; i < nicknameList.size(); i++){
				view.showFaithTrack(nicknameList.get(i), new ReducedFaithTrack(mechanics.getPlayer(i).getPlayerFaithTrack()));
			}
		}
		if(mechanics.getNumberOfPlayers() == 1){ //single player
			VirtualView view = virtualViewMap.get(nicknameList.get(0));
			view.showFaithTrack(nicknameList.get(0), new ReducedFaithTrack(((GameMechanicsSinglePlayer) mechanics).getLorenzoFaithTrack()));
		}
	}

	public Lobby getLobby() {
		return lobby;
	}

}
