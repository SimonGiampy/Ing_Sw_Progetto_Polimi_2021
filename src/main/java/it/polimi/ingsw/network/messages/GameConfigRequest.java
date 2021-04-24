package it.polimi.ingsw.network.messages;

public class GameConfigRequest extends Message{

	public GameConfigRequest() {
		super("lobby", MessageType.GAME_CONFIG_REQUEST);
	}
}
