package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observers.Observer;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class OfflineVirtualView extends VirtualView implements View, Observer {
	View view;

	public OfflineVirtualView(View view) {
		super(null);
		this.view=view;
	}

	@Override
	public void showMatchInfo(ArrayList<String> players) {
		view.showMatchInfo(players);
	}


	@Override
	public void update(Message message) {
		//super.update(message);
	}


	@Override
	public void askInitLeaders(String nickname, ArrayList<ReducedLeaderCard> leaderCards) {
		view.askInitLeaders(nickname,leaderCards);
	}

	@Override
	public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		view.askLeaderAction(nickname,availableLeaders);
	}

	@Override
	public void askAction(String nickname, ArrayList<PlayerActions> availableAction) {
		view.askAction(nickname,availableAction);
	}

	@Override
	public void askMarketAction(String nickname, ReducedMarket market) {
		view.askMarketAction(nickname,market);
	}

	@Override
	public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
		view.askWhiteMarbleChoice(fromWhiteMarble1,fromWhiteMarble2,whiteMarblesInput1,whiteMarblesInput2,howMany);
	}

	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		view.replyDepot(depot,initialMove,confirmationAvailable,inputValid);
	}

	@Override
	public void askBuyCardAction(String nickname, ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
		view.askBuyCardAction(nickname, cardsAvailable, wrongSlot);
	}

	@Override
	public void askProductionAction(String nickname, ArrayList<Productions> productionAvailable) {
		view.askProductionAction(nickname, productionAvailable);
	}

	@Override
	public void askFreeInput(int number) {
		view.askFreeInput(number);
	}

	@Override
	public void askFreeOutput(int number) {
		view.askFreeOutput(number);
	}

	@Override
	public void showNicknameConfirmation(boolean nicknameConfirmation) {
		view.showNicknameConfirmation(nicknameConfirmation);
	}

	@Override
	public void showLobbyConfirmation(boolean lobbyAccessed) {
		view.showLobbyConfirmation(lobbyAccessed);
	}

	@Override
	public void showGenericMessage(String genericMessage) {
		view.showGenericMessage(genericMessage);
	}

	@Override
	public void disconnection(String text, boolean termination) {
		view.disconnection(text, termination);
	}

	@Override
	public void showError(String error) {
		view.showError(error);
	}

	@Override
	public void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {
		view.showPlayerCardsAndProduction(nickname, cardProductionsManagement);
	}

	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		view.showCardsDeck(deck);
	}

	@Override
	public void showStrongBox(String nickname, ReducedStrongbox strongbox) {
		view.showStrongBox(nickname, strongbox);
	}

	@Override
	public void showFaithTrack(String nickname, ReducedFaithTrack faithTrack) {
		view.showFaithTrack(nickname, faithTrack);
	}

	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
		view.showDepot(nickname, depot);
	}

	@Override
	public void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		view.showLeaderCards(nickname, availableLeaders);
	}

	@Override
	public void showMarket(ReducedMarket market) {
		view.showMarket(market);
	}

	@Override
	public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {
		view.showLobbyList(lobbyList, idVersion);
	}

	@Override
	public void showWinMessage(String winner, int points) {
		view.showWinMessage(winner, points);
	}

	@Override
	public void showToken(TokenType token, Colors color) {
		view.showToken(token, color);
	}

	@Override
	public void connectionError() {
		view.connectionError();
	}
}
