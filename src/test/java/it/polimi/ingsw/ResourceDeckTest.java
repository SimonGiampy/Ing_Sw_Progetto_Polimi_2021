package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ResourceDeckTest {
	
	/**
	 * creates an array of marbles from the market and passes it to the resource deck. Then converts the white marbles
	 * into new resources according to the ability white marbles
	 */
	@Test
	public void processWhiteMarblesFromMarket_1(){
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles;
		marbles = new Marbles[] {Marbles.BLUE, Marbles.YELLOW, Marbles.WHITE, Marbles.GREY, Marbles.WHITE, Marbles.WHITE};
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.COIN);
		fromWhiteMarble1.add(Resources.SERVANT);
		
		try {
			deck.setFromWhiteMarble(fromWhiteMarble1, 1);
			
			deck.addResources(marbles, 3, 0); //correct input, doesn't throw exception
			
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		
		ArrayList<Resources> resourceList = deck.getResourceList();
		for(Resources r: resourceList)
			System.out.println(r);
	}
	
	/**
	 * creates an array of marbles from the market and passes it to the resource deck. Then converts the white marbles
	 * into new resources according to the ability white marbles
	 */
	@Test
	public void processWhiteMarblesFromMarket_2(){
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles = new Marbles[] {Marbles.YELLOW, Marbles.YELLOW, Marbles.WHITE, Marbles.YELLOW, Marbles.WHITE, Marbles.WHITE};
		
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.SERVANT);
		fromWhiteMarble1.add(Resources.SERVANT);
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		
		try {
			deck.setFromWhiteMarble(fromWhiteMarble1, 1);
			deck.setFromWhiteMarble(fromWhiteMarble2, 2);
			
			deck.addResources(marbles, 1, 1); //correct input, doesn't throw exception
			
		} catch (InvalidInputException e) {
			e.printStackTrace();
		}
		
		ArrayList<Resources> resourceList = deck.getResourceList();
		for(Resources r: resourceList)
			System.out.println(r);
	}
	
	/**
	 * creates an array of marbles from the market and passes it to the resource deck. Then converts the white marbles
	 * into new resources according to the ability white marbles
	 */
	@Test
	public void processWhiteMarblesFromMarket_3(){
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);
		Marbles[] marbles = new Marbles[] {Marbles.YELLOW, Marbles.YELLOW, Marbles.WHITE, Marbles.YELLOW, Marbles.WHITE, Marbles.WHITE};
		
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.SERVANT);
		fromWhiteMarble1.add(Resources.SERVANT);
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		fromWhiteMarble2.add(Resources.STONE);
		
		try {
			deck.setFromWhiteMarble(fromWhiteMarble1, 3);
			deck.setFromWhiteMarble(fromWhiteMarble2, 2);
			
			deck.addResources(marbles, 1, 0); //correct input, doesn't throw any exception
			
		} catch (InvalidInputException e) {
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