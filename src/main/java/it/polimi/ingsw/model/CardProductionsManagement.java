package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Productions;
import it.polimi.ingsw.model.util.Resources;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * this class handles player's cards and productions management
 */
public class CardProductionsManagement {
	private final ArrayList<Stack<DevelopmentCard>> cards;
	private final Strongbox myStrongbox;
	private final WarehouseDepot myWarehouseDepot;
	private final HashMap<Productions, Boolean> presentProductions;
	private final HashMap<Productions, ProductionRules> productions;
	private ArrayList<Productions> selectedInput;
	private int[] inputResources;

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
		
		
		presentProductions = new HashMap<>(6);
		presentProductions.put(Productions.BASE_PRODUCTION, true);
		presentProductions.put(Productions.STACK_1_CARD_PRODUCTION, false);
		presentProductions.put(Productions.STACK_2_CARD_PRODUCTION, false);
		presentProductions.put(Productions.STACK_3_CARD_PRODUCTION, false);
		presentProductions.put(Productions.LEADER_CARD_1_PRODUCTION, false);
		presentProductions.put(Productions.LEADER_CARD_2_PRODUCTION, false);
		
		productions = new HashMap<>(6);
		productions.put(Productions.BASE_PRODUCTION, baseProductionRules);
		
	}

	/**
	 * it adds a new leader card to available productions
	 * @param input is a list of required resources to activate production
	 * @param faithOutput is the number of faith points
	 */
	public void addLeaderCard(ArrayList<Resources> input, ArrayList<Resources> output, int faithOutput){
		if (!presentProductions.get(Productions.LEADER_CARD_1_PRODUCTION)) { // activates first leader
			presentProductions.put(Productions.LEADER_CARD_1_PRODUCTION, true);
			productions.put(Productions.LEADER_CARD_1_PRODUCTION, new ProductionRules(input,output,faithOutput));
		} else { // activates second leader
			presentProductions.put(Productions.LEADER_CARD_2_PRODUCTION, true);
			productions.put(Productions.LEADER_CARD_2_PRODUCTION, new ProductionRules(input, output, faithOutput));
		}
	}
	

	/**
	 * it adds a new card on the selected stack (1, 2 or 3)
	 * @param newCard is the card to insert
	 * @param selectedStack is the number of the selected stack
	 */
	public void addCard(DevelopmentCard newCard, int selectedStack) {
		Productions prod = Productions.values()[selectedStack - 1];
		if (cards.get(selectedStack-1).size() == 0) {
			presentProductions.put(prod, true);
		}
		
		cards.get(selectedStack-1).push(newCard);
		productions.put(prod, cards.get(selectedStack-1).peek().getProductionRules());
	}
	

	/** *
	 * it checks top card's level
	 * @param selectedStack is the number of the selected stack
	 * @return top card's level of the selected stack
	 */
	public int checkStackLevel(int selectedStack) {
		return cards.get(selectedStack-1).size();
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
	 * it activates the chosen production
	 * @param selectedProduction is the number of the selected stack
	 */
	public ArrayList<Resources> activateSingleProduction(Productions selectedProduction){
		return productions.get(selectedProduction).produce();
	}

	/**
	 * it activates all the productions selected by the player
	 * @param playerInput is a list of selected productions
	 * @param outputResources is a list of resources selected by the player
	 * @return the number of faith points of the selected productions
	 */
	public int activateSelectedProduction(ArrayList<Productions> playerInput, int[] outputResources){
		ArrayList<Resources> selectedProduction= new ArrayList<>();
		int totalFaithPoints = 0;
		for (Productions prod : playerInput) {
			selectedProduction.addAll(activateSingleProduction(prod));
			totalFaithPoints = returnFaithPoints(prod);
		}
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < outputResources[i]; j++) {
				selectedProduction.add(Resources.values()[i]);
			}
		}
		selectedProduction = selectedProduction.stream().filter(i -> i != Resources.FREE_CHOICE).collect(Collectors.toCollection(ArrayList::new));
		myStrongbox.storeResources(selectedProduction);
		return totalFaithPoints;
	}


	/**
	 * it returns the number of faith points from a production
	 * @param prod production
	 * @return the number of faith points
	 */
	public int returnFaithPoints(Productions prod){
		return productions.get(prod).getFaithOutput();
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
	 * checks if the selected production is available
	 * @param prod production
	 * @return true if the card's production is available
	 */
	public boolean isSingleProductionAvailable(Productions prod) {
		ArrayList<Resources> playerResources = myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		return productions.get(prod).isProductionAvailable(playerResources);
	}

	/**
	 * it gets the selected production input
	 * @param p is the production corresponding to the selected card stack
	 * @return an Arraylist of production's input
	 */
	public ArrayList<Resources> getProductionInput(Productions p) {
		return productions.get(p).getInputCopy();
	}

	/**
	 * it checks if all selected productions (valid) are available
	 * @param playerInput is a list of selected productions
	 * @return true if all productions can be started at the same time
	 */
	public boolean isSelectedProductionAvailable(ArrayList<Productions> playerInput){
		ArrayList<Resources> playerResources= myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		ArrayList<Resources> productionInput = new ArrayList<>();
		for (Productions prod : playerInput) {
			productionInput.addAll(getProductionInput(prod));
		}
		ProductionRules allSelectedProduction = new ProductionRules(productionInput, new ArrayList<>(),0);
		return allSelectedProduction.isProductionAvailable(playerResources);
	}

	/**
	 * it takes resources from Depot and Strongbox
	 * @param playerInput is a list of selected production
	 * @param inputResources is an array of number of Resources [#COIN,#SERVANT,#SHIELD,#STONE]
	 */
	public void takeSelectedResources(ArrayList<Productions> playerInput, int[] inputResources)  {
		ArrayList<Resources> playerResources= myWarehouseDepot.gatherAllResources();
		playerResources.addAll(myStrongbox.getContent());
		ArrayList<Resources> remainingResources;
		ArrayList<Resources> filteredProduction = filteredProduction(playerInput, inputResources);
		remainingResources = myWarehouseDepot.payResources(filteredProduction);
		myStrongbox.retrieveResources(remainingResources);
	}
	
	/**
	 * filters out the free choices from the list of productions chosen by the player
	 * @param playerInput list of productions
	 * @param inputResources resources going to the strongbox
	 * @return the result of the filter
	 */
	private ArrayList<Resources> filteredProduction(ArrayList<Productions> playerInput, int[] inputResources){
		ArrayList<Resources> playerResources = myWarehouseDepot.gatherAllResources();
		playerResources.addAll(myStrongbox.getContent());
		ArrayList<Resources> productionInput = new ArrayList<>();
		for (Productions prod : playerInput) {
			productionInput.addAll(getProductionInput(prod));
		}
		ArrayList<Resources> filteredProduction = productionInput.stream().filter(i -> i != Resources.FREE_CHOICE)
				.collect(Collectors.toCollection(ArrayList::new));
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < inputResources[i]; j++) {
				filteredProduction.add(Resources.values()[i]);
			}
		}
		return filteredProduction;
	}

	/**
	 * checks if the resources chosen by the player are valid
	 * @param playerInput productions to activate
	 * @param inputResources resources chosen
	 * @return true if the input is valid
	 */
	public boolean checkFreeInput(ArrayList<Productions> playerInput, int[] inputResources){
		ArrayList<Resources> playerResources = myWarehouseDepot.gatherAllResources();
		playerResources.addAll(myStrongbox.getContent());

		ArrayList<Resources> filteredProduction = filteredProduction(playerInput, inputResources);
		ProductionRules allSelectedProduction= new ProductionRules(filteredProduction,new ArrayList<>(),0);

		return allSelectedProduction.isProductionAvailable(playerResources);
	}

	/**
	 * it checks if one of card's productions is available
	 * @return true if at least one of card's production is available
	 */
	public boolean isAtLeastOneProductionAvailable() {
		return  (checkStackLevel(1) >0 && isSingleProductionAvailable(Productions.STACK_1_CARD_PRODUCTION)) ||
				(checkStackLevel(2) >0 && isSingleProductionAvailable(Productions.STACK_2_CARD_PRODUCTION)) ||
				(checkStackLevel(3) >0 && isSingleProductionAvailable(Productions.STACK_3_CARD_PRODUCTION)) ||
				isSingleProductionAvailable(Productions.BASE_PRODUCTION) ||
				(presentProductions.get(Productions.LEADER_CARD_1_PRODUCTION) &&
						isSingleProductionAvailable(Productions.LEADER_CARD_1_PRODUCTION)) ||
				(presentProductions.get(Productions.LEADER_CARD_2_PRODUCTION)
						&& isSingleProductionAvailable(Productions.LEADER_CARD_2_PRODUCTION));
	}

	/**
	 * it checks how many development cards the player owns
	 * @return the number of the cards
	 */
	public int numberOfCards(){
		return cards.get(0).size() + cards.get(1).size() + cards.get(2).size();
	}
	

	/**
	 *  it calculates the number of input ? (free choices) selected (by the player) productions
	 * @param playerInput is a list of production selected by the player
	 * @return the number of all ? in input
	 */
	public int numberOfFreeChoicesInInputProductions(ArrayList<Productions> playerInput){
		int totalNumber = 0;
		for (Productions prod : playerInput) {
			totalNumber = totalNumber +  productions.get(prod).numberOfFreeChoicesInput();
		}
		return totalNumber;
	}
	
	/**
	 *  it calculates the number of input ? (free choices) selected (by the player) productions
	 * @param playerInput is a list of production selected by the player
	 * @return the number of all ? in input
	 */
	public int numberOfFreeChoicesInOutputProductions(ArrayList<Productions> playerInput){
		int totalNumber = 0;
		for (Productions prod : playerInput) {
			totalNumber = totalNumber +  productions.get(prod).numberOfFreeChoicesOutput();
		}
		return totalNumber;
	}
	
	/**
	 * calculates the available productions
	 * @return a list of productions
	 */
	public ArrayList<Productions> availableProduction() {
		ArrayList<Productions> list = new ArrayList<>();
		for (Productions p: Productions.values()) {
			if(presentProductions.get(p) && isSingleProductionAvailable(p))
				list.add(p);
		}
		return list;
	}
	
	

	public ArrayList<Stack<DevelopmentCard>> getCards() {
		return cards;
	}
	
	public ArrayList<Productions> getSelectedInput() {
		return selectedInput;
	}

	public void setSelectedInput(ArrayList<Productions> selectedInput) {
		this.selectedInput = selectedInput;
	}

	public int[] getInputResources() {
		return inputResources;
	}

	public void setInputResources(int[] inputResources) {
		this.inputResources = inputResources;
	}
}



