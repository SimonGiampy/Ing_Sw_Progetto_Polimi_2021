package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionRulesTest {

	/**
	 * it tests if a production with real input (not ?) and enough resources in player's boxes is available
	 * inputProduction = {COIN,COIN} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,COIN,STONE} return true
	 */
	@Test
	public void isProductionAvailable() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.COIN);
		playerResources.add(Resources.COIN);
		playerResources.add(Resources.COIN);
		playerResources.add(Resources.STONE);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertTrue(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 * it tests if a production with real input (not ?) and not enough resources in player's boxes is available
	 *  inputProduction = {COIN,SHIELD} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,STONE} return false
	 */
	@Test
	public void isProductionAvailable_2() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.SHIELD);
		playerResources.add(Resources.COIN);
		playerResources.add(Resources.STONE);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertFalse(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 * it tests if a production with mixed input (real resources and ?) and enough resources in player's boxes is available
	 * inputProduction = {COIN,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,STONE} return true
	 */
	@Test
	public void isProductionAvailable_3() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.EMPTY);
		playerResources.add(Resources.COIN);
		playerResources.add(Resources.STONE);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertTrue(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 * it tests if a production with mixed input (real resources and ?) and  not enough resources in player's boxes is available
	 * // inputProduction = {COIN,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={} return false
	 */
	@Test
	public void isProductionAvailable_4() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.EMPTY);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertFalse(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 * it tests if a production with mixed input (only ?) and enough resources in player's boxes is available
	 * inputProduction = {EMPTY,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={STONE,SHIELD} return true
	 */
	@Test
	public void isProductionAvailable_5() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.EMPTY);
		inputProduction.add(Resources.EMPTY);
		playerResources.add(Resources.STONE);
		playerResources.add(Resources.SHIELD);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertTrue(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 * it tests if a production with mixed input (only ?) and not enough resources in player's boxes is available
	 * inputProduction = {EMPTY,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={} return false
	 */
	@Test
	public void isProductionAvailable_6() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.EMPTY);
		inputProduction.add(Resources.EMPTY);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertFalse(productionRules.isProductionAvailable(playerResources));
	}

	/**
	 *
	 */
	@Test
	public void numberOfInputEmptyResources(){
		ArrayList<Resources> inputProduction= new ArrayList<>();
		ArrayList<Resources> outputProduction= new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.EMPTY);
		inputProduction.add(Resources.STONE);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertEquals(1,productionRules.numberOfInputEmptyResources());
	}
	@Test
	public void numberOfInputEmptyResources_2(){
		ArrayList<Resources> inputProduction= new ArrayList<>();
		ArrayList<Resources> outputProduction= new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertEquals(0,productionRules.numberOfInputEmptyResources());
	}
}