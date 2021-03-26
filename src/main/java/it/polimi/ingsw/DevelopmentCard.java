package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * this class handles single card management
 */
public class DevelopmentCard {

    private final int level,victoryPoints;
    private final Colors color;
    private final ArrayList<Integer> costResourceList;
    private final ArrayList<Resources> typeResourceList;
    private final ProductionRules productionRules;

    /**
     * constructor for instantiating card's attributes
     * @param level is the level of the card
     * @param color is the color of the card
     * @param victoryPoints is the number of card's victory points
     * @param costResourceList //
     * @param typeResourceList is a list of needed resources to buy the card
     * @param productionRules is the instance of the corresponding production rules
     */

    public DevelopmentCard(int level, Colors color, int victoryPoints, ArrayList<Integer> costResourceList,
                           ArrayList<Resources> typeResourceList, ProductionRules productionRules){
        this.level=level;
        this.color=color;
        this.victoryPoints=victoryPoints;
        this.costResourceList=costResourceList;
        this.typeResourceList=typeResourceList;
        this.productionRules=productionRules;
    }

    /**
     * getter for the attribute level
     * @return card's level
     */
    public int getLevel(){
        return level;
    }

    /**
     * getter for the attribute victorypoints
     * @return the number of Victory points
     */
    public int getVictoryPoints(){
        return victoryPoints;
    }

    /**
     * //
     * @return //
     */
    public ArrayList<Integer> getCostResourceList(){
        return costResourceList;
    }

    /**
     * //
     * @return //
     */
    public ArrayList<Resources> getTypeResourceList(){
        return typeResourceList;
    }

    /**
     * getter for card's color
     * @return the color of the card
     */
    public Colors getColor(){
        return color;
    }

    /**
     * it recall production rules function to get the output of the production
     * @return an arraylist of resources that contains production's output.
     */
    public ArrayList<Resources> produce(){
        return productionRules.getOutput();
    }

    /**
     * it checks if card's production is available
     * @param input is a list of all the resources the player owns
     * @return true if the the production is available
     */
    public boolean isCardProductionAvailable(ArrayList<Resources> input){
        return productionRules.isProductionAvailable(input);
    }

    /**
     * it checks if card's production returns faith points.
     * @return true if the number of faith points is bigger than 0
     */
    public boolean doesCardProductionReturnsFaithPoints(){
        return productionRules.doesProductionReturnsFaithPoints();
    }

    /**
     * getter for card's production faith points
     * @return the number of faith points
     */
    public int returnFaithPoints(){
        return productionRules.getFaithOutput();
    }







}
