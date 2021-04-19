package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.util.Unicode;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * this class handles player's cards and productions management
 */
public class CardProductionsManagement {
	private final ArrayList<Stack<DevelopmentCard>> cards;
	private final Strongbox myStrongbox;
	private final WarehouseDepot myWarehouseDepot;
	private final boolean[] numberOfProduction;
	private final ProductionRules[] productions;

	/**
	 * default constructor
	 */
	public CardProductionsManagement(Strongbox playerStrongbox, WarehouseDepot playerWarehouseDepot, ProductionRules baseProductionRules){
		cards=new ArrayList<>();
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		myStrongbox = playerStrongbox;
		myWarehouseDepot = playerWarehouseDepot;
		numberOfProduction = new boolean[]{false,false,false,true,false,false};
		productions= new ProductionRules[6];
		productions[3]=baseProductionRules;

	}

	/** TODO: Testing?
	 * it adds a new leader card to available productions
	 * @param input is a list of required resources to activate production
	 * @param faithOutput is the number of faith points
	 */
	public void addLeaderCard(ArrayList<Resources> input, ArrayList<Resources> output, int faithOutput){
		if (!numberOfProduction[4]) {
			numberOfProduction[4] = true;
			productions[4]=new ProductionRules(input,output,faithOutput);
		}
		else {
			numberOfProduction[5] = true;
			productions[5]= new ProductionRules(input, output, faithOutput);
		}
	}


	/**
	 * it adds a new card on the selected stack
	 * @param newCard is the card to insert
	 * @param selectedStack is the number of the selected stack
	 */
	public void addCard(DevelopmentCard newCard,int selectedStack) {
		if(cards.get(selectedStack-1).size()==0)
			numberOfProduction[selectedStack-1]=true;
		cards.get(selectedStack-1).push(newCard);
		productions[selectedStack-1]=cards.get(selectedStack-1).peek().getProductionRules();
	}

	/**
	 * it shows card's information
 	 */
	public void showCards(){
		for (int i = 0; i < 3; i++) {
			if(cards.get(i).size()>0)
				cards.get(i).peek().showCard();
			else {
				String string = Unicode.TOP_LEFT +
						String.valueOf(Unicode.HORIZONTAL).repeat(26) +
						Unicode.TOP_RIGHT + "\n\n" +
						"  EMPTY\n  SLOT\n\n" +
						Unicode.BOTTOM_LEFT +
						String.valueOf(Unicode.HORIZONTAL).repeat(26) +
						Unicode.BOTTOM_RIGHT + "\n";
				System.out.println(string);
			}
		}
	}

	/** *
	 * it checks top card's level
	 * @param selectedStack is the number of the selected stack
	 * @return top card's level of the selected stack
	 */
	public int checkStackLevel(int selectedStack) {
		return  cards.get(selectedStack-1).size();
	}

	/**
	 * @return a list of player's cards requirement (color, level)
	 */
	public ArrayList<CardRequirement> getPlayerCardsRequirements(){
		ArrayList<CardRequirement> cardRequirements= new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < cards.get(i).size(); j++) {
				cardRequirements.add(new CardRequirement(cards.get(i).get(j).getColor(),cards.get(i).get(j).getLevel()));
			}
		}
		return cardRequirements;
	}

	/**
	 * it activates production
	 * @param selectedProduction is the number of the selected stack
	 */
	public ArrayList<Resources> activateSingleProduction(int selectedProduction){
		return productions[selectedProduction-1].produce();
	}

	/** TODO: Testing
	 * it activates all selected production
	 * @param playerInput is a list of selected production
	 * @param inputResources is a list of resources selected by the player
	 * @return the number of faith points of the selected productions
	 */
	public int activateSelectedProduction(ArrayList<Integer> playerInput,int[] inputResources){
		ArrayList<Resources> selectedProduction= new ArrayList<>();
		int totalFaithPoints=0;
		for (Integer integer : playerInput) {
			selectedProduction.addAll(activateSingleProduction(integer));
			totalFaithPoints=returnFaithPoints(integer);
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < inputResources[i]; j++) {
				selectedProduction.add(Resources.values()[i]);
			}
		}
		selectedProduction = selectedProduction.stream().filter(i -> i != Resources.FREE_CHOICE).collect(Collectors.toCollection(ArrayList::new));
		myStrongbox.storeResources(selectedProduction);
		return totalFaithPoints;
	}


	/**
	 * it returns the number of faith points
	 * @param selectedStack is the number of the selected stack
	 * @return the number of faith points
	 */
	public int returnFaithPoints(int selectedStack){
		return productions[selectedStack-1].getFaithOutput();
	}

	/**
	 * it calculates player's cards points
	 * @return the sum of all the cards points
	 */
	public int totalVictoryPoints(){
		return  cards.get(0).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
				cards.get(1).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
				cards.get(2).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum();
	}

	/**
	 *
	 * @param selectedStack is the number of the selected stack
	 * @return true if the card's production is available
	 */
	public boolean isSingleProductionAvailable(int selectedStack) {
		ArrayList<Resources> playerResources= myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		return productions[selectedStack-1].isProductionAvailable(playerResources);
	}

	/**
	 * it gets the selected production input
	 * @param selectedStack is the number of the selected stack
	 * @return an Arraylist of production's input
	 */
	public ArrayList<Resources> getProductionInput(int selectedStack){
		return productions[selectedStack-1].getInputCopy();
	}

	/**
	 * it checks if all selected productions (valid) are available
	 * @param playerInput is a list of selected production
	 * @return true if all productions can be started at the same time
	 */
	public boolean isSelectedProductionAvailable(ArrayList<Integer> playerInput){
		ArrayList<Resources> playerResources= myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		ArrayList<Resources> productionInput= new ArrayList<>();
		for (Integer integer : playerInput) {
			productionInput.addAll(getProductionInput(integer));
		}
		ProductionRules allSelectedProduction = new ProductionRules(productionInput, new ArrayList<>(),0);
		return allSelectedProduction.isProductionAvailable(playerResources);
	}

	/** TODO: Testing
	 * it takes resources from Depot and Strongbox
	 * @param playerInput is a list of selected production
	 * @param inputResources is an array of number of Resources [#COIN,#SERVANT,#SHIELD,#STONE]
	 */
	public void takeSelectedResources(ArrayList<Integer> playerInput, int[] inputResources) throws InvalidUserRequestException {
		ArrayList<Resources> playerResources= myWarehouseDepot.gatherAllResources();
		playerResources.addAll(myStrongbox.getContent());
		ArrayList<Resources> productionInput= new ArrayList<>();
		ArrayList<Resources> remainingResources;
		for (Integer integer : playerInput) {
			productionInput.addAll(getProductionInput(integer));
		}
		ArrayList<Resources> filteredProduction = productionInput.stream().filter(i->i!=Resources.FREE_CHOICE)
				.collect(Collectors.toCollection(ArrayList::new));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < inputResources[i]; j++) {
				filteredProduction.add(Resources.values()[i]);
			}
		}
		ProductionRules allSelectedProduction= new ProductionRules(filteredProduction,new ArrayList<>(),0);
		if (allSelectedProduction.isProductionAvailable(playerResources)){
			remainingResources = myWarehouseDepot.payResources(filteredProduction);
			myStrongbox.retrieveResources(remainingResources);
		}
		else throw new InvalidUserRequestException("invalid input resources");
	}

	/** TODO: Testing
	 * it checks if one of card's productions is available
	 * @return true if at least one of card's production is available
	 */
	public boolean isAtLeastOneProductionAvailable() throws InvalidInputException {
		return  (checkStackLevel(1)>0 && isSingleProductionAvailable(1)) ||
				(checkStackLevel(2)>0 && isSingleProductionAvailable(2)) ||
				(checkStackLevel(3)>0 && isSingleProductionAvailable(3)) ||
				isSingleProductionAvailable(4) ||
				(numberOfProduction[4] && isSingleProductionAvailable(5)) ||
				(numberOfProduction[5] && isSingleProductionAvailable(6));
	}

	/**
	 * it checks how many development cards the player owns
	 * @return the number of the cards
	 */
	public int numberOfCards(){
		return cards.get(0).size()+cards.get(1).size()+cards.get(2).size();
	}

	/**
	 * Checks if the input is correct (no values out of 1-6 and no duplicates) and selected production are activated
	 * @param playerInput number of the selected productions
	 * @return true if the input is correct and the productions available
	 */
	public boolean checkPlayerInput(ArrayList<Integer> playerInput){
		if (playerInput.size() != playerInput.stream().distinct().count() || playerInput.stream().anyMatch(i -> i>6 || i<1))
			return false;
		for (Integer integer : playerInput) {
			int input = integer - 1;
			if (!numberOfProduction[input])
				return false;
		}
		return true;
	}

	/**
	 * it calculates the number of input ? selected production
	 * @param selectedStack is the number of the selected production
	 * @return the number of ? in input
	 */
	public int numberOfInputEmptyResources(int selectedStack){
		return productions[selectedStack-1].numberOfInputEmptyResources();
	}

	/**
	 *  it calculates the number of input ? selected (by the player) productions
	 * @param playerInput is a list of production selected by the player
	 * @return the number of all ? in input
	 */
	public int numberOfInputEmptySelectedProduction(ArrayList<Integer> playerInput){
		int totalNumber=0;
		for (Integer integer : playerInput) {
			totalNumber = totalNumber + numberOfInputEmptyResources(integer);
		}
		return totalNumber;
	}

	/**
	 * it checks if the resources selected by the player to put in input are correct
	 * @param playerInput is a list of production selected by the player
	 * @param inputResources is an array of multiplicity of each resource selected by the player
	 * @return true if the sum of all resources selected by the player is equal to production's number of ? in input
	 */
	public boolean isNumberOfSelectedInputEmptyResourcesEnough(ArrayList<Integer> playerInput,int[] inputResources){
		int totalNumber= numberOfInputEmptySelectedProduction(playerInput);
		return totalNumber==inputResources[0]+inputResources[1]+inputResources[2]+inputResources[3];
	}

	/**
	 * it calculates the number of output ? selected production
	 * @param selectedStack is the number of the selected production
	 * @return the number of ? in output
	 */
	public int numberOfOutputEmptyResources(int selectedStack) {
		return productions[selectedStack-1].numberOfOutputEmptyResources();
	}

	/**
	 * it calculates the number of input ? selected (by the player) productions
	 * @param playerInput is a list of production selected by the player
	 * @return the number of all ? in input
	 */
	public int numberOfOutputEmptySelectedProduction(ArrayList<Integer> playerInput){
		int totalNumber=0;
		for (Integer integer : playerInput) {
			totalNumber = totalNumber + numberOfOutputEmptyResources(integer);
		}
		return totalNumber;
	}

	/**
	 * it checks if the resources selected by the player to put in output are correct
	 * @param playerInput is a list of production selected by the player
	 * @param outputResources is an array of multiplicity of each resource selected by the player
	 * @return true if the sum of all resources selected by the player is equal to production's number of ? in output
	 */
	public boolean isNumberOfSelectedOutputEmptyResourcesEnough(ArrayList<Integer> playerInput,int[] outputResources){
		int totalNumber=numberOfOutputEmptySelectedProduction(playerInput);
		return totalNumber == outputResources[0]+outputResources[1]+outputResources[2]+outputResources[3];
	}

	/**
	 * it shows selected production's information
	 * @param selectedStack is the number of selected production
	 */
	public void showSingleProduction(int selectedStack){
		productions[selectedStack-1].showProductionRulesInformation();
	}

	/**
	 *  it shows available productions
	 */
	public void showAvailableProductions(){
		System.out.println("Production Available:");
		for (int i = 0; i < 6; i++) {
			if(numberOfProduction[i] && isSingleProductionAvailable(i+1)){
				System.out.print(i+1+": ");
				System.out.println(Productions.values()[i]);
				showSingleProduction(i+1);
			}
		}
	}
}








