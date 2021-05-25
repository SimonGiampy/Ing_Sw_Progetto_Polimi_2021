package it.polimi.ingsw.network.messages.game.server2client;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

public class StartTurn extends Message {
	public StartTurn(String nickname){
		super(nickname, MessageType.START_TURN);
	}
}
