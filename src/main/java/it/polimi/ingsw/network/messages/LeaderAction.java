package it.polimi.ingsw.network.messages;

public class LeaderAction extends Message{

	private final int selectedLeader;
	private final int action; // 0: no action, 1: play leader, 2: discard leader

	public LeaderAction(String nickname, int selectedLeader, int action) {
		super(nickname, MessageType.LEADER_ACTION);
		this.selectedLeader = selectedLeader;
		this.action = action;
	}

	public int getSelectedLeader() {
		return selectedLeader;
	}

	public int getAction() {
		return action;
	}
}
