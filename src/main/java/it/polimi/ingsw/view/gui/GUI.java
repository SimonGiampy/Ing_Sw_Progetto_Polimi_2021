package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI extends ViewObservable implements View {
	
	private static Scene scene;
	private static SceneController controller;
	FXMLLoader fxmlLoader;
	
	public GUI(Scene scene) {
		this.scene = scene;
	}
	
	public void setRoot(String fxml) {
		FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml));
		scene.setUserData(fxmlLoader);
		try {
			scene.setRoot(fxmlLoader.load());
		} catch (IOException e) {
			Thread.currentThread().interrupt();
		}
		controller = fxmlLoader.getController();
	}
	
	@Override
	public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {
		LobbyAccess lobbyAccess;
		//lobbyAccess.addAllObservers(observers);
		//switcher.changeRootPane(observers, "lobby_access.fxml");
		//lobbyAccess = (LobbyAccess) switcher.getActiveController();
		setRoot("lobby_access.fxml");
		lobbyAccess = (LobbyAccess) controller;
		lobbyAccess.addAllObservers(observers);
		lobbyAccess.update(lobbyList,idVersion);
	}
	
	
	
	@Override
	public void askNumberOfPlayer() {
		System.out.println("num asked");
		setRoot("numberOfPlayers.fxml");
		//LobbyAccess lobbyAccess = (LobbyAccess) controller;
		//lobbyAccess.fieldNumPlayers.setVisible(true);



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
