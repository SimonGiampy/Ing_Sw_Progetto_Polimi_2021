package it.polimi.ingsw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListSetTest {
	
	ArrayList<Resources> subList;
	ArrayList<Resources> set;
	
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
	}
	
	@Test
	void subset() {
		//TODO: add asserts and javadoc here
		System.out.println("check: = " + ListSet.subset(set, subList));
		
	}
	
	@Test
	void removeSubSet() {
		//TODO: add asserts and javadoc here
		set = ListSet.removeSubSet(subList, set);
		System.out.println("remainder = " + set.toString());
	}
}