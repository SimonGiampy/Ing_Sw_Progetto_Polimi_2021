package it.polimi.ingsw.observers;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.PlayerActions;
import it.polimi.ingsw.model.util.Resources;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * implemented by ClientSideController. Methods are called whenever the client needs to update the server with its reply
 */
public interface ViewObserver {
	
	/**
	 * Create a new connection to the server with the updated info.
	 * @param serverInfo an hashmap of server address and server port.
	 */
	void onUpdateServerInfo(HashMap<String, String> serverInfo);

	/**
	 * it sends a message with to the server with the number of the lobby to access
	 * @param lobbyNumber number of the lobby
	 * @param idVersion the version of the lobby list received from the server
	 */
	void onUpdateLobbyAccess(int lobbyNumber, int idVersion);

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
	 * the client replies with the chosen game configuration (standard or custom path)
	 * @param gameConfiguration full path of the xml file (or "standard")
	 */
	void onUpdateGameConfiguration(String gameConfiguration);

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
	void onUpdateAction(PlayerActions selectedAction);

	/**
	 * it sends a message to the server with the row/column selected
	 * @param which is row or column
	 * @param where is the index of the row/column selected
	 */
	void onUpdateMarketAction(String which, int where);

	/**
	 * it sends the number of times of activation of every white marble leader
	 * @param quantity1 number of activations of first leader
	 * @param quantity2 number of activations of second leader
	 */
	void onUpdateWhiteMarbleChoice(int quantity1, int quantity2);
	/**
	 * it sends a message to the server with the movement of a resource
	 * @param fromWhere depot or deck. Indicates where to get the resources to be arranged
	 * @param toWhere depot or deck. Indicates the place where to place the resources
	 * @param origin positional number in the corresponding array of the place containing the resources
	 * @param destination positional number of the warehouse pyramid
	 * @param confirmation flag indicating if the player wants to confirm their resources positioning
	 */
	void onUpdateDepotMove(String fromWhere, String toWhere, int origin, int destination, boolean confirmation);

	/**
	 * it sends a message to the server with the bought card
	 * @param color is the color of the card
	 * @param level is the level of the card
	 */
	void onUpdateBuyCardAction(Colors color, int level, int slot);

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
	void onUpdateResourceChoice(ArrayList<Resources> resourcesList, ArrayList<Integer> resourcesNumber,int flag);

	/**
	 * it handles a disconnection wanted by the user
	 */
	void onDisconnection();
}
