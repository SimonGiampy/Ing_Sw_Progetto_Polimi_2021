package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ResourceDeckTest {
	
	@Test
	public void addResources() {
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles;
		marbles = new Marbles[]{Marbles.BLUE, Marbles.YELLOW, Marbles.WHITE, Marbles.GREY, Marbles.WHITE, Marbles.WHITE};
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.COIN);
		fromWhiteMarble2.add(Resources.SERVANT);
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		
		
		deck.setFromWhiteMarble(fromWhiteMarble1, 2);
		//deck.setFromWhiteMarble(fromWhiteMarble2, 2);
		
		deck.addResources(marbles, 0,1);
		
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