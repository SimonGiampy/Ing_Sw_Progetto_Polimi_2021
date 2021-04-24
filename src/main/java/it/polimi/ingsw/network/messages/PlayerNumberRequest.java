package it.polimi.ingsw.network.messages;

/**
 * the server asks the creator of the lobby how many players to put in the lobby
 */
public class PlayerNumberRequest extends Message {
	
	public PlayerNumberRequest(){
		super("server", MessageType.PLAYER_NUMBER_REQUEST);
	}

	@Override
	public String toString(){
		return "PlayerNumberRequest{ "+
				"nickname: "+ getNickname()+
				"  }";
	}
}
