package it.polimi.ingsw.view;

import it.polimi.ingsw.*;
import it.polimi.ingsw.util.Resources;

import java.util.ArrayList;

public interface View {

	void askNickname();

	void askNumberOfPlayer();

	void askCustomGame();

	void askInitLeaders();

	void askLeaderAction(ArrayList<Resources> availableLeaders);

	void askAction(ArrayList<Integer> availableAction);

	void askMarketAction();

	void askDepotMove();

	void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable); // maybe this need a message too

	void askProductionAction(ArrayList<Integer> productionAvailable);

	void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

	void showGenericMessage(String genericMessage);

	void showDisconnectionMessage(String nicknameDisconnected, String text);

	void showError(String error);

	void showFaithTrack(FaithTrack faithTrack);

	void showDepot(WarehouseDepot depot);

	void showMarket(Market market);

	void showPlayerCardsAndProduction(CardProductionsManagement cardProductionsManagement);

	void showCardsDeck(DevelopmentCardsDeck deck);

	void showLobby(ArrayList<String> players, int numPlayers);

	void showMatchInfo(ArrayList<String> players, String activePlayer);

	void showWinMessage(String winner);

}
