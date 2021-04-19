package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.ListSet;
import it.polimi.ingsw.model.util.Resources;
import it.polimi.ingsw.model.util.Unicode;

import java.util.ArrayList;

/**
 * this class handles single card management
 */
public class DevelopmentCard {
	
	private final int level,victoryPoints;
	private final Colors color;
	private final ArrayList<Resources> resourcesRequirement;
	private final ProductionRules productionRules;
	
	/**
	 * constructor for instantiating card's attributes
	 * @param level is the level of the card
	 * @param color is the color of the card
	 * @param victoryPoints is the number of card's victory points
	 * @param resourceList is a list of needed resources to buy the card
	 * @param productionRules is the instance of the corresponding production rules
	 */
	
	public DevelopmentCard(int level, Colors color, int victoryPoints, ArrayList<Resources> resourceList, ProductionRules productionRules) {
		this.level = level;
		this.color = color;
		this.victoryPoints = victoryPoints;
		this.resourcesRequirement = resourceList;
		this.productionRules = productionRules;
	}
	
	/**
	 * getter for the attribute level
	 * @return card's level
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * getter for the attribute victoryPoints
	 * @return the number of Victory points
	 */
	public int getVictoryPoints(){
		return victoryPoints;
	}

	/**
	 * too lazy to write something here
	 * @return //
	 */
	public ArrayList<Resources> getResourcesRequirement(){
		return resourcesRequirement;
	}
	
	/**
	 * getter for card's color
	 * @return the color of the card
	 */
	public Colors getColor(){
		return color;
	}

	public ProductionRules getProductionRules() {
		return productionRules;
	}

	private void appendTopFrame(StringBuilder string){
		string.append(color.getColorCode());
		string.append(Unicode.TOP_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(26));
		string.append(Unicode.TOP_RIGHT);
		string.append("\n");
		string.append(Unicode.RESET);
	}
	private void appendBottomFrame(StringBuilder string){
		string.append(color.getColorCode());
		string.append(Unicode.BOTTOM_LEFT);
		string.append(String.valueOf(Unicode.HORIZONTAL).repeat(26));
		string.append(Unicode.BOTTOM_RIGHT);
		string.append("\n");
		string.append(Unicode.RESET);
	}

	private void appendFirstLine(StringBuilder string){
		string.append("  LVL ");
		if(level==1)
			string.append(Unicode.DOT+"    ");
		else if(level==2)
			string.append(Unicode.DOT+" "+Unicode.DOT+"  ");
		else
			string.append(Unicode.DOT+" "+Unicode.DOT+" "+Unicode.DOT);
		string.append(Unicode.RESET+"        ");
		string.append(getProductionRules().getFaithOutput());
		string.append(Unicode.RED_BOLD.toString()+Unicode.CROSS2+Unicode.RESET+" ");
		string.append(victoryPoints+Resources.VICTORY_POINTS+" ");
		string.append("\n");
	}

	private void appendSecondLine(StringBuilder string){
		string.append("  REQs ");
		string.append(ListSet.showListMultiplicityOnConsole(resourcesRequirement));
		string.append("\n");
	}

	private void appendThirdLine(StringBuilder string){
		string.append("  In : ");
		string.append(ListSet.showListMultiplicityOnConsole(getProductionRules().getInputCopy()));
		string.append("\n");
	}

	private void appendFourthLine(StringBuilder string){
		string.append("  Out: ");
		string.append(ListSet.showListMultiplicityOnConsole(getProductionRules().getOutputCopy()));
		string.append("\n");
	}
	public void showCard(){
		StringBuilder string= new StringBuilder();
		appendTopFrame(string);
		appendFirstLine(string);
		appendSecondLine(string);
		appendThirdLine(string);
		appendFourthLine(string);
		appendBottomFrame(string);
		System.out.println(string);
	}
}
