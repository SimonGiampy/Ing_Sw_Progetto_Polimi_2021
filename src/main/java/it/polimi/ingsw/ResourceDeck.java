package it.polimi.ingsw;

import java.util.ArrayList;

public class ResourceDeck {

	private ArrayList<Resources> resourceList;
	private Resources whiteMarble1;
	private Resources whiteMarble2;
	int faithPoint;
	
	WarehouseDepot playerDepot;

	/**
	 * Constructor that assigns default values to the parameters
	 */
	public ResourceDeck(WarehouseDepot depot) {
		resourceList = new ArrayList<>();
		whiteMarble1 = Resources.NO_RESOURCE;
		whiteMarble2 = Resources.NO_RESOURCE;
		faithPoint = 0;
		
		playerDepot = depot;
	}

	/**
	 * Add resources to the list based on the colors of the marbles taken in the market
	 * @param marblesFromMarket array of the marbles
	 */
	protected void addResources(Marbles[] marblesFromMarket)  {
		addResources(marblesFromMarket, 0, 0);
	}

	/**
	 * Handles the case of a player playing two leader cards with the white marble ability, if this player
	 * get white marbles from the market he can choose which resource to get
	 * @param marblesFromMarket array of the marbles
	 * @param quantityLeader1 quantity of whiteMarble1, the resource of the first leader who has been played
	 * @param quantityLeader2 quantity of whiteMarble2, the resource of the second leader who has been played
	 */
	protected void addResources(Marbles[] marblesFromMarket, int quantityLeader1, int quantityLeader2) {
		faithPoint = 0;
		for(Marbles x: marblesFromMarket) {
			switch (x) {
				case YELLOW -> resourceList.add(Resources.COIN);
				case BLUE -> resourceList.add(Resources.SHIELD);
				case GREY -> resourceList.add(Resources.STONE);
				case PURPLE -> resourceList.add(Resources.SERVANT);
				case RED -> faithPoint = 1;
				case WHITE -> {
					if (quantityLeader1 > 0) {
						resourceList.add(whiteMarble1);
						quantityLeader1 --;
					} else if (quantityLeader2 > 0) {
						resourceList.add(whiteMarble2);
						quantityLeader2 --;
					}
					
				}
			}
		}
		
		playerDepot.addIncomingResources(getResourceList()); // sends the resources list from the market to the player warehouse depot
	}

	/**
	 * When a leader card with white marble ability is played, this set whiteMarble with the corresponding resource
	 * @param res resource specified by the leader played
	 */
	protected void setWhiteMarble(Resources res) {
		if (whiteMarble1 == Resources.NO_RESOURCE)
			whiteMarble1 = res;
		else {
			whiteMarble2 = res;
		}
		
	}
	
	protected ArrayList<Resources> getResourceList(){
		return resourceList;
	}

	protected int getFaithPoint() {
		return faithPoint;
	}

}
