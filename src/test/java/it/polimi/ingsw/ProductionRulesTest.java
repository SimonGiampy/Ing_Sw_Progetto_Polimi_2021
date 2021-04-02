package it.polimi.ingsw;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ProductionRulesTest {

	@Test // inputProduction = {COIN,SHIELD} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,SHIELD,STONE} true
	public void IsProductionAvailable() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.SHIELD);
		playerResources.add(Resources.COIN);
		playerResources.add(Resources.SHIELD);
		playerResources.add(Resources.STONE);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertTrue(productionRules.isProductionAvailable(playerResources));
	}
	@Test // inputProduction = {COIN,SHIELD} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,STONE} false
	public void IsProductionAvailable2() {
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
	@Test // inputProduction = {COIN,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={COIN,STONE} true
	public void IsProductionAvailable3() {
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
	@Test // inputProduction = {COIN,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={} false
	public void IsProductionAvailable4() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.COIN);
		inputProduction.add(Resources.EMPTY);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertFalse(productionRules.isProductionAvailable(playerResources));
	}
	@Test // inputProduction = {EMPTY,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={} false
	public void IsProductionAvailable5() {
		ArrayList<Resources> inputProduction=new ArrayList<>();
		ArrayList<Resources> outputProduction=new ArrayList<>();
		ArrayList<Resources> playerResources= new ArrayList<>();
		inputProduction.add(Resources.EMPTY);
		inputProduction.add(Resources.EMPTY);
		ProductionRules productionRules = new ProductionRules(inputProduction,outputProduction,1);
		assertFalse(productionRules.isProductionAvailable(playerResources));
	}

	@Test // inputProduction = {EMPTY,EMPTY} outputProduction {STONE,STONE} faithOutput=1 playerResources={STONE,SHIELD} true
	public void IsProductionAvailable6() {
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




}