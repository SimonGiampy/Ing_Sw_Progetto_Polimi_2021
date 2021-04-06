package it.polimi.ingsw;

import java.util.ArrayList;

public class FaithTrack {
	
	private ArrayList<Boolean> vaticanReports;
	private Integer currentPosition;
	private ArrayList<Tile> track;
	private final ArrayList<Integer> reportPoints;
	private int lastReportClaimed;	//Attribute of support for showFaithTrack
	
	
	/**
	 * Constructor that initializes the entire faith track
	 * @param tiles all the tiles contained in the faith track
	 * @param reportPoints the points gained from each report
	 */
	public FaithTrack(ArrayList<Tile> tiles, ArrayList<Integer> reportPoints) {
		track = tiles;
		currentPosition = 0;
		vaticanReports = new ArrayList<>();
		for(Integer ignored : reportPoints)
			vaticanReports.add(false);
		this.reportPoints = reportPoints;
		lastReportClaimed = -1;
	}

	
	/**
	 * Move the marker and control if the current player triggers a Papal Report
	 * @param faithPoints the number of faith points to be moved
	 * //@return true if the report has been claimed
	 */
	public void moveMarker(int faithPoints) {
		while(faithPoints > 0) {
			if(!isTrackFinished())
				currentPosition = currentPosition + 1;
			faithPoints--;
		}
	}
	
	
	/**
	 * When someone triggers one report, checks if this player is in the right position update the vaticanReports array
	 */
	public boolean checkVaticanReport(int reportNumber){
		if (reportNumber < reportPoints.size() && track.get(currentPosition).isInsideVatican(reportNumber)) {
			vaticanReports.set(reportNumber, true);
			lastReportClaimed = reportNumber;
			return true;
		}
		return false;
	}
	
	/**
	 * Count victory points when the game ends
	 * @return the number of victory points
	 */
	public int countFaithTrackVictoryPoints(){
		int totalPoints = 0;
		
		//Add points of the reports
		for(int i = 0; i< vaticanReports.size(); i++){
			if(vaticanReports.get(i)) {
				totalPoints = totalPoints + reportPoints.get(i);
			}
		}

		//Add points of the current position
		totalPoints = totalPoints + track.get(currentPosition).tilePoints();
		
		return totalPoints;
	}

	/**
	 * Checks if the player is in the last tile of the track
	 * @return true if the player have finished the track
	 */
	public boolean isTrackFinished(){
		return currentPosition == track.size();
	}

	/**
	 * Show all the track with the current position, the activated reports, the victory points for every tile
	 * and the number that identifies the tiles
	 */
	public void showFaithTrack(){

		StringBuilder string = new StringBuilder();

		//Row for vatican reports
		int count = 0;
		for(int i = 0; i < reportPoints.size(); i++) {
			while (!track.get(count).isPapalSpace()) {
				string.append("      ");
				count++;
			}
			/*if(i > lastReportClaimed){
				string.append(Colors.BLUE_BOLD).append("   ?  ");

			 */
			if(vaticanReports.get(i)){
				string.append(Colors.GREEN_BOLD).append("  ✓ ").append(Colors.RESET)
						.append(reportPoints.get(i)).append(" ");
			}else{
				string.append(Colors.RED_BOLD).append("  X ").append(Colors.RESET)
						.append(reportPoints.get(i)).append(" ");
			}
			//✖ ✕ ✔ ✓
			count++;
		}
		string.append("\n").append(Colors.RESET);

		//First row of "-"
		appendFrame(string);

		//Second row of "|", spaces and marker
		int k = 0;
		for(int i = 0; i < track.size(); i++){
			if (k < reportPoints.size() && track.get(i).isInsideVatican(k)) {
				string.append(Colors.ANSI_YELLOW);
				k++;
			}
			if(track.get(i).isPapalSpace()){
				string.append(Colors.ANSI_RED);
			}
			if(i > 1 && track.get(i-2).isPapalSpace()){
				string.append(Colors.RESET);
			}
			string.append("|  ");
			if(i == currentPosition){
				string.append(Colors.RED_BOLD + "+  " + Colors.RESET);
			}
			else{
				string.append("   ");
			}
		}
		string.append("|\n").append(Colors.RESET);
		//Third row of "|", spaces and victory points (printed only in the tile there they change)
		int currentVictoryPoints = -1;
		int l = 0;
		String currentColor = Colors.RESET;
		for(int i = 0; i < track.size(); i++){
			if (l < reportPoints.size() && track.get(i).isInsideVatican(l)) {
				currentColor = Colors.ANSI_YELLOW;
				l++;
			}
			if(track.get(i).isPapalSpace()){
				currentColor = Colors.ANSI_RED;
			}
			if(i > 1 && track.get(i-2).isPapalSpace()){
				currentColor = Colors.RESET;
			}
			string.append(currentColor).append("|  ");
			if(track.get(i).tilePoints() != currentVictoryPoints){
				string.append(Colors.RESET).append(track.get(i).tilePoints()).append(currentColor);
				if(track.get(i).tilePoints() < 10){
					string.append("  ");
				}
				else{
					string.append(" ");
				}
				currentVictoryPoints = track.get(i).tilePoints();
			}else{
				string.append("   ");
			}
		}
		string.append("|\n");
		string.append(Colors.RESET);

		//Last row of "-"
		appendFrame(string);

		//Number of the tiles

		string.append(Colors.RESET);
		for(int i = 0; i < track.size(); i++){
			string.append("   ").append(i);
			if(i < 10){
				string.append("  ");
			}
			else{
				string.append(" ");
			}
		}
		System.out.println(string);

	}

	/**
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the top and the bottom
	 * of the track
	 * @param string string builder containing all the representation of the track
	 */
	private void appendFrame(StringBuilder string) {
		int j = 0;
		for(int i = 0; i < track.size(); i++){
			if (j < reportPoints.size() && track.get(i).isInsideVatican(j)) {
				string.append(Colors.ANSI_YELLOW);
				j++;
			}
			if(track.get(i).isPapalSpace()){
				string.append(Colors.ANSI_RED);
			}
			if(i > 0 && track.get(i-1).isPapalSpace()){
				string.append("-").append(Colors.RESET).append("-----");
			}else {
				string.append("------");
			}
		}
		string.append("-\n").append(Colors.RESET);
	}

	/**
	 * Shows a legend to help the understanding of the graphic representation of the track
	 */
	public void helpMe(){
		System.out.println("HELP:");
		System.out.println(Colors.RED_BOLD + "+" + Colors.RESET + " is your marker");
		System.out.println("The number " + Colors.UNDERLINE+ "underneath" + Colors.RESET +
				" the tile indicate its position in the track");
		System.out.println("The number "+ Colors.UNDERLINE + "inside" + Colors.RESET +" the tile is the amount of " +
				"Victory Points you get if you are on it or after it at the end of the game");
		System.out.println(Colors.ANSI_YELLOW + "Yellow" + Colors.RESET + " frame indicates that this tile is " +
				"part of a Vatican Zone");
		System.out.println(Colors.ANSI_RED + "Red" + Colors.RESET + " frame indicates that this tile is a Papal Space");
		System.out.println("[" + Colors.GREEN_BOLD+ "✓"+ Colors.RESET + "/" + Colors.RED_BOLD+"X"+Colors.RESET +
				" Num] shows if you are currently getting Victory Points from that Vatican Report" +
				" and the amount of Victory Points");
	}
	
	public ArrayList<Tile> getTrack(){
		return track;
	}

	public int getCurrentPosition(){
		return currentPosition;
	}
	
	public boolean getVaticanReport(int num){
		return vaticanReports.get(num-1);
	}

	public ArrayList<Integer> getReportPoints() {
		return reportPoints;
	}
	
	public ArrayList<Boolean> getVaticanReports() {
		return vaticanReports;
	}


}



/*
NB: This is the code that will go in GameMechanics to make the reports work
if(a.moveMarker(num)){
        a.checkVaticanReport();
        b.checkVaticanReport();
        c.checkVaticanReport();
        d.checkVaticanReport();
        }

And this is the code to handle the simultaneous movement in the case of resource not stored from the market
        if(a.moveMarker(5) || b.moveMarker(4) || c.moveMarker(8)){
            a.checkVaticanReport();
            b.checkVaticanReport();
            c.checkVaticanReport();
            d.checkVaticanReport();
        }
 */