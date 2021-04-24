package it.polimi.ingsw.network.messages;

/**
 * the client replies with the chosen nickname
 */
public class NicknameReply extends Message {
	
	private final String nicknameProposal;

	public NicknameReply(String nicknameProposal) {
		super("Client", MessageType.NICKNAME_REPLY);
		this.nicknameProposal = nicknameProposal;
	}
	
	public String getNicknameProposal() {
		return this.nicknameProposal;
	}

	
}
