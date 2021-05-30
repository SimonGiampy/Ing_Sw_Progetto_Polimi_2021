package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.network.messages.game.client2server.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.OfflineVirtualView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ServerSideControllerTest {

	ServerSideController serverSideController;
	String nickname= "player1";
	@BeforeEach
	void setUp() {
		serverSideController = new ServerSideController(1);
		TestView testView= new TestView();
		OfflineVirtualView view = new OfflineVirtualView(testView);
		HashMap<String, VirtualView> map = new HashMap<>();

		map.put(nickname, view);
		OfflineController controller = new OfflineController(testView, serverSideController,nickname );
		testView.attach(controller);
		serverSideController.setVirtualViews(map);
		serverSideController.sendFaithTracks();
	}

	@Test
	void setVirtualViews() {
	}

	@Test
	void onMessageReceived() {
	}

	@Test
	void setGameConfig() {
	}

	@Test
	void startPreGame() {

	}

	@Test
	void startGame() {
	}

	@Test
	void initState() {
	}

	@Test
	void controllerAskLeaders() {
	}

	@Test
	void initialResourcesHandler() {
	}

	@Test
	void leaderSelectionHandler() {
		ArrayList<Integer> leaderSelection= new ArrayList<>();
		leaderSelection.add(1);
		leaderSelection.add(2);
		LeaderSelection message= new LeaderSelection(nickname,leaderSelection);
		serverSideController.leaderSelectionHandler(message);
	}

	@Test
	void inGameState() {
	}

	@Test
	void actionReplyHandler() {
	}

	@Test
	void marketInteraction() {

	}

	@Test
	void marketConvert() {
	}

	@Test
	void depotInteractionHandler() {
	}

	@Test
	void buyCardHandler() {
		BuyCard message = new BuyCard(nickname,1,Colors.YELLOW,1);
		serverSideController.buyCardHandler(message);
		assertEquals(1,serverSideController.getMechanics().getPlayer(0).getPlayersCardManager().getCards().get(0).size());
	}

	@Test
	void productionHandler() {
		ArrayList<Resources> set= new ArrayList<>();
		set.add(Resources.COIN);
		set.add(Resources.COIN);
		serverSideController.getMechanics().getPlayer(0).getMyStrongbox().storeResources(set);
		int size= serverSideController.getMechanics().getPlayer(0).getMyStrongbox().getContent().size();
		ArrayList<Productions> selectedProductions= new ArrayList<>();
		selectedProductions.add(Productions.BASE_PRODUCTION);
		ProductionSelection message = new ProductionSelection(nickname,selectedProductions);
		serverSideController.productionHandler(message);
		ArrayList<Resources> resources= new ArrayList<>();
		resources.add(Resources.COIN);
		ArrayList<Integer> number= new ArrayList<>();
		number.add(2);
		ResourcesList message2= new ResourcesList(nickname,resources,number,1);
		serverSideController.freeInputHandler(message2);
		ArrayList<Resources> outputResources= new ArrayList<>();
		outputResources.add(Resources.STONE);
		ArrayList<Integer> outputNumber= new ArrayList<>();
		outputNumber.add(1);
		ResourcesList message3= new ResourcesList(nickname,outputResources,outputNumber,2);
		serverSideController.freeOutputHandler(message3);
		assertEquals(size-1,serverSideController.getMechanics().getPlayer(0).getMyStrongbox().getContent().size());
		assertEquals(serverSideController.getMechanics().getPlayer(0).getMyStrongbox().getContent(),outputResources);
	}

	@Test
	void leaderActionHandler_Activate() {
		LeaderAction messageOne= new LeaderAction(nickname,1,1);
		serverSideController.leaderActionHandler(messageOne);
		assertTrue(serverSideController.getMechanics().getPlayer(0).isActiveAbilityLeader1());
		LeaderAction messageTwo= new LeaderAction(nickname,2,1);
		serverSideController.leaderActionHandler(messageTwo);
		assertTrue(serverSideController.getMechanics().getPlayer(0).isActiveAbilityLeader2());
	}

	@Test
	void leaderActionHandler_Discard() {
		LeaderAction messageOne= new LeaderAction(nickname,1,2);
		serverSideController.leaderActionHandler(messageOne);
		assertTrue(serverSideController.getMechanics().getPlayer(0).isDiscardedLeader1());
		LeaderAction messageTwo= new LeaderAction(nickname,2,2);
		serverSideController.leaderActionHandler(messageTwo);
		assertTrue(serverSideController.getMechanics().getPlayer(0).isDiscardedLeader2());
	}

	private class TestView extends ViewObservable implements View{

		@Override
		public void askNumberOfPlayer() {

		}

		@Override
		public void askNickname() {

		}

		@Override
		public void askInitResources(int number) {

		}

		@Override
		public void askInitLeaders(String nickname, ArrayList<ReducedLeaderCard> leaderCards) {

		}

		@Override
		public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {

		}

		@Override
		public void askAction(String nickname, ArrayList<PlayerActions> availableAction) {

		}

		@Override
		public void askMarketAction(String nickname, ReducedMarket market) {

		}

		@Override
		public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {

		}

		@Override
		public void askBuyCardAction(String nickname, ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {

		}

		@Override
		public void askProductionAction(String nickname, ArrayList<Productions> productionAvailable) {

		}

		@Override
		public void askFreeInput(int number) {

		}

		@Override
		public void askFreeOutput(int number) {

		}

		@Override
		public void showNicknameConfirmation(boolean nicknameAccepted) {

		}

		@Override
		public void showLobbyConfirmation(boolean lobbyAccessed) {

		}

		@Override
		public void showGenericMessage(String genericMessage) {

		}

		@Override
		public void disconnection(String text, boolean termination) {

		}

		@Override
		public void showError(String error) {

		}

		@Override
		public void showFaithTrack(String nickname, ReducedFaithTrack faithTrack) {

		}

		@Override
		public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {

		}

		@Override
		public void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {

		}

		@Override
		public void showMarket(ReducedMarket market) {

		}

		@Override
		public void showDepot(String nickname, ReducedWarehouseDepot depot) {

		}

		@Override
		public void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {

		}

		@Override
		public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {

		}

		@Override
		public void showStrongBox(String nickname, ReducedStrongbox strongbox) {

		}

		@Override
		public void showMatchInfo(ArrayList<String> players) {

		}

		@Override
		public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {

		}

		@Override
		public void showWinMessage(String winner, int points) {

		}

		@Override
		public void connectionError() {

		}

		@Override
		public void showToken(TokenType token, Colors color) {

		}
	}
}