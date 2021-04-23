package it.polimi.ingsw.network.messages;

public class LobbyAccess extends Message {
	
	private int lobbyNumber;
	
	public int getLobbyNumber() {
		return lobbyNumber;
	}
	
	LobbyAccess(int lobbyNumber) {
		super("undefined", MessageType.LOBBY_ACCESS);
		this.lobbyNumber = lobbyNumber;
	}
}
