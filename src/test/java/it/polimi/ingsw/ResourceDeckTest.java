package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ResourceDeckTest {
	
	@Test
	public void addResources(){
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles;
		marbles = new Marbles[]{Marbles.BLUE, Marbles.YELLOW, Marbles.WHITE, Marbles.GREY, Marbles.WHITE, Marbles.WHITE};
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.COIN);
		fromWhiteMarble1.add(Resources.SERVANT);
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		

		deck.setFromWhiteMarble(fromWhiteMarble1, 1);
		//deck.setFromWhiteMarble(fromWhiteMarble2, 2);

		try {
			deck.addResources(marbles, 3, 0);
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
		}

		ArrayList<Resources> resourceList = deck.getResourceList();
		for(Resources r: resourceList)
			System.out.println(r);
	}
	
	@Test
	public void testAddResources() {
	}
	
	@Test
	public void setFromWhiteMarble() {
	}
}