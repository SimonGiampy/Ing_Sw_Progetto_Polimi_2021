package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * this is class handles production rules of a card
 */
public class ProductionRules {
	
	private  final ArrayList<Resources> input;
	private  final ArrayList<Resources> output;
	private final int faithOutput;
	
	/**
	 * constructor for development card's production rules
	 * @param input is a list of the resources needed to activate production.
	 * @param output is a list of the resources produced.
	 * @param faithOutput indicates how many tiles the faith's sign is shifted when production is activated. it must be from 0 to 2.
	 */
	public ProductionRules(ArrayList<Resources> input, ArrayList<Resources> output, int faithOutput){
		this.input = input;
		this.output = output;
		this.faithOutput = faithOutput;
	}

	/**
	 * getter for the attribute output
	 * @return an arraylist of resources that contains production's output.
	 */
	public ArrayList<Resources> produce(){
		return new ArrayList<>(output);
	}

	/**
	 * getter for the attribute FaithOutput
	 * @return how many tiles the faith mover is shifted when production is activated. It must be from 0 to 2.
	 */
	public int getFaithOutput(){
		return faithOutput;
	}

	/**
	 * getter for the attribute input (it creates a copy)
	 * @return an Arraylist of production input
	 */
	public ArrayList<Resources> getInputCopy(){
		return new ArrayList<>(input);
	}
	
	/**
	 * getter for the attribute output (it creates a copy)
	 * @return an Arraylist of production output
	 */
	public ArrayList<Resources> getOutputCopy(){
		return new ArrayList<>(output);
	}
	
	/**
	 * it checks if production is available
	 * @param input is a list of all the resources the player owns
	 * @return true if the list needed for production is contained in the player's boxes. this version handles also the case when production input contains ? values
	 */
	public boolean isProductionAvailable(ArrayList<Resources> input){
		if (this.input.stream().noneMatch(i -> i != Resources.EMPTY))
			return this.input.size() <= input.size();
		else
			return ListSet.subset(
					this.input.stream().filter(i -> i != Resources.EMPTY).collect(Collectors.toCollection(ArrayList::new)),input)
					&& this.input.stream().filter((i -> i == Resources.EMPTY)).count() <=
					input.size() - this.input.stream().filter((i-> i != Resources.EMPTY)).count();
	}

	/**
	 *
	 * @return the number of ? production values (indicated with EMPTY flag)
	 */
	public int numberOfOutputEmptyResources(){
		return (int) output.stream().filter(i -> i==Resources.EMPTY).count();
	}
	public int numberOfInputEmptyResources(){
		return (int) input.stream().filter(i -> i==Resources.EMPTY).count();
	}
}
