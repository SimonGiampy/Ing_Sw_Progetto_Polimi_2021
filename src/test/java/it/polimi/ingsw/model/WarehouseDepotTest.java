package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.util.Resources;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class WarehouseDepotTest {

	
	/**
	 * tests the activation of the additional depots in the warehouse.
	 * Test successful
	 */
	@Test
	public void enableAdditionalDepot() {
		WarehouseDepot depot = new WarehouseDepot();
		
		ArrayList<Resources> addDepot1 = new ArrayList<>();
		addDepot1.add(Resources.COIN);
		addDepot1.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot1);
		ArrayList<Boolean> result = new ArrayList<>();
		result.add(false);
		result.add(false);
		assertEquals(depot.getExtraDepotContents().get(0), result);
		
		ArrayList<Resources> addDepot2 = new ArrayList<>();
		addDepot2.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot2);
		result.remove(0);
		assertEquals(result, depot.getExtraDepotContents().get(1));
	}
	
	/**
	 * tests the function to pay with resources for activating a production. Test Successful.
	 */
	@Test
	public void payResourcesTest() {
		WarehouseDepot depot = new WarehouseDepot();
		//incoming resources setup
		Resources[] list = new Resources[] {Resources.EMPTY, Resources.SHIELD, Resources.EMPTY,
					Resources.SHIELD, Resources.EMPTY, Resources.SERVANT};
		depot.setDepotForDebugging(list);
		
		//extra depot 1 with 2 resources and both slots occupied
		ArrayList<Resources> addDepot1 = new ArrayList<>();
		addDepot1.add(Resources.COIN);
		addDepot1.add(Resources.COIN);
		depot.enableAdditionalDepot(addDepot1);
		depot.getExtraDepotContents().get(0).set(0, true); //fills the slot
		depot.getExtraDepotContents().get(0).set(1, true); //fills the slot
		
		//extra depot 2 with 2 resources but the slot is not occupied
		ArrayList<Resources> addDepot2 = new ArrayList<>();
		addDepot2.add(Resources.STONE);
		addDepot2.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot2);
		depot.getExtraDepotContents().get(1).set(0, true); //fills the slot
		depot.getExtraDepotContents().get(1).set(1, true); //fills the slot
		
		
		ArrayList<Resources> price = new ArrayList<>();
		price.add(Resources.SHIELD); //payed from depot
		price.add(Resources.SERVANT); //payed from depot
		price.add(Resources.SERVANT); //not payed
		price.add(Resources.COIN); // payed from extra depot 1
		price.add(Resources.STONE); //payed from extra depot 2
		price.add(Resources.STONE); // payed from extra depot 2
		price.add(Resources.COIN); // payed from extra depot 1
		price.add(Resources.STONE); // not payed
		
		ArrayList<Resources> output = depot.payResources(price); // function call to be tested
		
		// computing expected output
		ArrayList<Resources> expectedResult = new ArrayList<>();
		expectedResult.add(Resources.SERVANT);
		expectedResult.add(Resources.STONE);
		
		assertEquals(expectedResult, output); // checks the function output
		
		Resources[] newWarehouse = new Resources[] {Resources.EMPTY, Resources.EMPTY, Resources.EMPTY,
				Resources.SHIELD, Resources.EMPTY, Resources.EMPTY};
		// checks remaining resources in the depot
		assertEquals(Arrays.asList(newWarehouse), Arrays.asList(depot.getDepot()));
		
		assertEquals(false, depot.getExtraDepotContents().get(0).get(0)); //extra depot 1, slot 0
		assertEquals(false, depot.getExtraDepotContents().get(0).get(1)); //extra depot 1, slot 1
		
		assertEquals(false, depot.getExtraDepotContents().get(1).get(0)); // extra depot 2, slot 0
		assertEquals(false, depot.getExtraDepotContents().get(1).get(1)); // extra depot 2, slot 0
		
		
	}
	
	/**
	 * this method is called after the user confirms that he can't or doesn't want to move any more
	 * resources to the warehouse depot. Test Successful.
	 */
	@Test
	public void discardResourcesAfterUserConfirmation() {
		WarehouseDepot depot = new WarehouseDepot();
		
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.COIN);
		list.add(Resources.SHIELD);
		list.add(Resources.SHIELD);
		list.add(Resources.STONE);
		depot.addIncomingResources(list);
		assertEquals(4, depot.discardResourcesAfterUserConfirmation());
	}
	
	
	/**
	 * adds some resources to the list of the incoming resources from the resource deck. Then sets up the
	 * additional depots, without filling them. Then moves the resources automatically to the additional depots
	 */
	@Test
	public void moveStuffFromResourceDeckToAdditionalDepotsAutomatically_1() {
		WarehouseDepot depot = new WarehouseDepot();
		
		//incoming resources setup
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.COIN);
		list.add(Resources.SHIELD);
		list.add(Resources.SHIELD);
		list.add(Resources.STONE);
		list.add(Resources.SERVANT);
		depot.addIncomingResources(list);
		
		//extra depot 1 with 2 resources and both slots occupied
		ArrayList<Resources> addDepot1 = new ArrayList<>();
		addDepot1.add(Resources.COIN);
		addDepot1.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot1);
		
		//extra depot 2 with 1 resource but the slot is not occupied
		ArrayList<Resources> addDepot2 = new ArrayList<>();
		addDepot2.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot2);
		
		depot.moveResourcesToAdditionalDepots(); // function to be tested
		
		//lists of expected values to check
		ArrayList<Boolean> expected1 = new ArrayList<>();
		expected1.add(true);
		expected1.add(true);
		ArrayList<Boolean> expected2 = new ArrayList<>();
		expected2.add(false);
		
		assertEquals(expected1, depot.getExtraDepotContents().get(0)); // first leader slots
		assertEquals(expected2, depot.getExtraDepotContents().get(1)); // second leader slots
		
	}
	
	/**
	 * adds some resources to the list of the incoming resources from the resource deck. Then sets up the
	 * additional depots, without filling them. Then moves the resources automatically to the additional depots
	 */
	@Test
	public void moveStuffFromResourceDeckToAdditionalDepotsAutomatically_2() {
		WarehouseDepot depot = new WarehouseDepot();
		
		//incoming resources setup
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.COIN);
		list.add(Resources.SHIELD);
		list.add(Resources.STONE);
		list.add(Resources.STONE);
		list.add(Resources.SERVANT);
		depot.addIncomingResources(list);
		
		//extra depot 1 with 2 resources and both slots will be empty
		ArrayList<Resources> addDepot1 = new ArrayList<>();
		addDepot1.add(Resources.COIN);
		addDepot1.add(Resources.COIN);
		depot.enableAdditionalDepot(addDepot1);
		
		//extra depot 2 with 1 resource a the slot will be occupied
		ArrayList<Resources> addDepot2 = new ArrayList<>();
		addDepot2.add(Resources.COIN);
		depot.enableAdditionalDepot(addDepot2);
		
		depot.moveResourcesToAdditionalDepots(); // function to be tested
		
		//lists of expected values to check
		ArrayList<Boolean> expected1 = new ArrayList<>();
		expected1.add(true);
		expected1.add(false);
		ArrayList<Boolean> expected2 = new ArrayList<>();
		expected2.add(false);
		
		assertEquals(expected1, depot.getExtraDepotContents().get(0)); // first leader slots
		assertEquals(expected2, depot.getExtraDepotContents().get(1)); // second leader slots
		
	}
	
	/**
	 * adds some resources to the list of the incoming resources from the resource deck. Then sets up the
	 * additional depots, without filling them. Then moves the resources automatically to the additional depots
	 */
	@Test
	public void moveStuffFromResourceDeckToAdditionalDepotsAutomatically_3() {
		WarehouseDepot depot = new WarehouseDepot();
		
		//incoming resources setup
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.COIN);
		list.add(Resources.SHIELD);
		list.add(Resources.SHIELD);
		list.add(Resources.STONE);
		list.add(Resources.SERVANT);
		depot.addIncomingResources(list);
		
		//extra depot 1 with 2 resources and both slots occupied
		ArrayList<Resources> addDepot1 = new ArrayList<>();
		addDepot1.add(Resources.COIN);
		addDepot1.add(Resources.SERVANT);
		depot.enableAdditionalDepot(addDepot1);
		
		//extra depot 2 with 1 resource but the slot is not occupied
		ArrayList<Resources> addDepot2 = new ArrayList<>();
		addDepot2.add(Resources.COIN);
		addDepot2.add(Resources.SERVANT);
		addDepot2.add(Resources.STONE);
		depot.enableAdditionalDepot(addDepot2);
		
		depot.moveResourcesToAdditionalDepots(); // function to be tested
		
		//lists of expected values to check
		ArrayList<Boolean> expected1 = new ArrayList<>();
		expected1.add(true);
		expected1.add(true);
		ArrayList<Boolean> expected2 = new ArrayList<>();
		expected2.add(false);
		expected2.add(false);
		expected2.add(true);
		
		assertEquals(expected1, depot.getExtraDepotContents().get(0)); // first leader slots
		assertEquals(expected2, depot.getExtraDepotContents().get(1)); // second leader slots
		
	}
	
	/**
	 * tests for various combinations of the warehouse depot (pyramid).
	 * Test successful.
	 */
	@Test
	public void isCombinationCorrect() {
		/* code for conversion from resource to integer
		switch (list[i]) {
				case COIN -> depotConverted[i] = 1;
				case STONE -> depotConverted[i] = 2;
				case SERVANT -> depotConverted[i] = 3;
				case SHIELD -> depotConverted[i] = 4;
				default -> depotConverted[i] = 0; // EMPTY
			}
		 */
		WarehouseDepot depot = new WarehouseDepot();
		
		// test1
		int[] test1 = new int[] {0,     2, 1,     0, 0, 2};
		assertFalse(depot.isCombinationCorrect(test1)); //false
		
		int[] test2 = new int[] {0,     1, 1,     1, 0, 0};
		assertFalse(depot.isCombinationCorrect(test2)); //false
		
		int[] test3 = new int[] {2,     0, 0,     1, 1, 2};
		assertFalse(depot.isCombinationCorrect(test3)); //false
		
		int[] test4 = new int[] {3,     1, 1,     2, 2, 2};
		assertTrue(depot.isCombinationCorrect(test4));  //true
		
		int[] test5 = new int[] {1,     0, 0,     0, 2, 0};
		assertTrue(depot.isCombinationCorrect(test5));  //true
		
		int[] test6 = new int[] {0,     0, 0,     0, 0, 0};
		assertTrue(depot.isCombinationCorrect(test6));  //true
		
		int[] test7 = new int[] {2,     0, 4,     0, 0, 4};
		assertFalse(depot.isCombinationCorrect(test7)); //false
		
		int[] test8 = new int[] {0,     0, 1,     3, 0, 3};
		assertTrue(depot.isCombinationCorrect(test8));  //true
		
		int[] test9 = new int[] {1,     2, 2,     4, 0, 0};
		assertTrue(depot.isCombinationCorrect(test9));  //true
	}
	
	@Test
	void getConvertedList() {
		WarehouseDepot w = new WarehouseDepot();
		Resources[] depot1 = new Resources[6];
		depot1[0] = Resources.EMPTY;
		depot1[1] = Resources.SERVANT;
		depot1[2] = Resources.SERVANT;
		depot1[3] = Resources.SHIELD;
		depot1[4] = Resources.COIN;
		depot1[5] = Resources.STONE;
		
		int[] result = new int[]{0, 3, 3, 4, 1, 2};
		
		assertArrayEquals(result, w.getConvertedList(depot1));
	}
	
	@Test
	void assignInitialResources() {
		WarehouseDepot w = new WarehouseDepot();
		w.assignInitialResources(Resources.COIN);
		
		Resources[] exp1 = new Resources[]{Resources.COIN, Resources.EMPTY, Resources.EMPTY, Resources.EMPTY, Resources.EMPTY, Resources.EMPTY};
		assertArrayEquals(exp1, w.getDepot());
		
		w.assignInitialResources(Resources.SHIELD, Resources.STONE);
		Resources[] exp2 = new Resources[]{Resources.SHIELD, Resources.STONE, Resources.EMPTY, Resources.EMPTY, Resources.EMPTY, Resources.EMPTY};
		assertArrayEquals(exp2, w.getDepot());
	}
	
	@Test
	void addIncomingResources() {
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.EMPTY);
		list.add(Resources.SHIELD);
		list.add(Resources.SHIELD);
		list.add(Resources.EMPTY);
		list.add(Resources.SERVANT);
		list.add(Resources.COIN);
		
		WarehouseDepot w = new WarehouseDepot();
		w.addIncomingResources(list);
		
		ArrayList<Resources> expected = new ArrayList<>();
		expected.add(Resources.EMPTY);
		expected.add(Resources.SHIELD);
		expected.add(Resources.SHIELD);
		expected.add(Resources.EMPTY);
		expected.add(Resources.SERVANT);
		expected.add(Resources.COIN);
		
		assertEquals(expected, w.getIncomingResources());
	}
	
	@Test
	void getAllResourcesWithEmptyAdditionalDepots() {
		WarehouseDepot depot = new WarehouseDepot();
		Resources[] res = new Resources[] {Resources.COIN, Resources.SHIELD, Resources.SHIELD,
				Resources.EMPTY, Resources.SERVANT, Resources.EMPTY};
		depot.setDepotForDebugging(res);
		
		// additional depots are left empty and untouched
		ArrayList<Resources> input = depot.gatherAllResources();
		//should return the same list that is present in the depot pyramid, with the exception of the empty resources
		ArrayList<Resources> expected = new ArrayList<>();
		expected.add(Resources.COIN);
		expected.add(Resources.SHIELD);
		expected.add(Resources.SHIELD);
		expected.add(Resources.SERVANT);
		assertEquals(expected, input);

	}
	
	@Test
	void moveResourcesToDepot() {
		WarehouseDepot depot = new WarehouseDepot();
		Resources[] res = new Resources[] {Resources.COIN, Resources.SHIELD, Resources.SHIELD,
				Resources.EMPTY, Resources.SERVANT, Resources.EMPTY};
		depot.setDepotForDebugging(res);
		try {
			depot.moveResourcesToDepot("depot", 1, 6);
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
		}
		List<Resources> expected = new ArrayList<>();
		expected.add(Resources.EMPTY);
		expected.add(Resources.SHIELD);
		expected.add(Resources.SHIELD);
		expected.add(Resources.EMPTY);
		expected.add(Resources.SERVANT);
		expected.add(Resources.COIN);
		assertEquals(expected, Arrays.asList(depot.getDepot()));
	}
	
	@Test
	void moveResourcesBackToDeck() {
		ArrayList<Resources> list = new ArrayList<>();
		list.add(Resources.SHIELD);
		list.add(Resources.SHIELD);
		list.add(Resources.COIN);
		
		WarehouseDepot depot = new WarehouseDepot();
		depot.addIncomingResources(list);
		
		try {
			depot.moveResourcesToDepot("deck", 2, 1);
			depot.moveResourcesBackToDeck(1);
		} catch (InvalidUserRequestException e) {
			e.printStackTrace();
		}
		ArrayList<Resources> list2 = new ArrayList<>();
		list2.add(Resources.SHIELD);
		list2.add(Resources.COIN);
		list2.add(Resources.SHIELD);
		
		assertEquals(list2, depot.getIncomingResources());
		
	}

	
}