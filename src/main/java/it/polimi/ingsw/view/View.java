package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

public interface View {

	void askNumberOfPlayer();
	
	void askNickname();

	void askCustomGame();

	void askInitLeaders();

	void askLeaderAction(ArrayList<Resources> availableLeaders);

	void askAction(ArrayList<Integer> availableAction);

	void askMarketAction();

	void askDepotMove();

	void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable);

	void askProductionAction(ArrayList<Integer> productionAvailable);

	void showLoginResult(boolean nicknameAccepted);

	void showGenericMessage(String genericMessage);

	void showDisconnectionMessage(String nicknameDisconnected, String text);

	void showError(String error);

	void showFaithTrack(ReducedFaithTrack faithTrack);

	void showDepot(ReducedWarehouseDepot depot);

	void showMarket(ReducedMarket market);

	void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement);

	void showCardsDeck(ReducedDevelopmentCardsDeck deck);

	void showStrongBox(ReducedStrongbox strongbox);

	void showLobby(ArrayList<String> players, int numPlayers);

	void showMatchInfo(ArrayList<String> players, String activePlayer);

	void showWinMessage(String winner);

}
