package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the server sends the winner of the game
 */
public class WinMessage extends Message {

	private final String winner;

	public WinMessage(String winner) {
		super("lobby", MessageType.WIN_MESSAGE);
		this.winner = winner;
	}

	public String getWinner() {
		return winner;
	}
}
