package it.polimi.ingsw.network.messages.login;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 * the client chooses the lobby to join, or creates a new one
 */
public class LobbyAccess extends Message {
	
	private final int lobbyNumber;
	private final int idVersion;
	
	public int getLobbyNumber() {
		return lobbyNumber;
	}
	
	public LobbyAccess(int lobbyNumber, int idVersion) {
		super("Undefined", MessageType.LOBBY_ACCESS);
		this.lobbyNumber = lobbyNumber;
		this.idVersion = idVersion;
	}

	public int getIdVersion() {
		return idVersion;
	}
}
