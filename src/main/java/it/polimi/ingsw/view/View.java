package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.reducedClasses.*;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;

public interface View {
	/**
	 * it asks how many players the host of the lobby wants
	 */
	void askNumberOfPlayer();

	/**
	 * it asks which nickname the player wants
	 */
	void askNickname();

	/**
	 * it asks to the host of the lobby which game configuration he wants
	 */
	void askCustomGame();

	/**
	 * it asks to all the player except the first which resources they wants at the start of the game
	 * @param number is the number of the resources that the player can choose
	 */
	void askInitResources(int number);

	/**
	 * it asks to all the player which leaders they wants
	 * @param leaderCards is a list of leaders
	 */
	void askInitLeaders(ArrayList<ReducedLeaderCard> leaderCards);

	/**
	 * it asks which leader action the player wants to do
	 * @param availableLeaders is a list of leaders
	 */
	void askLeaderAction(ArrayList<ReducedLeaderCard> availableLeaders);

	/**
	 * it asks which action the player wants to do
	 * @param availableAction is a list of available action
	 */
	void askAction(ArrayList<PlayerActions> availableAction);

	/**
	 * it asks what the player wants to do in the market
	 * @param market is the market
	 */
	void askMarketAction(ReducedMarket market);

	/**
	 * it asks which card the player want to buy
	 * @param cardsAvailable is a list of available card
	 */
	void askBuyCardAction(ArrayList<DevelopmentCard> cardsAvailable);

	/**
	 * TODO: to be fixed
	 * @param productionAvailable
	 */
	void askProductionAction(ArrayList<Integer> productionAvailable);

	/**
	 * it asks which free choice resources the player wants in input
	 * @param number is the number of free input resources
	 */
	void askFreeInput(int number);

	/**
	 * it asks which free choice resources the player wants in output
	 * @param number is the number of free output resources
	 */
	void askFreeOutput(int number);

	/**
	 * it shows if the nickname has been accepted
	 * @param nicknameAccepted is true if the nickname is valid
	 */
	void showNicknameConfirmation(boolean nicknameAccepted);

	/**
	 * it shows if the player has been admitted to the lobby
	 * @param lobbyAccessed is true if player has been admitted to the lobby
	 */
	void showLoginConfirmation(boolean lobbyAccessed);

	/**
	 * it shows a generic message
	 * @param genericMessage is the message to be shown
	 */
	void showGenericMessage(String genericMessage);

	/**
	 * it shows that a player hase logged out
	 * @param nicknameDisconnected is the nickname of the player
	 * @param text is a generic message to be shown
	 */
	void showDisconnectionMessage(String nicknameDisconnected, String text);

	/**
	 * it shows an error
	 * @param error is a generic error message
	 */
	void showError(String error);

	/**
	 * it shows player faith track
	 * @param faithTrack is player's faith track
	 */
	void showFaithTrack(ReducedFaithTrack faithTrack);

	/**
	 * it shows player depot and ask which combination the player wants
	 * @param depot is player's depot
	 * @param initialMove is true if it is the first message to be sent to the player
	 * @param confirmationAvailable is true if the confirmation is available
	 * @param inputValid is true if the previous input is valid
	 */
	void replyDepot(ReducedWarehouseDepot depot, boolean initialMove, boolean confirmationAvailable, boolean inputValid);

	/**
	 * it shows player leader cards
	 * @param availableLeaders is a list of leader cards
	 */
	void showLeaderCards(ArrayList<ReducedLeaderCard> availableLeaders);

	/**
	 * it shows the market
	 * @param market is the market
	 */
	void showMarket(ReducedMarket market);

	/**
	 * it shows the depot
	 * @param depot is the depot
	 */
	void showDepot(ReducedWarehouseDepot depot);

	/**
	 * it shows player's cards and productions
	 * @param cardProductionsManagement ?
	 */
	void showPlayerCardsAndProduction(ReducedCardProductionManagement cardProductionsManagement);

	/**
	 * it shows cards deck
	 * @param deck is deck
	 */
	void showCardsDeck(ReducedDevelopmentCardsDeck deck);

	/**
	 * it shows player's strongbox
	 * @param strongbox is the strongbox
	 */
	void showStrongBox(ReducedStrongbox strongbox);

	/**
	 * it shows match info
	 * @param players is a list of players
	 */
	void showMatchInfo(ArrayList<String> players);

	/**
	 * it shows a list of the lobbies on the server
	 * @param lobbyList is a list of lobbies
	 * @param idVersion is the version of the lobbyList sent
	 */
	void showLobbyList(ArrayList<String> lobbyList, int idVersion);

	/**
	 * it shows the winner of the game
	 * @param winner is the winner
	 */
	void showWinMessage(String winner);

	/**
	 *
	 */
	void connectionError();

}
