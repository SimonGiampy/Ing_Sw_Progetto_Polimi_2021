package it.polimi.ingsw;

public class DiscardToken implements Token{

	private final int cardsNumber;
	private final Colors color;

	public DiscardToken(int cardsNumber, Colors color){
		this.cardsNumber = cardsNumber;
		this.color = color;
	}

	@Override
	public int getNumber() {
		return cardsNumber;
	}

	@Override
	public int getEffect(){
		return 2;
	}
}
