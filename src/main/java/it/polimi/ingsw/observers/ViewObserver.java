package it.polimi.ingsw.observers;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;
import java.util.HashMap;

// implemented by Client Controller
// TODO: add CustomGame method

public interface ViewObserver {
	
	/**
	 * Create a new connection to the server with the updated info.
	 *
	 * @param serverInfo a map of server address and server port.
	 */
	void onUpdateServerInfo(HashMap<String, String> serverInfo);

	/**
	 * it sends a message with to the server with the number of the lobby to access
	 * @param lobbyNumber number of the lobby
	 */
	void onUpdateLobbyAccess(int lobbyNumber);

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
	 * @param selectedLeader is the index of the selected leader
	 * @param action play, discard or nothing
	 */
	void onUpdateLeaderAction(int selectedLeader, int action);

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
	 * it sends a message to the server with the movement of a resource
	 * @param where depot or deck. Indicates the place where to move the resources from
	 * @param from positional number in the corresponding array of the place containing the resources
	 * @param destination positional number of the warehouse pyramid
	 */
	void onUpdateDepotMove(String where, int from, int destination);

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
	 * it sends a message to the server with the selected free choice resources
	 * @param resourcesList list of the resources
	 * @param resourcesNumber how much of each one
	 */
	void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber);

	/**
	 * it handles a disconnection wanted by the user
	 */
	void onDisconnection();
}
