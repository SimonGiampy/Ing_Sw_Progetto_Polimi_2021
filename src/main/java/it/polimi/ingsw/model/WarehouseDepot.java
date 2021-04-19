package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.model.util.Unicode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This class handles the warehouse and the resources in input from the market, and output for the productions
 * This class also handles the additional depots for a certain number of leader cards activated
 */
public class WarehouseDepot {
	
	private ArrayList<Resources> incomingResources; //incoming resources from the market
	private Resources[] depot; // array of fixed length = 6, representing the pyramid
	private final ArrayList<Integer> positionsIncomingResources; // memorizes the positions of the resources that have been moved from the deck
	
	
	// list of 2 lists of resources, one for each leader. If the value of the first element in the list is NO_RES,
	// then the corresponding leader is not activated yet. The size of the lists for the base case is 2
	private final ArrayList<ArrayList<Resources>> extraDepotResources;
	// list of 2 lists of booleans, one for each leader. If the flag value is false, then the corresponding slot is empty,
	// otherwise there is a resource occupying it
	private final ArrayList<ArrayList<Boolean>> extraDepotContents;
	
	
	/**
	 * constructor for instantiating the default values
	 */
	public WarehouseDepot() {
		incomingResources = new ArrayList<>();
		positionsIncomingResources = new ArrayList<>();
		depot = new Resources[6];
		
		for (int i = 0; i < 6; i++) depot[i] = Resources.EMPTY;
		
		//initialization of the extra depots
		extraDepotResources = new ArrayList<>();
		extraDepotResources.add(new ArrayList<>());
		extraDepotResources.get(0).add(Resources.EMPTY);
		extraDepotResources.add(new ArrayList<>());
		extraDepotResources.get(1).add(Resources.EMPTY);
		extraDepotContents = new ArrayList<>();
		extraDepotContents.add(new ArrayList<>());
		extraDepotContents.add(new ArrayList<>());
	}
	
	/**
	 * debugging function for showing warehouse shelves content
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
	 * debugging function for showing deck resources
	 */
	public void showIncomingDeck() {
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
	public boolean moveResources(String where, int from, int destination) throws InvalidUserRequestException {
		//syntax: move <from> from <deck/depot> to <destination>
		// moves a resource coming from the deck (input) or the same depot. The resource has a related positional number for input and output
		// the destination is always in the depot (pyramid) where each position has a number associated (from 1 to 6)
		
		if (where.equals("deck")) { // resource coming from the deck
			if (depot[destination - 1] == Resources.EMPTY) { // valid destination
				depot[destination - 1] = incomingResources.get(from - 1);
				positionsIncomingResources.add(destination);
				incomingResources.remove(from - 1);
			} else { // destination is occupied by another resource
				throw new InvalidUserRequestException("move is not permitted");
			}
		} else if (where.equals("depot")) { // resource coming from the depot
			if (depot[from - 1] == Resources.EMPTY) { // resource moved must not be empty
				throw new InvalidUserRequestException("move is not permitted");
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
	public boolean moveResourcesBackToDeck(int position) throws InvalidUserRequestException {
		if (depot[position - 1] == Resources.EMPTY) { // cannot move an empty resource
	        throw new InvalidUserRequestException("move is not permitted");
		} else {
			if (positionsIncomingResources.contains(position)) { // resource was in the deck before the insertion
				incomingResources.add(depot[position - 1]);
				depot[position - 1] = Resources.EMPTY;
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
	 * adds new resources to the deck, from the market
	 * @param list of resources to add. Its length must be from 1 to 4
	 */
	public void addIncomingResources(ArrayList<Resources> list) {
		if (incomingResources.isEmpty()) {
			incomingResources = list;
		}
	}
	
	/**
	 * assigns a single resource according to the game rules, at the start of the game
	 * @param res a single resource to start with
	 */
	public void assignInitialResources(Resources res) {
		depot[0] = res;
	}
	
	/**
	 * assigns two resources according to the game rules, at the start of the game
	 * @param res1 first resource
	 * @param res2 second resource
	 */
	public void assignInitialResources(Resources res1, Resources res2) {
		if (res1 == res2) {
			depot[1] = res1;
			depot[2] = res2;
		} else {
			depot[0] = res1;
			depot[1] = res2;
		}
	}
	
	/**
	 * removes all the elements in the incoming deck when the player finishes its turn.
	 * All the resources in the deck are automatically discarded
	 * @return the number of resources discarded
	 */
	public int discardResourcesAfterUserConfirmation() {
		int discarding = incomingResources.size();
		incomingResources.clear();
		positionsIncomingResources.clear();
		return discarding;
	}
	
	/**
	 * enables the arrays containing the additional resources. The resource is empty if the ability is not enabled yet.
	 * Otherwise its value corresponds to the chosen resource.
	 * @param resources the additional resources defined for the leader ability
	 */
	public void enableAdditionalDepot(ArrayList<Resources> resources) {
		int size = resources.size(), whichLeader = 0;
		if (!isLeaderActivated(0)) { // if first leader is not activated
			extraDepotResources.set(0, resources);
		} else if (!isLeaderActivated(1)) { // if second leader is not activated
			extraDepotResources.set(1, resources);
			whichLeader = 1;
		}
		
		for (int i = 0; i < size; i++) {
			extraDepotContents.get(whichLeader).add(false); //initialization of slots flags, one slot per resource
		}
	}
	
	/**
	 * Moves the resources to both additional depots automatically, giving priority to the leader specified in input
	 * @param priorityLeader the number of the leader card depot to prioritize when assigning resources (1 or 2)
	 */
	public void moveResourcesToAdditionalDepots(int priorityLeader) throws InvalidUserRequestException {
		if (priorityLeader == 1) {
			moveResourcesToLeaderDepot(0);
			moveResourcesToLeaderDepot(1);
		} else if (priorityLeader == 2) {
			moveResourcesToLeaderDepot(1);
			moveResourcesToLeaderDepot(0);
		} else throw new InvalidUserRequestException("the number of the leader card to prioritize must be 1 or 2.");
		
		//removes the empty spots in the incoming deck
		incomingResources = removeEmptySpaces(incomingResources);
	}
	
	/**
	 * this function moves automatically the biggest number of resources from the incoming deck to the additional depots, where possible.
	 * It works for 2 leaders, whether they are activated or not. For each leader, if enabled, it moves the resources inside,
	 * if there are any spots available. Otherwise, it does nothing if the resources can't be inserted. This method is automatic.
	 * 	 This function is called exclusively from the method above
	 * @param whichLeader the index of the leader to move the resources to (0 or 1)
	 */
	private void moveResourcesToLeaderDepot(int whichLeader) {
		if (isLeaderActivated(whichLeader)) { //selected depot is enabled
			
			for (int i = 0; i < incomingResources.size(); i++) { //cycles for every position in the incoming deck
				for (int j = 0; j < extraDepotResources.get(whichLeader).size(); j++) { //cycles for every slot in the extra depots
					
					if (incomingResources.get(i) == extraDepotResources.get(whichLeader).get(j) &&
							!extraDepotContents.get(whichLeader).get(j)) {
						extraDepotContents.get(whichLeader).set(j, true);
						incomingResources.set(i, Resources.EMPTY);
					}
					
				}
			}
			
		}
	}
	
	
	
	/**
	 * analyzes the integer array equivalent of a list of resources
	 * @param pyramid the converted array
	 * @return true if the combination of resources is a valid one
	 */
	public boolean isCombinationCorrect(int[] pyramid) {
		if (pyramid[0] != 0) { // top shelf is not empty
			// top shelf has the same resource as middle shelf or top shelf
			if (pyramid[0] == pyramid[1] || pyramid[0] == pyramid[2] || pyramid[0] == pyramid[3] ||
					pyramid[0] == pyramid[4] || pyramid[0] == pyramid[5]) {
				return false;
			}
		}
		
		if (pyramid[1] != 0) { //middle shelf is not empty
			if (pyramid[1] != pyramid[2] && pyramid[2] != 0) { // first resource must be equal to the second one if not empty
				return false;
			} else if (pyramid[1] == pyramid[3] || pyramid[1] == pyramid[4] || pyramid[1] == pyramid[5]) {
				return false; // resources must be different in different shelves
			}
		} else { // middle shelf is empty
			if (pyramid[2] != 0) { //if the first spot is not occupied, then also the second one should not be occupied
				if (pyramid[2] == pyramid[3] || pyramid[2] == pyramid[4] || pyramid[2] == pyramid[5]) {
					return false;
				}
			}
		}
		
		if (pyramid[3] != 0) { // bottom shelf is not empty
			if (pyramid[3] != pyramid[4] && pyramid[4] != 0) { // different resources in the same shelf
				return false;
			} else if (pyramid[3] != pyramid[5] && pyramid[5] != 0) { // different resources in the same shelf
				return false;
			} else return pyramid[4] == pyramid[5] || pyramid[4] == 0 || pyramid[5] == 0;
		} else { // bottom shelf is empty
			if (pyramid[4] != 0) { // if first resource is empty, then also the others must be empty as well
				return pyramid[4] == pyramid[5] || pyramid[5] == 0;
			}
		}
		
		return true; //if the conditions above are not satisfied
	}
	
	/**
	 * transforms an array of resources into its corresponding integer array values
	 * @param list of resources to be converted
	 * @return converted list
	 */
	public int[] getConvertedList(Resources[] list) {
		assert list.length == 6;
		int[] depotConverted = new int[6];
		for (int i = 0; i < 6; i++) {
			switch (list[i]) {
				case COIN -> depotConverted[i] = 1;
				case STONE -> depotConverted[i] = 2;
				case SERVANT -> depotConverted[i] = 3;
				case SHIELD -> depotConverted[i] = 4;
				default -> depotConverted[i] = 0; // EMPTY resource
			}
		}
		return depotConverted;
		
	}
	
	/**
	 * returns the list of all the resources contained in the data structures
	 * @return the list of all the resources in the warehouse depot and the additional depots for each player
	 */
	public ArrayList<Resources> gatherAllResources() {
		ArrayList<Resources> completeList = new ArrayList<>(Arrays.asList(depot));
		completeList = removeEmptySpaces(completeList);
		
		for (int leader = 0; leader < 2; leader++) {
			for (int i = 0; i < extraDepotResources.get(leader).size(); i++) {
				if (isLeaderActivated(leader)) {
					if (extraDepotContents.get(leader).get(i)) {
						completeList.add(extraDepotResources.get(leader).get(i));
					}
				}
			}
		}
		return completeList;
	}
	
	/**
	 * removes the EMPTY resource from a list of resources
	 * @param list input
	 * @return the same list without the empty resources
	 */
	private ArrayList<Resources> removeEmptySpaces(ArrayList<Resources> list) {
		//removes the empty spots in the incoming deck
		return (ArrayList<Resources>) list.stream().
				filter((res) -> !res.equals(Resources.EMPTY)).
				collect(Collectors.toList());
	}
	
	/**
	 * check for the leader activation flag
	 * @param whichLeader first leader is 0; second leader is 1.
	 * @return true if the leader has been activated, false otherwise
	 */
	private boolean isLeaderActivated(int whichLeader) {
		return extraDepotResources.get(whichLeader).get(0) != Resources.EMPTY;
	}
	
	
	/**
	 * remove resources form the depots in order to pay for a new development cards
	 * @param price the "price" to be payed is a list of resources needed for buying a new development card
	 * @return the resources that are still not payed
	 */
	public ArrayList<Resources> payResources(ArrayList<Resources> price) {
		// first checks the pyramid to see if there are some of the required resources
		for (int i = 0; i < 6; i++) {
			if (price.contains(depot[i])) {
				price.remove(depot[i]);
				depot[i] = Resources.EMPTY;
			}
		}
		
		//then checks the additional depots
		for (int leader = 0; leader < 2; leader++) { // iterates for the leader which may be enabled
			if (isLeaderActivated(leader)) { //selected depot is enabled
				for (int i = 0; i < price.size(); i++) { // iterates for all the resources left to be payed
					
					//first index of the resource in the additional depot
					int elementFound = extraDepotResources.get(leader).indexOf(price.get(i));
					if (elementFound >= 0) {
						//searches through every occurrence of the object
						for (int j = elementFound; j <= extraDepotResources.get(leader).lastIndexOf(price.get(i)); j++) {
							//if the element is sored in the depot
							if (extraDepotContents.get(leader).get(j)) {
								//removes the element for the input list and the list of contents
								extraDepotContents.get(leader).set(j, false);
								price.remove(i);
								break; // next element in the price list
							}
						}
					}
					
				}
			}
		}
		
		return price;
	}

	
	
	public int getResourceDeckSize() {
		return incomingResources.size();
	}
	
	public Resources[] getDepot() {
		return depot;
	}
	
	public void setDepotForDebugging(Resources[] depot) {
		this.depot = depot;
	}
	
	public ArrayList<Integer> getPositionsIncomingResources() {
		return positionsIncomingResources;
	}
	public ArrayList<Resources> getIncomingResources() {
		return incomingResources;
	}
	
	public ArrayList<ArrayList<Boolean>> getExtraDepotContents() {
		return extraDepotContents;
	}
}
