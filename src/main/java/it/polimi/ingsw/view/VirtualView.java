package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.observer.Observer;

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
	public void askLobbyToJoin() {
	
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
		clientHandler.sendMessage(new GameConfigRequest());
	}

	@Override
	public void askInitResources(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 0));
	}

	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
		clientHandler.sendMessage(new LeaderShow(leaderCards, false));
	}

	@Override
	public void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders) {
		clientHandler.sendMessage(new LeaderShow(availableLeaders, true));
	}

	@Override
	public void askAction(ArrayList<Integer> availableAction) {
		clientHandler.sendMessage(new ActionRequest(availableAction));
	}

	@Override
	public void askMarketAction(ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow(market, 1));
	}

	@Override
	public void askDepotMove(ReducedWarehouseDepot depot) {
		clientHandler.sendMessage(new DepotShow(depot, 1));
	}

	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {
		clientHandler.sendMessage(new CardsShow(cardsAvailable));
	}

	@Override
	public void askProductionAction(ArrayList<Integer> productionAvailable) {
		clientHandler.sendMessage(new ProductionShow(productionAvailable));
	}

	@Override
	public void askFreeInput(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 1));
	}

	@Override
	public void askFreeOutput(int number) {
		clientHandler.sendMessage(new ResourceChoice(number, 2));
	}

	@Override
	public void showNicknameConfirmation(boolean nicknameConfirmation) {
		clientHandler.sendMessage(new NicknameConfirmation(nicknameConfirmation));
	}

	@Override
	public void showLoginConfirmation(boolean lobbyAccessed) {
		clientHandler.sendMessage(new LoginConfirmation(lobbyAccessed));
	}

	@Override
	public void showGenericMessage(String genericMessage) {
		clientHandler.sendMessage(new GenericMessage(genericMessage));
	}

	@Override
	public void showDisconnectionMessage(String nicknameDisconnected, String text) {
		clientHandler.sendMessage(new DisconnectionMessage(nicknameDisconnected,text));
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
		clientHandler.sendMessage(new DepotShow(depot, 0));
	}

	@Override
	public void showMarket(ReducedMarket market) {
		clientHandler.sendMessage(new MarketShow(market, 0));
	}

	@Override
	public void showLobby(ArrayList<String> players, int numPlayers) {
		clientHandler.sendMessage(new LobbyShow(players,numPlayers));
	}

	public void showLobbyList(ArrayList<String> lobbyList){
		clientHandler.sendMessage(new LobbyList(lobbyList));
	}

	@Override
	public void showMatchInfo(ArrayList<String> players, String activePlayer) {
		clientHandler.sendMessage(new MatchInfoShow(players,activePlayer));
	}

	@Override
	public void showWinMessage(String winner) {
		clientHandler.sendMessage(new WinMessage(winner));
	}

	public ClientHandler getClientHandler() {
		return clientHandler;
	}

}
