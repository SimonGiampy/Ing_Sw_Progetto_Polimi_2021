package it.polimi.ingsw;

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

	/*public void showCard(){
		/*
		String colorCode;
		String resetWhite = "\033[0m";
		switch(this.color){
			case GREEN -> colorCode = "\u001B[32m";
			case BLUE -> colorCode = "\u001B[34m";
			case YELLOW -> colorCode = "\u001B[33m";
			case PURPLE -> colorCode = "\u001B[35m";
			default -> throw new IllegalStateException("Unexpected value: " + this.color);
		}


		System.out.print("Resources requirements: ");
		ListSet.showListMultiplicityOnConsole(getResourcesRequirement());
		System.out.println("Card: level " + getLevel() + " and color " + getColor());
		productionRules.showProductionRulesInformation();
		System.out.println("Card victory points: "+ getVictoryPoints() + "\n");
	}
	// "\033[0m"; white
	/*
	PURPLE("\u001B[35m"), //purple color
	BLUE("\u001B[34m"), //blue color
	YELLOW("\u001B[33m"); //yellow color
	GREEN("\U001B[32M");

	*/

	public ProductionRules getProductionRules() {
		return productionRules;
	}

	public void appendTopFrame(StringBuilder string){
		string.append(color.getColorCode());
		string.append(Unicode.TOP_LEFT);
		for (int i = 0; i < 26; i++) {
			string.append(Unicode.HORIZONTAL);
		}
		string.append(Unicode.TOP_RIGHT);
		string.append("\n");
		string.append(Unicode.RESET);
	}
	public void appendBottomFrame(StringBuilder string){
		string.append(color.getColorCode());
		string.append(Unicode.BOTTOM_LEFT);
		for (int i = 0; i < 26; i++) {
			string.append(Unicode.HORIZONTAL);
		}
		string.append(Unicode.BOTTOM_RIGHT);
		string.append("\n");
		string.append(Unicode.RESET);
	}

	public void appendFirstLine(StringBuilder string){
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

	public void appendSecondLine(StringBuilder string){
		string.append("  REQs ");
		string.append(ListSet.showListMultiplicityOnConsole(resourcesRequirement));
		string.append("\n");
	}

	public void appendThirdLine(StringBuilder string){
		string.append("  In : ");
		string.append(ListSet.showListMultiplicityOnConsole(getProductionRules().getInputCopy()));
		string.append("\n");
	}

	public void appendFourthLine(StringBuilder string){
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

	public static void main(String[] args) {
		StringBuilder string = new StringBuilder();
		StringBuilder string2= new StringBuilder();
		ArrayList<Resources> resources=new ArrayList<>();
		resources.add(Resources.COIN);
		resources.add(Resources.SERVANT);
		resources.add(Resources.SHIELD);
		resources.add(Resources.SHIELD);
		resources.add(Resources.STONE);
		resources.add(Resources.FREE_CHOICE);

		DevelopmentCard dev= new DevelopmentCard(3,Colors.PURPLE,3,resources,new ProductionRules(resources,resources,1));
		dev.showCard();


		System.out.println("  LVL"+Unicode.GREEN_BOLD+" ● ● ●    "+ Unicode.RESET+"3"+Unicode.RED_BOLD+Unicode.CROSS2+Unicode.RESET+" 7"+Unicode.YELLOW_BOLD+"\uD83C\uDFC6"+Unicode.RESET);
		System.out.println("  REQs 3"+Unicode.YELLOW_BRIGHT+"\uD83D\uDCB0"+Unicode.RESET+" 2"+Unicode.PURPLE_BOLD+"\uD83D\uDE47"+Unicode.RESET+" 1"+Unicode.BLUE_BOLD+"\uD83D\uDEE1"+Unicode.RESET+" 4"+Unicode.BLACK_BOLD+"\uD83D\uDDFF"+Unicode.RESET);
		System.out.println("  In : 3"+Unicode.YELLOW_BRIGHT+"\uD83D\uDCB0 "+Unicode.RESET+"1"+Unicode.PURPLE_BOLD+"\uD83D\uDE47"+Unicode.RESET+" 1"+Unicode.BLACK_BOLD+"\uD83D\uDDFF"+Unicode.RESET+" 1"+Unicode.BLUE_BOLD+"\uD83D\uDEE1"+Unicode.RESET);
		System.out.println("  Out: 1"+Unicode.PURPLE_BOLD+"\uD83D\uDC64"+Unicode.RESET+" 1"+Unicode.BLACK_BOLD+"\uD83D\uDDFF"+Unicode.RESET);

		dev.appendBottomFrame(string2);
		System.out.println(string2);



	}
}
