package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Marbles;
import it.polimi.ingsw.model.util.Resources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDeckTest {

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
		deck.setMarblesFromMarket(marbles);

		deck.setFromWhiteMarble(fromWhiteMarble1, 1);

		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(0, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(1, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(2, 0));
		assertDoesNotThrow(() -> deck.addResources(3, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(4, 0));

		ArrayList<Resources> correctOutput = new ArrayList<>();
		correctOutput.add(Resources.SHIELD);
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.STONE);
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.COIN);

		assertEquals(correctOutput, depot.getIncomingResources());
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
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.STONE);
		deck.setMarblesFromMarket(marbles);

		deck.setFromWhiteMarble(fromWhiteMarble1, 1);
		deck.setFromWhiteMarble(fromWhiteMarble2, 1);

		assertDoesNotThrow(() -> deck.addResources(2, 1));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(0, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(1, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(1, 1));
		assertDoesNotThrow(() -> deck.addResources(3, 0));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(2, 2));

		ArrayList<Resources> correctOutput = new ArrayList<>();
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.SERVANT);
		correctOutput.add(Resources.COIN);
		correctOutput.add(Resources.SERVANT);
		correctOutput.add(Resources.STONE);

		assertEquals(correctOutput, depot.getIncomingResources());
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
		deck.setMarblesFromMarket(marbles);

		deck.setFromWhiteMarble(fromWhiteMarble1, 3);
		deck.setFromWhiteMarble(fromWhiteMarble2, 2);

		assertDoesNotThrow(() -> deck.addResources(1, 0)); //correct input, doesn't throw any exception
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(2, 2));
		assertDoesNotThrow( () -> deck.addResources(0, 1));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(1, 1));
		assertThrows(InvalidUserRequestException.class, () -> deck.addResources(1, 2));
	}

	@Test
	public void setFromWhiteMarble() {
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);

		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.SERVANT);
		fromWhiteMarble1.add(Resources.COIN);
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.SHIELD);

		deck.setFromWhiteMarble(fromWhiteMarble1, 2);
		deck.setFromWhiteMarble(fromWhiteMarble2, 1);

		assertSame(deck.getFromWhiteMarble1(), fromWhiteMarble1);
		assertSame(deck.getFromWhiteMarble2(), fromWhiteMarble2);
		assertEquals(deck.getWhiteMarblesInput1(), 2);
		assertEquals(deck.getWhiteMarblesInput2(), 1);
	}

	@Test
	public void genericTest(){
		WarehouseDepot depot = new WarehouseDepot();
		ResourceDeck deck = new ResourceDeck(depot);

		deck.setFaithPoint(1);
		assertEquals(1, deck.getFaithPoint());
		ArrayList<Resources> fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.SERVANT);
		fromWhiteMarble1.add(Resources.COIN);
		ArrayList<Resources> fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.SHIELD);

		deck.setFromWhiteMarble(fromWhiteMarble1, 2);
		assertTrue(deck.isWhiteAbility1Activated());

		Marbles[] marbles = new Marbles[] {Marbles.YELLOW, Marbles.YELLOW, Marbles.WHITE, Marbles.PURPLE};
		deck.setMarblesFromMarket(marbles);
		assertEquals(0, deck.getRightNumberOfActivations());

		deck.setFromWhiteMarble(fromWhiteMarble2, 1);
		assertTrue(deck.isWhiteAbility2Activated());

		assertEquals(1, deck.getWhiteMarblesTaken());
	}

}