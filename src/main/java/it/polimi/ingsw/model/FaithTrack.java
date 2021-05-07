package it.polimi.ingsw.model;

import java.util.ArrayList;

public class FaithTrack {
	
	private final ArrayList<Boolean> vaticanReports;
	private final ArrayList<Boolean> reportsActivated;
	private Integer currentPosition;
	private final ArrayList<Tile> track;
	private final ArrayList<Integer> reportPoints;
	private final boolean singlePlayer;
	private int lastReportClaimed;
	
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
		reportsActivated = new ArrayList<>();
		for(Integer ignored : reportPoints) {
			vaticanReports.add(false);
			reportsActivated.add(false);
		}
		this.reportPoints = reportPoints;
		this.singlePlayer = singlePlayer;
	}

	
	/**
	 * Move the marker and control if the current player triggers a Papal Report
	 * @param faithPoints the number of faith points to be moved
	 */
	public void moveMarker(int faithPoints) {
		while(faithPoints > 0) {
			if(!isTrackFinished()) {
				currentPosition = currentPosition + 1;
				if(track.get(currentPosition).isPapalSpace() && lastReportClaimed < reportPoints.size()
						&& track.get(currentPosition).isInsideVatican(lastReportClaimed)) {
					reportsActivated.set(lastReportClaimed, true);
					lastReportClaimed++;
				}
			}
			faithPoints--;
		}
	}
	
	
	/**
	 * Checks if the player activated the selected report
	 * @return true if they activated it
	 */
	public boolean checkActivationVaticanReport(int reportNumber){
		return reportsActivated.get(reportNumber);
	}

	/**
	 * Checks if the player is in the right position to activate the report
	 * @param reportNumber number of the report
	 */
	public void checkVaticanReport(int reportNumber){
		if (reportNumber < reportPoints.size() && track.get(currentPosition).isInsideVatican(reportNumber))
			vaticanReports.set(reportNumber, true);
		if(reportNumber == lastReportClaimed){
			lastReportClaimed++;
		}
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
		totalPoints = totalPoints + track.get(currentPosition).getVictoryPoints();
		
		return totalPoints;
	}

	/**
	 * Checks if the player is in the last tile of the track
	 * @return true if the player have finished the track
	 */
	public boolean isTrackFinished(){
		return currentPosition == track.size()-1;
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

	public int getLastReportClaimed() {
		return lastReportClaimed;
	}

}
