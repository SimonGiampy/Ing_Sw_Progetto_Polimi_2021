package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the game host communicates the number of players to play with
 */
public class PlayerNumberReply extends Message {
	
	private final int playerNumber;

	public PlayerNumberReply(int playerNumber){
		super("Client", MessageType.PLAYER_NUMBER_REPLY);
		this.playerNumber = playerNumber;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}
	
}
