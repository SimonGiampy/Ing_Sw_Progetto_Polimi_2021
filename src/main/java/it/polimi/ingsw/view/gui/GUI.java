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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GUI extends ViewObservable implements View {
	
	private final Scene scene;
	private final Stage stage;
	private SceneController controller;
	private String currentFXML;
	private String playerNickname;
	private final NotificationHandler notificationHandler;

	/**
	 * constructor with UI parameters
	 * @param scene created by the app
	 * @param stage general stage
	 */
	public GUI(Scene scene, Stage stage) {
		this.scene = scene;
		this.stage = stage;
		currentFXML = "connection.fxml"; // initial window
		notificationHandler = new NotificationHandler();
	}
	
	/**
	 * changes the content and loads the fxml file
	 * @param fxml name of the file with its extension
	 */
	private void setRoot(String fxml) {
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
			if (nicknameAccepted) {
				playerNickname = nickname.getNickname();
				setRoot("nicknameAccepted.fxml");
			}
			else {
				nickname.setInvalid();
			}
		});
	}
	

	@Override
	public void showMatchInfo(ArrayList<String> players) {
		Platform.runLater(() -> {
			setRoot("tabs.fxml");
			PlayerTabs playerTabs = (PlayerTabs) controller;
			playerTabs.addAllObservers(observers);
			playerTabs.instantiateTabs(players, playerNickname);
		});

	}
	
	@Override
	public void askInitResources(int number) {
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " initial resources.");
			dialog.initOwner(stage);
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
		Platform.runLater(() -> {
			LeadersDialog dialog = new LeadersDialog(leaderCards);
			dialog.initOwner(stage);
			dialog.showAndWait().ifPresent(integers -> notifyObserver(obs -> obs.onUpdateInitLeaders(integers)));
		});
	}
	
	@Override
	public void askLeaderAction(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		/*
		Platform.runLater(() -> {
			PlayerTabs playerTabs = (PlayerTabs) controller;
			playerTabs.update(nickname);
		});
		*/
		
		//TODO: this method do the same thing of the following one
		showLeaderCards(nickname, availableLeaders);
	}
	
	@Override
	public void askAction(String nickname,ArrayList<PlayerActions> availableAction) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;

			/*
			PauseTransition pause = new PauseTransition(Duration.seconds(1));
			pause.setOnFinished(event ->
					playerTabs.commonBoard.setTextVisible(true));
			pause.play();
			PauseTransition pause2 = new PauseTransition(Duration.seconds(3));
			pause2.setOnFinished(event ->
					playerTabs.commonBoard.setTextVisible(false));
			pause2.play();

			 */
			playerTabs.updateActions(nickname,availableAction);
			if (availableAction.contains(PlayerActions.PRODUCTIONS)) {
				playerTabs.update(nickname,true);
			}
			if (availableAction.contains(PlayerActions.LEADER)) {
				notifyObserver(obs -> obs.onUpdateAction(PlayerActions.LEADER));
			}
			
		});
	}

	@Override
	public void startTurn(String nickname){
		Platform.runLater(() -> {
			PlayerTabs playerTabs = (PlayerTabs) controller;
			playerTabs.update(nickname);
		});
	}
	
	@Override
	public void askMarketAction(String nickname,ReducedMarket market) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname,false);
		});
	
	}
	
	@Override
	public void askWhiteMarbleChoice(ArrayList<Resources> fromWhiteMarble1, ArrayList<Resources> fromWhiteMarble2, int whiteMarblesInput1, int whiteMarblesInput2, int howMany) {
		Platform.runLater(() -> {
			WhiteMarblesDialog dialog = new WhiteMarblesDialog(fromWhiteMarble1, fromWhiteMarble2, howMany);
			dialog.initOwner(stage);
			dialog.showAndWait().ifPresent(x -> notifyObserver(obs -> obs.onUpdateWhiteMarbleChoice(x[0], x[1])));
		});
	}
	
	@Override
	public void askBuyCardAction(String nickname, ArrayList<DevelopmentCard> cardsAvailable, boolean wrongSlot) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(cardsAvailable,wrongSlot);
			playerTabs.update(nickname,false);

		});
	}
	
	@Override
	public void askProductionAction(String nickname,ArrayList<Productions> productionAvailable) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(false);
			playerTabs.updateProduction(nickname,productionAvailable);
		});
	
	}
	
	@Override
	public void askFreeInput(int number) {
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " resources for the production input.");
			dialog.initOwner(stage);
			freeChoice(dialog,1);
		});
	}
	
	@Override
	public void askFreeOutput(int number) {
		Platform.runLater(() -> {
			ResourcesDialog dialog = new ResourcesDialog(number, "Choose a total of " + number + " resources for the production output.");
			dialog.initOwner(stage);
			freeChoice(dialog,2);
		});
	}
	
	/**
	 * starts a free choices dialog and asks for a number of resources, updating the observer with the result in output
	 * @param dialog to be shown to the user asking for a list of resources
	 */
	private void freeChoice(ResourcesDialog dialog, int flag) {
		dialog.showAndWait().ifPresent(resources -> {
			ArrayList<Integer> numbers = getQuantitiesFromResources(resources);
			ArrayList<Resources> finalResources = resources.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
			notifyObserver(obs -> obs.onUpdateResourceChoice(finalResources, numbers,flag));
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
		Platform.runLater(() -> notificationHandler.addNotification(genericMessage, (AnchorPane) scene.getRoot()));
	}
	
	@Override
	public void disconnection(String text, boolean termination) {
	
	}
	
	@Override
	public void showError(String error) {
	
	}
	
	@Override
	public void showFaithTrack(String nickname, ReducedFaithTrack faithTrack) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname, faithTrack);
		});
	}
	
	@Override
	public void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.depotInteraction(depot, initialMove, confirmationAvailable, inputValid);
		});
	
	}
	
	@Override
	public void showLeaderCards(String nickname, ArrayList<ReducedLeaderCard> availableLeaders) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname, availableLeaders);
		});
	}
	
	@Override
	public void showMarket(ReducedMarket market) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(market);
		});
	}
	
	@Override
	public void showDepot(String nickname, ReducedWarehouseDepot depot) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname, depot);
		});
	}
	
	@Override
	public void showPlayerCardsAndProduction(String nickname, ReducedCardProductionManagement cardProductionsManagement) {
		//This is just the update of the card productions
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname, cardProductionsManagement);
		});
	}
	
	@Override
	public void showCardsDeck(ReducedDevelopmentCardsDeck deck) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(deck);
		});

	}
	
	@Override
	public void showStrongBox(String nickname, ReducedStrongbox strongbox) {
		// updates the player' strongbox
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(nickname, strongbox);
		});
	}

	@Override
	public void showWinMessage(String winner, int points) {
		Platform.runLater(() -> {
			setRoot("winner.fxml");
			Winner winnerController= (Winner) controller;
			if (!winner.contains(",")) {
				winnerController.setNickname(winner.toUpperCase());
				winnerController.setPoints("WITH "+points+" POINTS");
			}
			else{
				winnerController.setWinner("IT'S A DRAW!");
				winnerController.setNickname("THESE PLAYERS:\n{"+winner.toUpperCase()+"}");
				winnerController.setPoints("HAVE SCORED "+points+" POINTS");
			}
		});
	}
	
	@Override
	public void connectionError() {
	
	}
	
	@Override
	public void showToken(TokenType token, Colors color) {
		Platform.runLater(() -> {
			PlayerTabs playerTabs= (PlayerTabs) controller;
			playerTabs.update(token,color);
		});
	}
}
