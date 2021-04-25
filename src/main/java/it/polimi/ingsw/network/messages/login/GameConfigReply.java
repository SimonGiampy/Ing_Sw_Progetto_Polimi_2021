package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the client replies with the game config file chosen (standard or custom)
 */
public class GameConfigReply extends Message {

	private final String gameConfiguration;


	public GameConfigReply(String nickname, String gameConfiguration) {
		super(nickname, MessageType.GAME_CONFIG_REPLY);
		this.gameConfiguration = gameConfiguration;
	}

	public String getGameConfiguration() {
		return gameConfiguration;
	}
}
