package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.*;
import it.polimi.ingsw.observers.ViewObservable;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.scenes.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;

public class GUI extends ViewObservable implements View {
	
	private Scene scene;
	private SceneController controller;
	private String currentFXML;
	
	public GUI(Scene scene) {
		this.scene = scene;
		currentFXML = "connection.fxml"; // initial window
	}
	
	public void setRoot(String fxml) {
		FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml));
		//scene.setUserData(fxmlLoader); // this is most likely not needed
		currentFXML = fxml;
		try {
			scene.setRoot(fxmlLoader.load());
		} catch (IOException e) {
			Thread.currentThread().interrupt();
		}
		controller = fxmlLoader.getController();
	}
	
	@Override
	public void showLobbyList(ArrayList<String> lobbyList, int idVersion) {
		Platform.runLater(() -> {
			if (currentFXML.equals("lobby_access.fxml")) {
				LobbyAccess lobbyAccess = (LobbyAccess) controller;
				lobbyAccess.update(lobbyList,idVersion);
			} else {
				setRoot("lobby_access.fxml");
				LobbyAccess lobbyAccess = (LobbyAccess) controller;
				lobbyAccess.addAllObservers(observers);
				lobbyAccess.update(lobbyList,idVersion);
			}
		});
	}
	
	@Override
	public void showLobbyConfirmation(boolean lobbyAccessed) {
		// update scene only if the result is positive (the flag is true), otherwise shows a connection error
		Platform.runLater(() -> {
			if (!lobbyAccessed) {
				LobbyAccess lobbyAccess = (LobbyAccess) controller;
				lobbyAccess.setLobbyInvalid();
			}
		});
	}
	
	@Override
	public void askNumberOfPlayer() {
		Platform.runLater(() -> {
			setRoot("numberOfPlayers.fxml");
			NumberOfPlayers numberOfPlayers = (NumberOfPlayers) controller;
			numberOfPlayers.addAllObservers(observers);
		});
	}

	@Override
	public void askNickname() {
		Platform.runLater(() -> {
			if (!currentFXML.equals("nickname.fxml")) {
				setRoot("nickname.fxml");
				Nickname nickname = (Nickname) controller;
				nickname.addAllObservers(observers);
			}
		});
	}

	@Override
	public void showNicknameConfirmation(boolean nicknameAccepted) {
		Platform.runLater(() -> {
			Nickname nickname = (Nickname) controller;
			nickname.addAllObservers(observers);
			if (nicknameAccepted)
				setRoot("nicknameAccepted.fxml");
			else {
				nickname.setInvalid();
			}
		});
	}
	
	@Override
	public void askCustomGame() {
		notifyObserver(obs->obs.onUpdateGameConfiguration("standard"));
	}

	@Override
	public void showMatchInfo(ArrayList<String> players) {
		Platform.runLater(() -> {
			setRoot("tabs.fxml");
			PlayerTabs playerTabs = (PlayerTabs) controller;
			playerTabs.addAllObservers(observers);
			playerTabs.instantiateTabs(players);
		});

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
	public void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders) {
	
	}
	
	@Override
	public void showMarket(ReducedMarket market) {
	
	}
	
	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
	
	}
	
	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {
	
	}
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
	
	}
	
	@Override
	public void showStrongBox(String nickname, ReducedStrongbox strongbox) {
	
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
