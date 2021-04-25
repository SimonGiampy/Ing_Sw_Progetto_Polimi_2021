package it.polimi.ingsw.network.messages.game.client2server;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

/**
 *  the client sends chosen action index to the server
 */
public class ActionReply extends Message {

	private final int selectedAction;


	public ActionReply(String nickname, int selectedAction) {
		super(nickname, MessageType.ACTION_REPLY);
		this.selectedAction = selectedAction;
	}

	public int getSelectedAction() {
		return selectedAction;
	}
}
