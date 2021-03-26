package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * this class handles the warehouse and the resources in input and output
 */
public class WarehouseDepot {
	
	private ArrayList<Resources> incomingResources; //incoming resources from the market
	private Resources[] depot; // array of fixed length = 6, representing the pyramid
	private ArrayList<Integer> positionsIncomingResources;
	
	/**
	 * constructor for instantiating the default values
	 */
	protected WarehouseDepot() {
		incomingResources = new ArrayList<>();
		positionsIncomingResources = new ArrayList<>();
		depot = new Resources[6];
		for (int i = 0; i < 6; i++) depot[i] = Resources.NO_RESOURCE;
	}
	
	/**
	 * debugging function for showing warehouse shelves content
	 */
	protected void showDepot() {
		System.out.println("warehouse contains: ");
		System.out.println("top: \t\t" + depot[0].toString());
		System.out.println("middle: \t" + depot[1].toString() + "\t" + depot[2].toString());
		System.out.println("bottom: \t" + depot[3].toString() + "\t" + depot[4].toString() + "\t" + depot[5].toString());
	}
	
	/**
	 * debugging function for showing deck resources
	 */
	protected void showIncomingDeck() {
		System.out.print("deck contains: \t");
		for (int i = 1; i <= incomingResources.size(); i++) {
			System.out.print(i + ": " + incomingResources.get(i - 1).toString() + "\t");
		}
		System.out.print("\n");
	}
	
	/**
	 * moves the resources around between the deck and the depot. It also checks the validity of the numbers in input
	 * @param where depot or deck. Indicates the place where to move the resources from
	 * @param from positional number in the corresponding array of the place containing the resources
	 * @param destination positional number of the warehouse pyramid
	 * @return true if the present configuration of the warehouse is correct
	 *         false if more moves are required
	 */
	protected boolean moveResources(String where, int from, int destination) {
		//syntax: move <from> from <deck/depot> to <destination>
		// moves a resource coming from the deck (input) or the same depot. The resource has a related positional number for input and output
		// the destination is always in the depot (pyramid) where each position has a number associated (from 1 to 6)
		
		if (where.equals("deck")) { // resource coming from the deck
			if (depot[destination - 1] == Resources.NO_RESOURCE) { // valid destination
				depot[destination - 1] = incomingResources.get(from - 1);
				positionsIncomingResources.add(destination);
				incomingResources.remove(from - 1);
			} else { // destination is occupied by another resource
				System.out.println("move is not permitted");
				return false;
			}
		} else if (where.equals("depot")) { // resource coming from the depot
			if (depot[from - 1] == Resources.NO_RESOURCE) { // resource moved must not be empty
				System.out.println("move is not permitted");
				return false;
			} else { //switches the positions in the pyramid
				if (!(positionsIncomingResources.contains(from) && positionsIncomingResources.contains(destination))) {
					if (positionsIncomingResources.contains(from)) { // updates the list with the new position
						positionsIncomingResources.add(destination);
						positionsIncomingResources.remove((Integer) from);
					} else if (positionsIncomingResources.contains(destination)) { // updates the list with the new position
						positionsIncomingResources.add(from);
						positionsIncomingResources.remove((Integer) destination);
					}
				}
				
				Resources temp = depot[from - 1];
				depot[from - 1] = depot[destination - 1];
				depot[destination - 1] = temp;
			}
		}
		
		// if the deck is empty and all the resources are in a correct place, then it returns true
		if (isCombinationCorrect(getConvertedList(depot))) {
			return true;
		} else {
			System.out.println("more moves needed"); // because the configuration is not complete yet
			return false;
		}
	}
	
	/**
	 * move a resource from the warehouse to the deck, only if the same resource was in the deck before
	 * @param position the positional number of the resource in the warehouse
	 * @return true if the move is permitted and correctly executed
	 */
	protected boolean moveResourcesBackToDeck(int position) {
		if (depot[position - 1] == Resources.NO_RESOURCE) { // cannot move an empty resource
			System.out.println("move is not permitted");
			return false;
		} else {
			if (positionsIncomingResources.contains(position)) { // resource was in the deck before the insertion
				incomingResources.add(depot[position - 1]);
				depot[position - 1] = Resources.NO_RESOURCE;
				positionsIncomingResources.remove((Integer) position);
			} else { // the resource was not in the deck before the insertion
				System.out.println("move is not permitted");
				return false;
			}
		}
		
		// if the deck is empty and all the resources are in a correct place, then it returns true
		if (isCombinationCorrect(getConvertedList(depot))) {
			return true;
		} else {
			System.out.println("more moves needed"); // because the configuration is not complete yet
			return false;
		}
	}
	
	/**
	 * input management for moving things around in the warehouse
	 * @return a boolean that indicates if all the resources have been moved and if the warehouse configuration is correct
	 *         returns false if more moves are required
	 */
	protected boolean processNewMove() {
		Scanner scanner = new Scanner(System.in);
		String read;
		String regexGoingToWarehouse = "move\s[1-4]\sfrom\s(deck|depot)\sto\s[1-6]"; // regex pattern for reading input for moving the
		// resources to the warehouse
		String regexGoingToDeck = "move\s[1-6]\sto\sdeck"; //regex pattern for reading input for moving back to the deck
		
		showIncomingDeck();
		showDepot();
		System.out.println("write new move command");
		boolean checkGoingToWarehouse, checkGoingToDeck, ok ;
		int from = 0;
		String place = "";
		do {
			read = scanner.nextLine(); // user input
			
			checkGoingToWarehouse = Pattern.matches(regexGoingToWarehouse, read);
			if (checkGoingToWarehouse) { // process request for moving resource from the deck to the warehouse
				from = Character.getNumericValue(read.charAt(5));
				if (read.charAt(14) == 'c') { //send from deck
					place = "deck";
				} else { // send from depot
					place = "depot";
				}
				if (place.equals("deck") && from > incomingResources.size()) { // invalid input
					ok = false;
				} else {
					ok = true;
				}
			} else { // sends the request
				checkGoingToDeck = Pattern.matches(regexGoingToDeck, read);
				ok = checkGoingToDeck;
			}
			
			if (!ok) { // user input does not match with the defined pattern
				System.out.println("input request invalid, write again");
			}
		} while (!ok); // while the input is not valid
		
		if (checkGoingToWarehouse) { // process request for moving to the warehouse
			if (place.equals("deck")) {
				return moveResources(place, from, Character.getNumericValue(read.charAt(20)));
			} else {
				return moveResources(place, from, Character.getNumericValue(read.charAt(21)));
			}
		} else { // process request for moving from the warehouse to the deck
			return moveResourcesBackToDeck(Character.getNumericValue(read.charAt(5)));
		}
		
	}
	
	/**
	 * adds new resources to the deck, from the market
	 * @param list of resources to add. Its length must be from 1 to 4
	 */
	protected void addResource(ArrayList<Resources> list) {
		this.incomingResources = list;
	}
	
	/**
	 * helper function that shows the user how to interact with the CLI
	 */
	protected void helpMe() {
		System.out.println("Syntax for moving resources from the deck or depot to the depot is: 'move <position> from <deck/depot> to " +
				"<destination>'");
		System.out.println("Syntax for moving a resource from the warehouse to the deck is : 'move <position> to deck'");
		System.out.println("The positional number in the warehouse is between 1 and 6: from top to bottom, and from left to right");
	}
	
	
	/**
	 * analyzes the integer array equivalent of a list of resources
	 * @param depot the converted array
	 * @return true if the combination of resources is a valid one
	 */
	protected boolean isCombinationCorrect(int[] depot) {
		if (depot[0] != 0) { // top shelf is not empty
			// top shelf has the same resource as middle shelf or top shelf
			if (depot[0] == depot[1] || depot[0] == depot[2] || depot[0] == depot[3] || depot[0] == depot[4] || depot[0] == depot[5]) {
				return false;
			}
		}
		
		if (depot[1] != 0) { //middle shelf is not empty
			if (depot[1] != depot[2] && depot[2] != 0) { // first resource must be equal to the second one if not empty
				return false;
			} else if (depot[1] == depot[3] || depot[1] == depot[4] || depot[1] == depot[5]) { // resources must be different in different shelves
				return false;
			}
		} else { // middle shelf is empty
			if (depot[2] != 0) { //if the first spot is not occupied, then also the second one should not be occupied
				if (depot[2] == depot[3] || depot[2] == depot[4] || depot[2] == depot[5]) {
					return false;
				}
			}
		}
		
		if (depot[3] != 0) { // bottom shelf is not empty
			if (depot[3] != depot[4] && depot[4] != 0) { // different resources in the same shelf
				return false;
			} else if (depot[3] != depot[5] && depot[5] != 0) { // different resources in the same shelf
				return false;
			} else if (depot[4] != depot[5] && depot[4] != 0 && depot[5] != 0) {
				return false;
			}
		} else { // bottom shelf is empty
			if (depot[4] != 0) { // if first resource is empty, then also the others must be empty as well
				if (depot[4] != depot[5] && depot[5] != 0) {
					return false;
				}
			}
		}
		
		return true; //if the conditions above are not satisfied
	}
	
	/**
	 * transforms an array of resources into its corresponding integer array values
	 * @param list of resources to be converted
	 * @return converted list
	 */
	protected int[] getConvertedList(Resources[] list) {
		assert list.length == 6;
		int[] depotConverted = new int[6];
		for (int i = 0; i < 6; i++) {
			switch (list[i]) {
				case COIN -> depotConverted[i] = 1;
				case STONE -> depotConverted[i] = 2;
				case SERVANT -> depotConverted[i] = 3;
				case SHIELD -> depotConverted[i] = 4;
				default -> depotConverted[i] = 0;
			}
		}
		return depotConverted;
		
	}
	

}
