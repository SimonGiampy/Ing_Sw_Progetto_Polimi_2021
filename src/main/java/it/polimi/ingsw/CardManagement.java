package it.polimi.ingsw;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * this class handles player's cards management
 */
public class CardManagement {
	private final ArrayList<Stack<DevelopmentCard>> cards;
	private final ProductionRules baseProductionRules;
	private final ArrayList<ProductionRules> leaderProductionRules;

	/**
	 * default constructor
	 */
	public CardManagement(){
		cards=new ArrayList<>();
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		cards.add(new Stack<>());
		baseProductionRules= new ProductionRules();
		leaderProductionRules= new ArrayList<>();

	}

	/* // in stand by waiting for leader cards class
	public void addLeaderCard(){
		leaderProductionRules.add(new ProductionRules());
	}
	*/

	/**
	 * it adds a new card on the selected stack
	 * @param newCard is the card to insert
	 * @param selectedStack is the number of the selected stack
	 */
	public void addCard(DevelopmentCard newCard,int selectedStack){
		switch (selectedStack){
			case 1 ->   cards.get(1).push(newCard);
			case 2 ->   cards.get(2).push(newCard);
			case 3 ->   cards.get(3).push(newCard);
			default ->  throw new InvalidParameterException("invalid parameter");
		}
	}

	/**
	 * it checks top card's level
	 * @param selectedStack is the number of the selected stack
	 * @return top card's level of the selected stack
	 */
	public int checkStackLevel(int selectedStack){
		return switch (selectedStack) {
			case 1 ->   cards.get(1).size();
			case 2 ->   cards.get(2).size();
			case 3 ->   cards.get(3).size();
			default ->  throw new InvalidParameterException("invalid parameter");
		};
	}

	/**
	 * it activates production
	 * @param selectedProduction is the number of the selected stack
	 * @return an arraylist of resources that contains production's output.
	 */
	public ArrayList<Resources> activateProduction(int selectedProduction) {
		return switch (selectedProduction) {
			case 1 -> cards.get(1).peek().produce();
			case 2 -> cards.get(2).peek().produce();
			case 3 -> cards.get(3).peek().produce();
			case 4 -> baseProductionRules.getOutput(); // wanna put the reset here?
			case 5 -> leaderProductionRules.get(1).getOutput();
			case 6 -> leaderProductionRules.get(2).getOutput();
			default -> throw new InvalidParameterException("invalid parameter");
		};
	}

	/**
	 * it checks if the card returns faith points (this method could be useless)
	 * @param selectedStack is the number of the selected stack
	 * @return true if the card returns faith points
	 */
	public boolean doesProductionReturnsFaithPoints(int selectedStack) {
		return switch (selectedStack) {
			case 1 -> cards.get(1).peek().doesCardProductionReturnsFaithPoints();
			case 2 -> cards.get(2).peek().doesCardProductionReturnsFaithPoints();
			case 3 -> cards.get(3).peek().doesCardProductionReturnsFaithPoints();
			default -> throw new InvalidParameterException("invalid parameter");
		};
	}

	/**
	 * it returns the number of faith points
	 * @param selectedStack is the number of the selected stack
	 * @return the number of faith points
	 */
	public int returnFaithPoints(int selectedStack) {
		return switch (selectedStack) {
			case 1 -> cards.get(1).peek().returnFaithPoints();
			case 2 -> cards.get(2).peek().returnFaithPoints();
			case 3 -> cards.get(3).peek().returnFaithPoints();
			case 4 -> baseProductionRules.getFaithOutput();
			case 5 -> leaderProductionRules.get(1).getFaithOutput();
			case 6 -> leaderProductionRules.get(2).getFaithOutput();
			default -> throw new InvalidParameterException("invalid parameter");
		};
	}

	/**
	 * it calculates player's cards points
	 * @return the sum of all the cards points
	 */
	public int totalVictoryPoints(){
		return  cards.get(1).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
				cards.get(2).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
				cards.get(3).stream().mapToInt(DevelopmentCard::getVictoryPoints).sum();
	}

	/**
	 *
	 * @param selectedStack is the number of the selected stack
	 * @param input is a list of all the resources the player owns
	 * @return true if the card's production is available
	 */
	public boolean isProductionAvailable(int selectedStack, ArrayList<Resources> input){
		return switch (selectedStack) {
			case 1 -> cards.get(1).peek().isCardProductionAvailable(input);
			case 2 -> cards.get(2).peek().isCardProductionAvailable(input);
			case 3 -> cards.get(3).peek().isCardProductionAvailable(input);
			case 4 -> baseProductionRules.isProductionAvailable(input);
			case 5 -> leaderProductionRules.get(1).isProductionAvailable(input);
			case 6 -> leaderProductionRules.get(2).isProductionAvailable(input);
			default -> throw new InvalidParameterException("invalid parameter");
		};
	}

	public boolean isLeaderCardActivated(int selectedLeaderCard) {
		return switch (selectedLeaderCard) {
			case 1 -> leaderProductionRules.size() > 0;
			case 2 -> leaderProductionRules.size() > 1;
			default -> throw new InvalidParameterException("invalid parameter");
		};
	}

	/** TODO: handle leader production
	 * it checks if one of card's productions is available
	 * @param input is a list of all the resources the player owns
	 * @return true if at least one of card's production is available
	 */
	public boolean isProductionAvailable(ArrayList<Resources> input){
		return  isProductionAvailable(1,input) ||
				isProductionAvailable(2,input) ||
				isProductionAvailable(3,input);
	}
	public int NumberOfCards(){
		return cards.get(1).size()+cards.get(2).size()+cards.get(3).size();
	}

}
