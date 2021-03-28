package it.polimi.ingsw;

public class BlackCrossToken implements Token{

	private final int tileNumber;

	protected BlackCrossToken(int tileNumber){

		this.tileNumber = tileNumber;

	}

	@Override
	public int getNumber(){
		return tileNumber;
	}

	@Override
	public int getEffect(){
		return 1;
	}
}
