package it.polimi.ingsw.network.messages;

public class WinMessage extends Message{

	private final String winner;

	public WinMessage(String winner) {
		super("lobby", MessageType.WIN_MESSAGE);
		this.winner = winner;
	}

	public String getWinner() {
		return winner;
	}
}
