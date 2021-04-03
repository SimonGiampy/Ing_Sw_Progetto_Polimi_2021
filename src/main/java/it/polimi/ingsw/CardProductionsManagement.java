package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidDevCardSlotException;
import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;

import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * this class handles player's cards management
 * TODO: this javadoc needs more words
 */
public class CardProductionsManagement {
	private final ArrayList<Stack<DevelopmentCard>> cards;
	private final ProductionRules baseProductionRules;
	private final ArrayList<ProductionRules> leaderProductionRules;
	private final Strongbox myStrongbox;
	private final WarehouseDepot myWarehouseDepot;
	private final boolean[] numberOfProduction;

	/**
	 * default constructor
	 */
	public CardProductionsManagement(Strongbox playerStrongbox, WarehouseDepot playerWarehouseDepot, ProductionRules baseProductionRules){
		cards=new ArrayList<>();
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		this.baseProductionRules = baseProductionRules;
		leaderProductionRules = new ArrayList<>();
		myStrongbox = playerStrongbox;
		myWarehouseDepot = playerWarehouseDepot;
		numberOfProduction = new boolean[]{false,false,false,true,false,false};
	}

	/**
	 * it adds a new leader card to available productions
	 * @param input is a list of required resources to activate production
	 * @param faithOutput is the number of faith points
	 */
	public void addLeaderCard(ArrayList<Resources> input, ArrayList<Resources> output, int faithOutput){
		if (leaderProductionRules.size()==0)
			numberOfProduction[4] = true;
		else
			numberOfProduction[5] = true;
		leaderProductionRules.add(new ProductionRules(input,output,faithOutput));
	}


	/**
	 * it adds a new card on the selected stack
	 * @param newCard is the card to insert
	 * @param selectedStack is the number of the selected stack
	 */
	public void addCard(DevelopmentCard newCard,int selectedStack) throws InvalidDevCardSlotException {
		switch (selectedStack){
			case 1 -> {
				if(cards.get(0).size()==0)
					numberOfProduction[0]=true;
				cards.get(0).push(newCard);
			}
			case 2 -> {
				if(cards.get(1).size()==0)
					numberOfProduction[1]=true;
				cards.get(1).push(newCard);
			}
			case 3 -> {
				if(cards.get(2).size()==0)
					numberOfProduction[2]=true;
				cards.get(2).push(newCard);
			}
			default ->  throw new InvalidDevCardSlotException("invalid slot number for card insertion");
		}
	}

	/**
	 * it checks top card's level
	 * @param selectedStack is the number of the selected stack
	 * @return top card's level of the selected stack
	 */
	public int checkStackLevel(int selectedStack) throws InvalidInputException {
		return switch (selectedStack) {
			case 1 ->   cards.get(0).size();
			case 2 ->   cards.get(1).size();
			case 3 ->   cards.get(2).size();
			default ->  throw new InvalidInputException("invalid input parameter (selected stack)");
		};
	}

	/**
	 * it activates production
	 * @param selectedProduction is the number of the selected stack
	 */
	public ArrayList<Resources> activateSingleProduction(int selectedProduction) throws InvalidUserRequestException {
		return switch (selectedProduction) {
			case 1 -> cards.get(0).peek().produce();
			case 2 -> cards.get(1).peek().produce();
			case 3 -> cards.get(2).peek().produce();
			case 4 -> baseProductionRules.produce();
			case 5 -> leaderProductionRules.get(0).produce();
			case 6 -> leaderProductionRules.get(1).produce();
			default -> throw new InvalidUserRequestException("invalid production number chosen");
		};
	}
	/* TODO: if ? are equals to 0 set input ArrayList (or Array) to [0,0,0,0] */
	/**
	 * it activates all selected production
	 * @param playerInput is a list of selected production
	 * @param inputResources is a list of resources selected by the player
	 * @return the number of faith points of the selected production
	 */
	public int activateSelectedProduction(ArrayList<Integer> playerInput,int[] inputResources) throws InvalidUserRequestException {
		ArrayList<Resources> selectedProduction= new ArrayList<>();
		int totalFaithPoints=0;
		for (Integer integer : playerInput) {
			selectedProduction.addAll(activateSingleProduction(integer));
			totalFaithPoints=returnFaithPoints(integer);
		}
		for (int j = 0; j <inputResources[0]; j++) {
			selectedProduction.add(Resources.COIN);
		}
		for (int j = 0; j < inputResources[1]; j++) {
			selectedProduction.add(Resources.SERVANT);
		}
		for (int j = 0; j < inputResources[2]; j++) {
			selectedProduction.add(Resources.SHIELD);
		}
		for (int j = 0; j < inputResources[3]; j++) {
			selectedProduction.add(Resources.STONE);
		}
		selectedProduction = selectedProduction.stream().filter(i -> i != Resources.EMPTY).collect(Collectors.toCollection(ArrayList::new));
		myStrongbox.storeResources(selectedProduction);
		return totalFaithPoints;
	}


	/**
	 * it returns the number of faith points
	 * @param selectedStack is the number of the selected stack
	 * @return the number of faith points
	 */
	public int returnFaithPoints(int selectedStack) throws InvalidUserRequestException {
		return switch (selectedStack) {
			case 1 -> cards.get(0).peek().returnFaithPoints();
			case 2 -> cards.get(1).peek().returnFaithPoints();
			case 3 -> cards.get(2).peek().returnFaithPoints();
			case 4 -> baseProductionRules.getFaithOutput();
			case 5 -> leaderProductionRules.get(0).getFaithOutput();
			case 6 -> leaderProductionRules.get(1).getFaithOutput();
			default -> throw new InvalidUserRequestException("invalid production number chosen");
		};
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
	public boolean isSingleProductionAvailable(int selectedStack) throws InvalidUserRequestException {
		ArrayList<Resources> playerResources= myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		return switch (selectedStack) {
			case 1 -> cards.get(0).peek().isCardProductionAvailable(playerResources);
			case 2 -> cards.get(1).peek().isCardProductionAvailable(playerResources);
			case 3 -> cards.get(2).peek().isCardProductionAvailable(playerResources);
			case 4 -> baseProductionRules.isProductionAvailable(playerResources);
			case 5 -> leaderProductionRules.get(0).isProductionAvailable(playerResources);
			case 6 -> leaderProductionRules.get(1).isProductionAvailable(playerResources);
			default -> throw new InvalidUserRequestException("invalid production number chosen");
		};
	}

	/**
	 * it gets the selected production input
	 * @param selectedStack is the number of the selected stack
	 * @return an Arraylist of production's input
	 */
	public ArrayList<Resources> getProductionInput(int selectedStack) throws InvalidUserRequestException {
		return switch (selectedStack) {
			case 1 -> cards.get(0).peek().getProductionInput();
			case 2 -> cards.get(1).peek().getProductionInput();
			case 3 -> cards.get(2).peek().getProductionInput();
			case 4 -> baseProductionRules.getInputCopy();
			case 5 -> leaderProductionRules.get(0).getInputCopy();
			case 6 -> leaderProductionRules.get(1).getInputCopy();
			default -> throw new InvalidUserRequestException("invalid production number chosen");
		};
	}

	/**
	 * it checks if all selected productions (valid) are available
	 * @param playerInput is a list of selected production
	 * @return true if all productions can be started at the same time
	 */
	public boolean isSelectedProductionAvailable(ArrayList<Integer> playerInput) throws InvalidUserRequestException {
		ArrayList<Resources> playerResources= myStrongbox.getContent();
		playerResources.addAll(myWarehouseDepot.gatherAllResources());
		ArrayList<Resources> productionInput= new ArrayList<>();
		for (Integer integer : playerInput) {
			productionInput.addAll(getProductionInput(integer));
		}
		ProductionRules allSelectedProduction = new ProductionRules(productionInput, new ArrayList<>(),0);
		return allSelectedProduction.isProductionAvailable(playerResources);
	}

	/** TODO: Exception
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
		ArrayList<Resources> filteredProduction = productionInput.stream().filter(i->i!=Resources.EMPTY)
				.collect(Collectors.toCollection(ArrayList::new));
		for (int i = 0; i < inputResources[i]; i++) {
			filteredProduction.add(Resources.COIN);
		}
		for (int i = 0; i < inputResources[i]; i++) {
			filteredProduction.add(Resources.SERVANT);
		}
		for (int i = 0; i < inputResources[i]; i++) {
			filteredProduction.add(Resources.SHIELD);
		}
		for (int i = 0; i < inputResources[i]; i++) {
			filteredProduction.add(Resources.STONE);
		}
		ProductionRules allSelectedProduction= new ProductionRules(filteredProduction,new ArrayList<>(),0);
		if (allSelectedProduction.isProductionAvailable(playerResources)){
			remainingResources = myWarehouseDepot.payResources(filteredProduction);
			myStrongbox.retrieveResources(remainingResources);
		}
		//else Exception
	}

	/**
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
	 * TODO: forgot javadoc here
	 * @param playerInput
	 * @return
	 */
	public boolean checkPlayerInput(ArrayList<Integer> playerInput){
		//return playerInput.stream().equals(playerInput.stream().distinct()) && playerInput.stream().noneMatch(i -> i>numberOfProduction);
		for (Integer integer : playerInput) {
			int input = integer - 1;
			if (numberOfProduction[input])
				return false;
		}
		return true;
	}
}




