package it.polimi.ingsw;

import java.util.ArrayList;

public class ResourceDeck {

	private ArrayList<Resources> resourceList;
	private Resources whiteMarble1;
	private Resources whiteMarble2;
	int faithPoint;

	/**
	 * Constructor that assigns default values to the parameters
	 */
	public ResourceDeck(){

		resourceList = new ArrayList<>();
		whiteMarble1 = Resources.NO_RESOURCE;
		whiteMarble2= Resources.NO_RESOURCE;
		faithPoint = 0;
	}

	/**
	 * Add resources to the list based on the colors of the marbles taken in the market
	 * @param marblesFromMarket array of the marbles
	 */

	protected void addResources(Marbles[] marblesFromMarket){
		faithPoint = 0;
		for(Marbles x: marblesFromMarket) {
			switch (x) {
				case YELLOW -> resourceList.add(Resources.COIN);
				case BLUE -> resourceList.add(Resources.SHIELD);
				case GREY -> resourceList.add(Resources.STONE);
				case VIOLET -> resourceList.add(Resources.SERVANT);
				case RED -> faithPoint = 1;
				case WHITE -> resourceList.add(whiteMarble1);
			}
		}
	}

	/**
	 * Handles the case of a player playing two leader cards with the white marble ability, if this player
	 * get white marbles from the market he can choose which resource to get
	 * @param marblesFromMarket array of the marbles
	 * @param quantity1 quantity of whiteMarble1, the resource of the first leader who has been played
	 */

	protected void addResources(Marbles[] marblesFromMarket, int quantity1){
		faithPoint = 0;
		for(Marbles x: marblesFromMarket){
			switch(x){
				case YELLOW:
					resourceList.add(Resources.COIN);
					break;
				case BLUE:
					resourceList.add(Resources.SHIELD);
					break;
				case GREY:
					resourceList.add(Resources.STONE);
					break;
				case VIOLET:
					resourceList.add(Resources.SERVANT);
					break;
				case RED:
					faithPoint = 1;
					break;
				case WHITE:
					if(quantity1 > 0){
						resourceList.add(whiteMarble1);
						quantity1--;
					}
					else
						resourceList.add(whiteMarble2);
					break;
			}
		}
	}

	/**
	 * When a leader card with white marble ability is played, this set whiteMarble with the corresponding resource
	 * @param res resource specified by the leader played
	 */
	protected void setWhiteMarble(Resources res){
		if(whiteMarble1 == Resources.NO_RESOURCE)
			whiteMarble1 = res;
		else
			whiteMarble2 = res;
	}


	protected ArrayList<Resources> getResourceList(){
		return resourceList;
	}

	protected int getFaithPoint(){
		return faithPoint;
	}

}
