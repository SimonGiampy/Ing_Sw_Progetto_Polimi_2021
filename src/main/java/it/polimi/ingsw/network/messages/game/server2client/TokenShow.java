package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class TokenShow extends Message {
	private final TokenType tokenType;
	private final Colors color;

	public TokenShow(TokenType tokenType,Colors color){
		super("Lobby", MessageType.TOKEN_SHOW);
		this.tokenType = tokenType;
		this.color=color;
	}

	public TokenType getTokenType() {
		return tokenType;
	}

	public Colors getColor() {
		return color;
	}

}
