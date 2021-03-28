package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * this is class handles production rules of a card
 */
public class ProductionRules {
	
	private  final ArrayList<Resources> input;
	private  final ArrayList<Resources> output;
	int faithOutput;
	
	/**
	 * constructor for development card's production rules
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
	 * constructor for base production rules
	 */
	public ProductionRules(){
		this.input=new ArrayList<Resources>();
		this.output=new ArrayList<Resources>();
		faithOutput=0;
	}

	/**
	 * constructor for leader card's production
	 * is a list of the resources needed to activate production
	 * @param faithOutput is a list of the resources produced
	 */
	public ProductionRules(ArrayList<Resources> input, int faithOutput){
		this.input=input;
		this.output= new ArrayList<Resources>();
		this.faithOutput=faithOutput;
	}

	/**
	 * setter for the attribute output when the player can select it
	 * @param
	 */
	public void addSelectedOutput( Resources selectedResources) {
		if (this.output.isEmpty())
			this.output.add(selectedResources);
	}

	/**
	 * it resets output after the end of the turn (base production or leader production)
	 */
	public void resetProduction(){
		this.output.clear();
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
		if (this.input.isEmpty())
			return (input.size()>1);
		else
			return input.containsAll(this.input);
	}
	
	/**
	 * it checks if the production returns faith points.
	 * @return true if the number of faith points is bigger than 0.
	 */
	public boolean doesProductionReturnsFaithPoints(){
		return (getFaithOutput()>0);
	}
}
