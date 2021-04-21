package it.polimi.ingsw.observer;

import it.polimi.ingsw.model.util.Colors;

import java.util.ArrayList;

// implemented by Client Controller
// TODO: add CustomGame method

public interface ViewObserver {

	/**
	 * it sends a message to the server with the chosen nickname
	 * @param nickname is the nickname to be sent
	 */
	void onUpdateNickname(String nickname);

	/**
	 * it sends a message to the server with the chosen number of players
	 * @param playerNumber is the number of players
	 */
	void onUpdatePlayersNumber(int playerNumber);

	/**
	 * it sends a message to the server with the initial leaders selected by the player
	 * @param selectedLeaders is an arraylist of index of the selected leaders
	 */
	void onUpdateInitLeaders(ArrayList<Integer> selectedLeaders);

	/**
	 * it sends a message to the server with the leaders selected for action
	 * @param selectedLeaders is an arraylist of index of the selected leaders
	 */
	void onUpdateLeaderAction(ArrayList<Integer> selectedLeaders);

	/**
	 * it sends a message to the server with the selected action
	 * @param selectedAction is the index of the selected action
	 */
	void onUpdateAction(int selectedAction);

	/**
	 * it sends a message to the server with the row/column selected
	 * @param which is row or column
	 * @param where is the index of the row/column selected
	 */
	void onUpdateMarketAction(String which, int where);

	/**
	 *
	 */
	void onUpdateDepotMove(); // TODO: incomplete

	/**
	 * it sends a message to the server with the bought card
	 * @param color is the color of the card
	 * @param level is the level of the card
	 */
	void onUpdateBuyCardAction(Colors color, int level);

	/**
	 * it sends a message to the server with the selected production input
	 * @param selectedProduction is an arraylist of index of the selected production
	 */
	void onUpdateProductionAction(ArrayList<Integer> selectedProduction);

	/**
	 * it handles a disconnection wanted by the user
	 */
	void onDisconnection();
}
