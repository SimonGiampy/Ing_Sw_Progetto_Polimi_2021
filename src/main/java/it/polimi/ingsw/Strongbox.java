package it.polimi.ingsw;

import java.util.ArrayList;

/**
 * this class handles the strongbox and the resources in input and output
 */
public class Strongbox {
	private ArrayList<Resources> content;
	
	/**
	 * constructor for instantiating the default values
	 */
	public Strongbox(){
		content=new ArrayList<>();
	}
	
	/**
	 * it adds resources to the strongbox
	 * @param input is a list of the resources to add
	 */
	public void storeResources(ArrayList<Resources> input){
		content.addAll(input);
	}
	
	/**
	 * counts number of resources when the game ends
	 * @return number of resources in the strongbox
	 */
	public int numberOfResources(){
		return content.size();
	}
	
	/**
	 * it remove the selected resources from the strongbox
	 * @param input is a list of resources
	 */
	public void retrieveResources(ArrayList<Resources> input){
		content.removeAll(input);
	}
	
	/**
	 * getter for the attribute content
	 * @return a list of resources
	 */
	public ArrayList<Resources> getContent(){
		return content;
	}
	
}
