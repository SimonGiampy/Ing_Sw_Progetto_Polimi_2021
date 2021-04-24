package it.polimi.ingsw.network.messages;

/**
 *  the client sends chosen action index to the server
 */
public class ActionReply extends Message{

	private final int selectedAction;


	public ActionReply(String nickname, int selectedAction) {
		super(nickname, MessageType.ACTION_REPLY);
		this.selectedAction = selectedAction;
	}

	public int getSelectedAction() {
		return selectedAction;
	}
}
