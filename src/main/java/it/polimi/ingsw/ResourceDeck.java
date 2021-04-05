package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidInputException;
import it.polimi.ingsw.exceptions.InvalidUserRequestException;

import java.util.ArrayList;
import java.util.Arrays;

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
	 * Handles the case of a player playing two leader cards with the white marble ability, if this player
	 * get white marbles from the market he can choose which resource to get
	 * @param marblesFromMarket array of the marbles
	 * @param quantityLeader1 number of times the player wants to activate the power of the first leader played
	 * @param quantityLeader2 number of times the player wants to activate the power of the second leader played
	 */
	protected void addResources(Marbles[] marblesFromMarket, int quantityLeader1, int quantityLeader2)
			throws InvalidUserRequestException {
		int whiteMarblesFromMarket = ListSet.count(new ArrayList<>(Arrays.asList(marblesFromMarket)), Marbles.WHITE);
		
		//Throws exception if the total of the white marbles required to activate the leaders ability is different from
		//      the number of white marbles to be processed with the leader card abilities
		if (isWhiteAbility1Activated() || isWhiteAbility2Activated()) {
			if ((quantityLeader1 * whiteMarblesInput1 + quantityLeader2 * whiteMarblesInput2) > whiteMarblesFromMarket) {
				throw new InvalidUserRequestException("Invalid number of activations of leaders ability");
			}
		}
		
		//Convert marble into resources based on their color
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
		//else throw new InvalidInputException("two white marbles abilities already activated by the player");
	}
	
	public ArrayList<Resources> getResourceList(){
		return resourceList;
	}

	public int getFaithPoint() {
		return faithPoint;
	}

	public ArrayList<Resources> getFromWhiteMarble1() {
		return fromWhiteMarble1;
	}

	public ArrayList<Resources> getFromWhiteMarble2() {
		return fromWhiteMarble2;
	}

	public int getWhiteMarblesInput1() {
		return whiteMarblesInput1;
	}

	public int getWhiteMarblesInput2() {
		return whiteMarblesInput2;
	}
	
	public boolean isWhiteAbility1Activated() {
		return getWhiteMarblesInput1() > 0;
	}
	
	public boolean isWhiteAbility2Activated() {
		return getWhiteMarblesInput2() > 0;
	}
	
}
