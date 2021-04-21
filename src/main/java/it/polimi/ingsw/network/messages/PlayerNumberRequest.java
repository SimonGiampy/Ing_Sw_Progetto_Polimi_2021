package it.polimi.ingsw.network.messages;

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
