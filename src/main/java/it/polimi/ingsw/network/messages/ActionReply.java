package it.polimi.ingsw.network.messages;

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
