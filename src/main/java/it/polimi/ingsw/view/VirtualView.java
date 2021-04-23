package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observer;

import java.sql.Array;
import java.util.ArrayList;

public class VirtualView implements View, Observer {

	private final ClientHandler clientHandler;

	public VirtualView(ClientHandler clientHandler){
		this.clientHandler = clientHandler;
	}

	@Override
	public void update(Message message) {
		clientHandler.sendMessage(message);
	}

	@Override
	public void askNumberOfPlayer() {
		clientHandler.sendMessage(new PlayerNumberRequest());
	}
	
	@Override
	public void askNickname() {
		clientHandler.sendMessage(new NicknameRequest());
	}

	@Override
	public void askCustomGame() {

	}

	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
		clientHandler.sendMessage(new LeaderShow(leaderCards));
	}

	@Override
	public void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders) {
		clientHandler.sendMessage(new LeaderShow(availableLeaders));
	}

	@Override
	public void askAction(ArrayList<Integer> availableAction) {
	}

	@Override
	public void askMarketAction(ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow(market));
	}

	@Override
	public void askDepotMove(ReducedWarehouseDepot depot) {
		clientHandler.sendMessage(new DepotShow(depot));
	}

	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {
		clientHandler.sendMessage(new CardsShow(cardsAvailable));
	}

	@Override
	public void askProductionAction(ArrayList<Integer> productionAvailable) {
		//clientHandler.sendMessage(new Messaggio da fare (productionAvailable));
	}

	@Override
	public void showLoginResult(boolean nicknameAccepted) {
		clientHandler.sendMessage(new LoginReply(nicknameAccepted));
	}

	@Override
	public void showGenericMessage(String genericMessage) {

	}

	@Override
	public void showDisconnectionMessage(String nicknameDisconnected, String text) {

	}

	@Override
	public void showError(String error) {
		clientHandler.sendMessage(new ErrorMessage(error));
	}

	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {
		clientHandler.sendMessage(new PlayerCardsAndProductionShow(cardProductionsManagement));
	}

	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		clientHandler.sendMessage(new CardsDeckShow(deck));
	}

	@Override
	public void showStrongBox(ReducedStrongbox strongbox) {
		clientHandler.sendMessage(new StrongboxShow(strongbox));
	}

	@Override
	public void showFaithTrack(ReducedFaithTrack faithTrack) {
		clientHandler.sendMessage(new FaithTrackShow(faithTrack));
	}

	@Override
	public void showDepot(ReducedWarehouseDepot depot) {
		clientHandler.sendMessage(new DepotShow(depot));
	}

	@Override
	public void showMarket(ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow(market));
	}

	@Override
	public void showLobby(ArrayList<String> players, int numPlayers) {
	}

	@Override
	public void showMatchInfo(ArrayList<String> players, String activePlayer) {

	}

	@Override
	public void showWinMessage(String winner) {

	}

	public ClientHandler getClientHandler() {
		return clientHandler;
	}

}
