package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * this is class handles production rules of a card
 */
public class ProductionRules {

    private  final ArrayList<Resources> input;
    private final ArrayList<Resources> output;
    int faithOutput;

    /**
     * constructor for the class.
     * @param input is a list of the resources needed to activate production.
     * @param output is a list of the resources produced.
     * @param faithOutput indicates how many tiles the faith's sign is shifted when production is activated. it must be from 0 to 2.
     */
    public ProductionRules(ArrayList<Resources> input, ArrayList<Resources> output, int faithOutput){
        this.input=input;
        this.output=output;
        this.faithOutput=faithOutput;
    }

    /**
     * getter for the attribute output
     * @return an arraylist of resources that contains production's output.
     */
    public ArrayList<Resources> getOutput(){
        return output;
    }

    /**
     * getter for the attribute FaithOutput
     * @return how many tiles the faith's sign is shifted when production is activated. it must be from 0 to 2.
     */
    public int getFaithOutput(){
        return faithOutput;
    }

    /**
     * it checks if production is available
     * @param input is a list of all the resources the player owns
     * @return true if the list needed for production is contained in the player's boxes.
     */
    public boolean isProductionAvailable(ArrayList<Resources> input){
        return input.contains(this.input);
    }

    /**
     * it checks if the production returns faith points.
     * @return true if the number of faith points is bigger than 0.
     */
    public boolean doesProductionReturnsFaithPoints(){
        return (getFaithOutput()>0);
    }




}
