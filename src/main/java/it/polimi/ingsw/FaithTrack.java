package it.polimi.ingsw;

import java.util.ArrayList;

public class FaithTrack {
	
	private ArrayList<Boolean> vaticanReports;
	private Integer currentPosition;
	private ArrayList<Tile> track;
	private final ArrayList<Integer> reportPoints;
	private int lastReportClaimed;
	
	
	/**
	 * Constructor that initializes the entire faith track
	 * @param tiles all the tiles contained in the faith track
	 * @param reportPoints the points gained from each report
	 */
	public FaithTrack(ArrayList<Tile> tiles, ArrayList<Integer> reportPoints) {
		track = tiles;
		currentPosition = 0;
		vaticanReports = new ArrayList<>();
		for(Integer i: reportPoints)
			vaticanReports.add(false);
		this.reportPoints = reportPoints;
		lastReportClaimed = 0;
	}

	
	/**
	 * Move the marker and control if the current player triggers a Papal Report
	 * @param faithPoints the number of faith points to be moved
	 * @return true if the report has been claimed
	 */
	public boolean moveMarker(int faithPoints) {
		boolean reportClaimed = false;
		while(faithPoints > 0) {

			if(currentPosition + 1 < track.size())
				currentPosition = currentPosition + 1;
			
			if (track.get(currentPosition).isPapalSpace())
				reportClaimed = true;
			
			faithPoints--;
		}
		return reportClaimed;
	}
	
	
	/**
	 * When someone triggers one report, checks if this player is in the right position update the vaticanReports array
	 */
	public void checkVaticanReport(){
		if (track.get(currentPosition).isInsideVatican(lastReportClaimed) && lastReportClaimed < vaticanReports.size()) {
			vaticanReports.set(lastReportClaimed, true);
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
		totalPoints = totalPoints + track.get(currentPosition).tilePoints();
		
		return totalPoints;
	}

	/**
	 * Checks if the player is in the last tile of the track
	 * @return true if the player have finished the track
	 */
	public boolean isTrackFinished(){
		return lastReportClaimed == reportPoints.size();
	}
	
	//Methods that just return info. I leave them here because maybe they'll be useful in the future
	
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