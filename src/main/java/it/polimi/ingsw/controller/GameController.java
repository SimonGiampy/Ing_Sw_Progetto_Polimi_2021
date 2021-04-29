package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.ReducedLeaderCard;
import it.polimi.ingsw.model.reducedClasses.ReducedMarket;
import it.polimi.ingsw.model.reducedClasses.ReducedWarehouseDepot;
import it.polimi.ingsw.model.util.GameState;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.network.messages.game.server2client.LeaderShow;
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

public class GameController {

	private GameMechanicsMultiPlayer mechanics;
	private GameState gameState;
	private HashMap<String, VirtualView> virtualViewMap;
	private Lobby lobby;
	private ArrayList<String> nicknameList;
	private final boolean[] gameReady; //
	private final boolean[] initResources;


	private TurnController turnController;
	
	private int numberOfPlayers;
	
	public GameController(Lobby lobby, int numberOfPlayers) {
		this.lobby = lobby;
		this.numberOfPlayers = numberOfPlayers;
		mechanics = new GameMechanicsMultiPlayer(this, numberOfPlayers);
		initResources= new boolean[numberOfPlayers-1];
		gameReady= new boolean[numberOfPlayers];
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

	public void setTurnController(TurnController turnController) {
		this.turnController = turnController;
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
	
	//TODO: must be called when game mechanics is ready
	public void readGameConfig(String fullPath) {
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanics.instantiateGame(devCards, leaderCards, baseProduction, tiles, report);

	}

	private static boolean allTrue(boolean[] values){
		for (boolean value : values) {
			if (!value)
				return false;
		}
		return true;
	}

	private void startGame(){
		setGameState(GameState.GAME);
		//need a broadcast message
		turnController.newTurn();
	}

	public void startPreGame(){
		setTurnController(new TurnController(virtualViewMap,this,nicknameList,mechanics));
		setGameState(GameState.INIT);
		VirtualView vv= virtualViewMap.get(nicknameList.get(0));
		vv.showGenericMessage("You are the first player! Wait for the others players");
		if(numberOfPlayers==1)
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

	private void initState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case RESOURCES_LIST-> initialResourcesHandler((ResourcesList) receivedMessage);
			case LEADER_SELECTION -> leaderSelectionHandler((LeaderSelection) receivedMessage);
		}
	}

	private void initialResourcesHandler(ResourcesList message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		mechanics.assignInitialAdvantage(message.getResourcesList(),playerIndex);
		initResources[playerIndex-1]=true;
		VirtualView view=virtualViewMap.get(message.getNickname());
		view.showGenericMessage("This is your depot now!");
		view.showDepot(new ReducedWarehouseDepot(mechanics.getPlayer(playerIndex).getPlayersWarehouseDepot()));

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

	private void inGameState(Message receivedMessage){
		switch (receivedMessage.getMessageType()){
			case ACTION_REPLY-> actionReplyHandler((ActionReply) receivedMessage);
			case INTERACTION_WITH_MARKET-> marketInteractionHandler((InteractionWithMarket) receivedMessage);
			case BUY_CARD-> buyCardHandler((BuyCard) receivedMessage);
			case PRODUCTION_SELECTION -> productionHandler((ProductionSelection) receivedMessage);
			case LEADER_ACTION -> leaderActionHandler((LeaderAction) receivedMessage);
		}
	}

	private void actionReplyHandler(ActionReply message){
		VirtualView view= virtualViewMap.get(message.getNickname());
		int playerIndex=nicknameList.indexOf(message.getNickname());
		switch (message.getSelectedAction()){
			case MARKET-> {
				view.askMarketAction(new ReducedMarket(mechanics.getMarket()));
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

	private void marketInteractionHandler(InteractionWithMarket message){
		int playerIndex= nicknameList.indexOf(message.getNickname());
		try {
			mechanics.getPlayer(playerIndex).interactWithMarket(message.getWhich(),message.getWhere());
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
			//TODO: move some depot methods
		}

		turnController.setPhaseType(PhaseType.MAIN_ACTION);
	}

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


	private void loginState(Message receivedMessage){

	}

	private void loginHandler(LobbyAccess message){
		message.getNickname();
	}

	private void endGameState(Message receivedMessage) {

	}
	
	/**
	 * TODO: called from the lobby when the game must be halted since a player left the game
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
