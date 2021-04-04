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
	 * it remove the selected resources from the strongbox
	 * @param input is a list of resources
	 */
	public void retrieveResources(ArrayList<Resources> input){
		content = ListSet.removeSubSet(content, input);
	}
	
	/**
	 * getter for the attribute content
	 * @return a list of resources
	 */
	public ArrayList<Resources> getContent(){
		return new ArrayList<>(content);
	}
}
