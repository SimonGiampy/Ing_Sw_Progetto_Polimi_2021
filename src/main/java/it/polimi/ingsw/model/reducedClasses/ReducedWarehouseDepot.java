package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.WarehouseDepot;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;

import java.io.Serializable;
import java.util.ArrayList;

public class ReducedWarehouseDepot implements Serializable {

	private ArrayList<Resources> incomingResources; //incoming resources from the market
	private Resources[] depot; // array of fixed length = 6, representing the pyramid
	private final ArrayList<Integer> positionsIncomingResources; // memorizes the positions of the resources that have been moved from the deck

	// list of 2 lists of resources, one for each leader. If the value of the first element in the list is NO_RES,
	// then the corresponding leader is not activated yet. The size of the lists for the base case is 2
	private final ArrayList<ArrayList<Resources>> extraDepotResources;
	// list of 2 lists of booleans, one for each leader. If the flag value is false, then the corresponding slot is empty,
	// otherwise there is a resource occupying it
	private final ArrayList<ArrayList<Boolean>> extraDepotContents;

	public ReducedWarehouseDepot(WarehouseDepot depot){
		incomingResources=depot.getIncomingResources();
		this.depot=depot.getDepot();
		positionsIncomingResources= depot.getPositionsIncomingResources();
		extraDepotResources=depot.getExtraDepotResources();
		extraDepotContents=depot.getExtraDepotContents();
	}

	public ArrayList<Resources> getIncomingResources() {
		return incomingResources;
	}

	public Resources[] getDepot() {
		return depot;
	}

	public ArrayList<Integer> getPositionsIncomingResources() {
		return positionsIncomingResources;
	}

	public ArrayList<ArrayList<Resources>> getExtraDepotResources() {
		return extraDepotResources;
	}

	public ArrayList<ArrayList<Boolean>> getExtraDepotContents() {
		return extraDepotContents;
	}

	/**
	 * show the depot
	 */
	public void showDepot() {
		StringBuilder string = new StringBuilder();
		string.append("            ").append(Unicode.TOP_LEFT).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
				.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.TOP_RIGHT).append("\n");
		string.append("            ").append(Unicode.VERTICAL).append(" ").append(depot[0].toString())
				.append(" ").append(Unicode.VERTICAL).append("\n");
		string.append("        ").append(Unicode.TOP_LEFT).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
				.append(Unicode.HORIZONTAL).append(Unicode.BOTTOM_RIGHT).append("    ")
				.append(Unicode.BOTTOM_LEFT).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
				.append(Unicode.HORIZONTAL).append(Unicode.TOP_RIGHT).append("\n");
		string.append("        ").append(Unicode.VERTICAL).append(" ").append(depot[1].toString()).append("      ")
				.append(depot[2].toString()).append(" ").append(Unicode.VERTICAL).append("\n");
		string.append("    ").append(Unicode.TOP_LEFT).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
				.append(Unicode.HORIZONTAL).append(Unicode.BOTTOM_RIGHT).append("            ")
				.append(Unicode.BOTTOM_LEFT).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
				.append(Unicode.HORIZONTAL).append(Unicode.TOP_RIGHT).append("\n");
		string.append("    ").append(Unicode.VERTICAL).append(" ").append(depot[3].toString())
				.append("      ").append(depot[4].toString()).append("  ").append("    ").append(depot[5].toString())
				.append(" ").append(Unicode.VERTICAL).append("\n");
		string.append("    ").append(Unicode.BOTTOM_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(20)).append(Unicode.BOTTOM_RIGHT).append("\n");
		/*
		string.append("Top:       " + "_|").append(depot[0].toString()).append("|_").append("\n");
		string.append("Middle:  " + "_|").append(depot[1].toString()).append("  ")
				.append(depot[2].toString()).append("|_").append("\n");
		string.append("Bottom: " + "|").append(depot[3].toString())
				.append("  ").append(depot[4].toString()).append("  ").append(depot[5].toString()).append("|").append("\n");

		 */
		System.out.println(string);

	}

	/**
	 * shows the incoming resources
	 */
	public void showIncomingDeck() {
		System.out.print("deck contains: \t");
		for (int i = 1; i <= incomingResources.size(); i++) {
			System.out.print(i + ": " + incomingResources.get(i - 1).toString() + "\t");
		}
		System.out.print("\n");
	}
}
