package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server asks for the game configuration file path to the game host
 */
public class GameConfigRequest extends Message {

	public GameConfigRequest() {
		super("lobby", MessageType.GAME_CONFIG_REQUEST);
	}
}