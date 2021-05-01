package it.polimi.ingsw.model;

import it.polimi.ingsw.model.util.Unicode;

import java.util.ArrayList;

public class FaithTrack {
	
	private final ArrayList<Boolean> vaticanReports;
	private Integer currentPosition;
	private final ArrayList<Tile> track;
	private final ArrayList<Integer> reportPoints;
	
	private final boolean singlePlayer;
	
	/**
	 * Constructor that initializes the entire faith track
	 * @param tiles all the tiles contained in the faith track
	 * @param reportPoints the points gained from each report
	 * @param singlePlayer flag indicating if this is a single player match
	 */
	public FaithTrack(ArrayList<Tile> tiles, ArrayList<Integer> reportPoints, boolean singlePlayer) {
		track = tiles;
		currentPosition = 0;
		vaticanReports = new ArrayList<>();
		for(Integer ignored : reportPoints)
			vaticanReports.add(false);
		this.reportPoints = reportPoints;
		this.singlePlayer = singlePlayer;
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

/*	*//**
	 * Show all the track with the current position, the activated reports, the victory points for every tile
	 * and the number that identifies the tiles
	 *//*
	public void showFaithTrack(int marker){

		StringBuilder string = new StringBuilder();

		StringBuilder markerColor = new StringBuilder();
		if(marker == 1)
			markerColor.append(Unicode.RED_BOLD);
		else
			markerColor.append(Unicode.BLACK_BOLD);
		//Row for vatican reports
		int count = 0;
		for(int i = 0; i < reportPoints.size(); i++) {
			while (!track.get(count).isPapalSpace()) {
				string.append("      ");
				count++;
			}
			*//*if(i > lastReportClaimed){
				string.append(Unicode.BLUE_BOLD).append("   ?  ");

			 *//*
			if(vaticanReports.get(i)){
				string.append(Unicode.GREEN_BOLD).append("  ").append(Unicode.TICK).append(" ").append(Unicode.RESET)
						.append(reportPoints.get(i));
			}else{
				string.append(Unicode.RED_BOLD).append("  ").append(Unicode.CROSS_REPORT).append(" ").append(Unicode.RESET)
						.append(reportPoints.get(i));
			}
			//✖ ✕ ✔ ✓
			count++;
		}
		string.append("\n").append(Unicode.RESET);

		//First row of "-"
		appendTopFrame(string);

		//Second row of "|", spaces and marker
		int k = 0;
		String currentColor = Unicode.RESET;
		for(int i = 0; i < track.size(); i++){
			if (k < reportPoints.size() && track.get(i).isInsideVatican(k)) {
				currentColor = Unicode.ANSI_YELLOW.toString();
				k++;
			}
			if(track.get(i).isPapalSpace()){
				currentColor = Unicode.ANSI_RED.toString();
			}
			if(i > 1 && track.get(i-2).isPapalSpace()){
				currentColor = Unicode.RESET;
			}
			if(i == currentPosition){
				string.append(currentColor).append(Unicode.VERTICAL).append(Unicode.WHITE_BACKGROUND_BRIGHT).append("  ");
				string.append(markerColor).append(Unicode.CROSS_MARKER)
						.append("  ").append(currentColor);
			}
			else{
				string.append(currentColor).append(Unicode.VERTICAL).append("  ");
				string.append("   ");
			}
		}
		string.append(currentColor).append(Unicode.VERTICAL).append("\n").append(Unicode.RESET);

		//Third row of "|", spaces and victory points (printed only in the tile where they change)
		int currentVictoryPoints = -1;
		int l = 0;
		currentColor = Unicode.RESET;
		for(int i = 0; i < track.size(); i++){
			if (l < reportPoints.size() && track.get(i).isInsideVatican(l)) {
				currentColor = Unicode.ANSI_YELLOW.toString();
				l++;
			}
			if(track.get(i).isPapalSpace()){
				currentColor = Unicode.ANSI_RED.toString();
			}
			if(i > 1 && track.get(i-2).isPapalSpace()){
				currentColor = Unicode.RESET;
			}
			string.append(currentColor).append(Unicode.VERTICAL);
			if(i==currentPosition) {
				string.append(Unicode.WHITE_BACKGROUND_BRIGHT);
			}
			string.append("  ");
			if(track.get(i).tilePoints() != currentVictoryPoints){
				if(i==currentPosition) {
					string.append(Unicode.BLACK_BOLD).append(track.get(i).tilePoints()).append(Unicode.WHITE_BACKGROUND_BRIGHT);
				}else{
					string.append(Unicode.RESET).append(track.get(i).tilePoints());
				}
				if(track.get(i).tilePoints() < 10){
					string.append("  ");
				}
				else{
					string.append(" ");
				}
				string.append(currentColor);
				currentVictoryPoints = track.get(i).tilePoints();
			}else{
				string.append("   ");
			}
			string.append(Unicode.RESET);
		}
		string.append(Unicode.ANSI_RED).append(Unicode.VERTICAL).append("\n").append(Unicode.RESET);

		//Last row of "-"
		appendBottomFrame(string);

		//Number of the tiles
		string.append(Unicode.RESET);
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

	*//**
	 * Support method for showFaithTrack, appends to the string builder the "-" lines that form the top and the bottom
	 * of the track
	 * @param string string builder containing all the representation of the track
	 *//*
	private void appendTopFrame(StringBuilder string) {
		int j = 0;
		String s = Unicode.TOP_LEFT.toString();
		for(int i = 0; i < track.size(); i++){
			if (j < reportPoints.size() && track.get(i).isInsideVatican(j)) {
				string.append(Unicode.ANSI_YELLOW);
				j++;
			}
			if(track.get(i).isPapalSpace()){
				string.append(Unicode.ANSI_RED);
			}
			if(i > 0 && track.get(i-1).isPapalSpace()){
				string.append(Unicode.T_SHAPE).append(Unicode.RESET).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}else {
				string.append(s).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}
			s = Unicode.T_SHAPE.toString();
		}
		string.append(Unicode.TOP_RIGHT).append("\n").append(Unicode.RESET);
	}


	private void appendBottomFrame(StringBuilder string) {
		int j = 0;
		String s = Unicode.BOTTOM_LEFT.toString();
		for(int i = 0; i < track.size(); i++){
			if (j < reportPoints.size() && track.get(i).isInsideVatican(j)) {
				string.append(Unicode.ANSI_YELLOW);
				j++;
			}
			if(track.get(i).isPapalSpace()){
				string.append(Unicode.ANSI_RED);
			}
			if(i > 0 && track.get(i-1).isPapalSpace()){
				string.append(Unicode.REVERSE_T_SHAPE).append(Unicode.RESET).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}else {
				string.append(s).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL)
						.append(Unicode.HORIZONTAL).append(Unicode.HORIZONTAL);
			}
			s = Unicode.REVERSE_T_SHAPE.toString();
		}
		string.append(Unicode.BOTTOM_RIGHT).append("\n").append(Unicode.RESET);
	}*/

	
	/**
	 * Shows a legend to help the understanding of the graphic representation of the track
	 */
	public void helpMe(){
		System.out.println("HELP:");
		System.out.println(Unicode.RED_BOLD + "+" + Unicode.RESET + " is your marker");
		System.out.println("The number " + Unicode.UNDERLINE+ "underneath" + Unicode.RESET +
				" the tile indicate its position in the track");
		System.out.println("The number "+ Unicode.UNDERLINE + "inside" + Unicode.RESET +" the tile is the amount of " +
				"Victory Points you get if you are on it or after it at the end of the game");
		System.out.println(Unicode.ANSI_YELLOW + "Yellow" + Unicode.RESET + " frame indicates that this tile is " +
				"part of a Vatican Zone");
		System.out.println(Unicode.ANSI_RED + "Red" + Unicode.RESET + " frame indicates that this tile is a Papal Space");
		System.out.println("[" + Unicode.GREEN_BOLD+ "✓"+ Unicode.RESET + "/" + Unicode.RED_BOLD+"X"+Unicode.RESET +
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
	
	public boolean isSinglePlayer() {
		return singlePlayer;
	}
}
