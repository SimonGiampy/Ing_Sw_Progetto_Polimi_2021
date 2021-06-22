package it.polimi.ingsw.model.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListSetTest {
	
	ArrayList<Resources> subList;
	ArrayList<Resources> set;
	
	ArrayList<Resources> setWithSingleRes;
	
	/**
	 * instantiation of the list of resources to check the 2 functions
	 */
	@BeforeEach
	void setUp() {
		subList = new ArrayList<>();
		subList.add(Resources.STONE);
		subList.add(Resources.COIN);
		subList.add(Resources.STONE);
		subList.add(Resources.COIN);
		subList.add(Resources.SERVANT);
		
		set = new ArrayList<>();
		set.add(Resources.STONE);
		set.add(Resources.COIN);
		set.add(Resources.SHIELD);
		set.add(Resources.SERVANT);
		set.add(Resources.COIN);
		set.add(Resources.STONE);
		set.add(Resources.STONE);
		
		setWithSingleRes = new ArrayList<>();
		setWithSingleRes.add(Resources.COIN);
	}
	
	
	@Test
	void subset() {
		assertTrue(ListSet.subset(set, subList));
		
	}
	
	@Test
	void removeSubSet() {
		set = ListSet.removeSubSet(subList, set);
		assertTrue(set.isEmpty());
	}
}