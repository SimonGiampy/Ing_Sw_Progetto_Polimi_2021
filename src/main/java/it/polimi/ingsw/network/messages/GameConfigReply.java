package it.polimi.ingsw.network.messages;

public class GameConfigReply extends Message{

	private final String gameConfiguration;


	public GameConfigReply(String nickname, String gameConfiguration) {
		super(nickname, MessageType.GAME_CONFIG_REPLY);
		this.gameConfiguration = gameConfiguration;
	}

	public String getGameConfiguration() {
		return gameConfiguration;
	}
}
