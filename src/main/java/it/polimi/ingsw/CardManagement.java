package it.polimi.ingsw;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * this class handles player's cards management
 */
public class CardManagement {
    private final Stack<DevelopmentCard> firstStack;
    private final Stack<DevelopmentCard> secondStack;
    private final Stack<DevelopmentCard> thirdStack;

    /**
     * default constructor
     */
    public CardManagement(){
        firstStack= new Stack<>();
        secondStack= new Stack<>();
        thirdStack= new Stack<>();
    }

    /**
     * it adds a new card on the selected stack
     * @param newCard is the card to insert
     * @param selectedStack is the number of the selected stack
     */
    public void addCard(DevelopmentCard newCard,int selectedStack){
        switch (selectedStack){
            case 1 ->   firstStack.push(newCard);
            case 2 ->   secondStack.push(newCard);
            case 3 ->   thirdStack.push(newCard);
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
            case 1 ->   firstStack.size();
            case 2 ->   secondStack.size();
            case 3 ->   thirdStack.size();
            default ->  throw new InvalidParameterException("invalid parameter");
        };
    }

    /**
     * it activate card production
     * @param selectedStack is the number of the selected stack
     * @return an arraylist of resources that contains production's output.
     */
    public ArrayList<Resources> activateProduction(int selectedStack) {
        return switch (selectedStack) {
            case 1 -> firstStack.peek().produce();
            case 2 -> secondStack.peek().produce();
            case 3 -> thirdStack.peek().produce();
            default -> throw new InvalidParameterException("invalid parameter");
        };
    }

    /**
     * it checks if the card returns faith points
     * @param selectedStack is the number of the selected stack
     * @return true if the card returns faith points
     */
    public boolean doesCardReturnsFaithPoints(int selectedStack) {
        return switch (selectedStack) {
            case 1 -> firstStack.peek().doesCardProductionReturnsFaithPoints();
            case 2 -> secondStack.peek().doesCardProductionReturnsFaithPoints();
            case 3 -> thirdStack.peek().doesCardProductionReturnsFaithPoints();
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
            case 1 -> firstStack.peek().returnFaithPoints();
            case 2 -> secondStack.peek().returnFaithPoints();
            case 3 -> thirdStack.peek().returnFaithPoints();
            default -> throw new InvalidParameterException("invalid parameter");
        };
    }

    /**
     * it calculates player's cards points
     * @return the sum of all the cards points
     */
    public int totalVictoryPoints(){
        return  firstStack.stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
                secondStack.stream().mapToInt(DevelopmentCard::getVictoryPoints).sum()+
                thirdStack.stream().mapToInt(DevelopmentCard::getVictoryPoints).sum();
    }

    /**
     *
     * @param selectedStack is the number of the selected stack
     * @param input is a list of all the resources the player owns
     * @return true if the card's production is available
     */
    public boolean isStackProductionAvailable(int selectedStack, ArrayList<Resources> input){
        return switch (selectedStack) {
            case 1 -> firstStack.peek().isCardProductionAvailable(input);
            case 2 -> secondStack.peek().isCardProductionAvailable(input);
            case 3 -> thirdStack.peek().isCardProductionAvailable(input);
            default -> throw new InvalidParameterException("invalid parameter");
        };
    }

    /**
     * it checks if one of card's productions is available
     * @param input is a list of all the resources the player owns
     * @return true if at least one of card's production is available
     */
    public boolean isProductionAvailable(ArrayList<Resources> input){
        return  isStackProductionAvailable(1,input) ||
                isStackProductionAvailable(2,input) ||
                isStackProductionAvailable(3,input);
    }
    public int NumberOfCards(){
        return firstStack.size()+secondStack.size()+thirdStack.size();
    }

}
