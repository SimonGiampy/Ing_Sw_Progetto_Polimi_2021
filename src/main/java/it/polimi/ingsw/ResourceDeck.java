package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ResourceDeck {

    private ArrayList<Resources> resourceList;
    private boolean whiteMarbleToCoin;
    private boolean whiteMarbleToStone;
    private boolean whiteMarbleToServant;
    private boolean whiteMarbleToShield;
    int faithPoint;

    public ResourceDeck(){

        resourceList = new ArrayList<Resources>();
        whiteMarbleToCoin = false;
        whiteMarbleToServant = false;
        whiteMarbleToShield = false;
        whiteMarbleToStone = false;
        faithPoint = 0;
    }

    protected void addResources(ArrayList<Marbles> marblesFromMarket){
        faithPoint = 0;
        ArrayList<Resources> resToAdd;
        resToAdd = marbleToResource(marblesFromMarket);
        resourceList = resToAdd;
    }

    protected ArrayList<Resources> marbleToResource(ArrayList<Marbles> marblesFromMarket){
        ArrayList<Resources> resToAdd;
        resToAdd = new ArrayList<Resources>();
        for(Marbles x: marblesFromMarket){
            switch(x){
                case YELLOW:
                    resToAdd.add(Resources.COIN);
                case BLUE:
                    resToAdd.add(Resources.SHIELD);
                case GREY:
                    resToAdd.add(Resources.STONE);
                case VIOLET:
                    resToAdd.add(Resources.SERVANT);
                case RED:
                    faithPoint = 1;
                case WHITE:
                    if(whiteMarbleToStone)
                        resToAdd.add(Resources.STONE);
                    if(whiteMarbleToShield)
                        resToAdd.add(Resources.SHIELD);
                    if(whiteMarbleToCoin)
                        resToAdd.add(Resources.COIN);
                    if(whiteMarbleToServant)
                        resToAdd.add(Resources.SERVANT);
            }

        }
        marblesFromMarket.clear();
        return resToAdd;
    }

    protected void setWhiteMarbleToCoin(){
        whiteMarbleToCoin = true;
    }

    protected void setWhiteMarbleToStone(){
        whiteMarbleToStone = true;
    }

    protected void setWhiteMarbleToServant(){
        whiteMarbleToServant = true;
    }

    protected void setWhiteMarbleToShield(){
        whiteMarbleToShield = true;
    }

    protected ArrayList<Resources> getResourceList(){
        return resourceList;
    }

}
