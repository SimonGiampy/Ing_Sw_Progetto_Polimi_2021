package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.DevelopmentCard;
import it.polimi.ingsw.model.Market;
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
import java.util.stream.Collectors;

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
		Platform.runLater(() -> {
			notifyObserver(obs->obs.onUpdateGameConfiguration("standard"));
		});
	}

	@Override
	public void showMatchInfo(ArrayList<String> players) {
		Platform.runLater(() -> {
			setRoot("tabs.fxml");
			PlayerTabs playerTabs = (PlayerTabs) controller;;
			playerTabs.addAllObservers(observers);
			playerTabs.instantiateTabs(players);

		});

	}
	
	@Override
	public void askInitResources(int number) {
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " initial resources.");
			dialog.showAndWait().ifPresent(resources -> {
				ArrayList<Integer> numbers = new ArrayList<>();
				if (number == 2) {
					if (resources.get(0).equals(resources.get(1))) {
						numbers.add(2);
					} else {
						numbers.add(1);
						numbers.add(1);
					}
				} else {
					numbers.add(1);
				}
				notifyObserver(obs -> obs.onUpdateResourceChoice(resources, numbers,0));
			});
		});
	}
	
	@Override
	public void askInitLeaders(String nickname, ArrayList<ReducedLeaderCard> leaderCards) {
		ArrayList<Integer> leaderSelection= new ArrayList<>();
		leaderSelection.add(1);
		leaderSelection.add(2);
		notifyObserver(obs-> obs.onUpdateInitLeaders(leaderSelection));
	}
	
	@Override
	public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
	
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
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " resources for the production input.");
			freeChoice(dialog);
		});
	}
	
	@Override
	public void askFreeOutput(int number) {
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " resources for the production output.");
			freeChoice(dialog);
		});
	}
	
	/**
	 * starts a free choices dialog and asks for a number of resources, updating the observer with the result in output
	 * @param dialog to be shown to the user asking for a list of resources
	 */
	private void freeChoice(ResourcesDialog dialog) {
		dialog.showAndWait().ifPresent(resources -> {
			ArrayList<Integer> numbers = getQuantitiesFromResources(resources);
			ArrayList<Resources> finalResources = resources.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
			notifyObserver(obs -> obs.onUpdateResourceChoice(finalResources, numbers,0));
		});
	}
	
	/**
	 * calculates the quantities for each resources
	 * @param resources a list of resources with duplicates
	 * @return the quantities of resources per single resource
	 */
	private ArrayList<Integer> getQuantitiesFromResources(ArrayList<Resources> resources) {
		ArrayList<Integer> num = new ArrayList<>();
		for (Resources r: Resources.values()) {
			if (!r.equals(Resources.EMPTY) && !r.equals(Resources.FREE_CHOICE)) {
				int x = ListSet.count(resources, r);
				if (x != 0) {
					num.add(x);
				}
			}
		}
		return num;
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
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.commonBoard.setMarket(market);
		});
	}
	
	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
	
	}
	
	@Override
	public void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement) {
	
	}
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.commonBoard.setDeck(deck);
		});




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
