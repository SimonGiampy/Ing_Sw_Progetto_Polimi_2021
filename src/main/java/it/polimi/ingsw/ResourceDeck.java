package it.polimi.ingsw;

import java.util.ArrayList;

public class ResourceDeck {

	private ArrayList<Resources> resourceList;
	private ArrayList<Resources> fromWhiteMarble1;
	private ArrayList<Resources> fromWhiteMarble2;
	private int whiteMarblesInput1;
	private int whiteMarblesInput2;
	private int faithPoint;
	
	WarehouseDepot playerDepot;

	/**
	 * Constructor that assigns default values to the parameters
	 */
	public ResourceDeck(WarehouseDepot depot) {

		resourceList = new ArrayList<>();
		fromWhiteMarble1 = new ArrayList<>();
		fromWhiteMarble1.add(Resources.EMPTY);
		fromWhiteMarble2 = new ArrayList<>();
		fromWhiteMarble2.add(Resources.EMPTY);

		faithPoint = 0;
		whiteMarblesInput1 = 0;
		whiteMarblesInput2 = 0;
		
		playerDepot = depot;
	}

	/**
	 * Add resources to the list based on the colors of the marbles taken in the market
	 * @param marblesFromMarket array of the marbles
	 */
	protected void addResources(Marbles[] marblesFromMarket)  {
		if(fromWhiteMarble1.get(0) == Resources.EMPTY)
			addResources(marblesFromMarket, 0, 0);
		else if(fromWhiteMarble2.get(0) == Resources.EMPTY)
			addResources(marblesFromMarket, 4, 0);
	}

	/**
	 * Handles the case of a player playing two leader cards with the white marble ability, if this player
	 * get white marbles from the market he can choose which resource to get
	 * @param marblesFromMarket array of the marbles
	 * @param quantityLeader1 number of times the player wants to activate the power of the first leader played
	 * @param quantityLeader2 number of times the player wants to activate the power of the second leader played
	 */
	protected void addResources(Marbles[] marblesFromMarket, int quantityLeader1, int quantityLeader2) {
		faithPoint = 0;
		int whiteMarbleCount = 0;
		for(Marbles x: marblesFromMarket) {
			switch (x) {
				case YELLOW -> resourceList.add(Resources.COIN);
				case BLUE -> resourceList.add(Resources.SHIELD);
				case GREY -> resourceList.add(Resources.STONE);
				case PURPLE -> resourceList.add(Resources.SERVANT);
				case RED -> faithPoint = 1;
				case WHITE -> {
					whiteMarbleCount++;
					if(fromWhiteMarble1.get(0) != Resources.EMPTY) {
						if (quantityLeader1 > 0 && whiteMarbleCount == whiteMarblesInput1) {
							resourceList.addAll(fromWhiteMarble1);
							quantityLeader1--;
							whiteMarbleCount = 0;
						} else if (quantityLeader2 > 0 && whiteMarbleCount == whiteMarblesInput2) {
							resourceList.addAll(fromWhiteMarble2);
							quantityLeader2--;
							whiteMarbleCount = 0;
						}
					}
				}
			}
		}
		
		playerDepot.addIncomingResources(getResourceList()); // sends the resources list from the market to the player warehouse depot
	}

	/**
	 * When a leader card with white marble ability is played, sets the number of white marbles in input and the
	 * resources in output
	 * @param res resource specified by the leader played
	 */
	public void setFromWhiteMarble(ArrayList<Resources> res, int whiteMarblesInput) {
		if(fromWhiteMarble1.get(0) == Resources.EMPTY){
			fromWhiteMarble1 = res;
			whiteMarblesInput1 = whiteMarblesInput;
		} else if(fromWhiteMarble2.get(0) == Resources.EMPTY){
			fromWhiteMarble2 = res;
			whiteMarblesInput2 = whiteMarblesInput;

		}

	}
	
	protected ArrayList<Resources> getResourceList(){
		return resourceList;
	}

	protected int getFaithPoint() {
		return faithPoint;
	}

}
