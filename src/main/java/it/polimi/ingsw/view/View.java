package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

public interface View {
	
	void askNumberOfPlayer();
	
	void askNickname();

	void askCustomGame();

	void askInitResources(int number);

	void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards);

	void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders);

	void askAction(ArrayList<PlayerActions> availableAction);

	void askMarketAction(ReducedMarket market);

	void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable);

	void askProductionAction(ArrayList<Integer> productionAvailable);

	void askFreeInput(int number);

	void askFreeOutput(int number);

	void showNicknameConfirmation(boolean nicknameAccepted);

	void showLoginConfirmation(boolean lobbyAccessed);

	void showGenericMessage(String genericMessage);

	void showDisconnectionMessage(String nicknameDisconnected, String text);

	void showError(String error);

	void showFaithTrack(ReducedFaithTrack faithTrack);
	
	void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid);

	void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders);

	void showMarket(ReducedMarket market);
	
	void showDepot(ReducedWarehouseDepot depot);

	void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement);

	void showCardsDeck(ReducedDevelopmentCardsDeck deck);

	void showStrongBox(ReducedStrongbox strongbox);

	void showMatchInfo(ArrayList<String> players);

	void showLobbyList(ArrayList<String> lobbyList);

	void showWinMessage(String winner);

	void connectionError();

}
