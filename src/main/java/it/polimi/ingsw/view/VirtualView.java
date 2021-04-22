package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Resources;
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
	public void askInitLeaders() {

	}

	@Override
	public void askLeaderAction(ArrayList<Resources> availableLeaders) {

	}

	@Override
	public void askAction(ArrayList<Integer> availableAction) {

	}

	@Override
	public void askMarketAction() {
	}

	@Override
	public void askDepotMove() {

	}

	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable) {

	}

	@Override
	public void askProductionAction(ArrayList<Integer> productionAvailable) {

	}

	@Override
	public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {

	}

	@Override
	public void showGenericMessage(String genericMessage) {

	}

	@Override
	public void showDisconnectionMessage(String nicknameDisconnected, String text) {

	}

	@Override
	public void showError(String error) {

	}

	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {

	}

	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {

	}

	@Override
	public void showStrongBox(ReducedStrongbox strongbox) {

	}

	@Override
	public void showFaithTrack(ReducedFaithTrack faithTrack) {

	}

	@Override
	public void showDepot(ReducedWarehouseDepot depot) {

	}

	@Override
	public void showMarket(ReducedMarket market) {

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
