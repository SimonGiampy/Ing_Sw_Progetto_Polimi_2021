package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class GUI extends ViewObservable implements View {
	
	private SceneSwitcher switcher;
	
	public GUI(SceneSwitcher switcher) {
		this.switcher = switcher;
	}
	
	@Override
	public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {
		LobbyAccess lobbyAccess = new LobbyAccess();
		lobbyAccess.addAllObservers(observers);
		//lobbyAccess.update(lobbyList);
		lobbyAccess.update(new ArrayList<>(Arrays.asList("madonna", "cagna")));
		switcher.changeRootPane(observers, "lobby_access.fxml");
	}
	
	@Override
	public void askNumberOfPlayer() {

	}
	
	@Override
	public void askNickname() {
	
	}
	
	@Override
	public void askCustomGame() {
	
	}
	
	@Override
	public void askInitResources(int number) {
	
	}
	
	@Override
	public void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards) {
	
	}
	
	@Override
	public void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders) {
	
	}
	
	@Override
	public void askAction(ArrayList<PlayerActions> availableAction) {
	
	}
	
	@Override
	public void askMarketAction(ReducedMarket market) {
	
	}
	
	@Override
	public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
	
	}
	
	@Override
	public void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
	
	}
	
	@Override
	public void askProductionAction(ArrayList<Productions> productionAvailable) {
	
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
	public void showFaithTrack(ReducedFaithTrack faithTrack) {
	
	}
	
	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
	
	}
	
	@Override
	public void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders) {
	
	}
	
	@Override
	public void showMarket(ReducedMarket market) {
	
	}
	
	@Override
	public void showDepot(ReducedWarehouseDepot depot) {
	
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
	public void showMatchInfo(ArrayList<String> players) {
	
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
