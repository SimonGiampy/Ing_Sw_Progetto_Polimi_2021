package it.polimi.ingsw;

import java.util.ArrayList;

public class FaithTrack {
	
	private ArrayList<Boolean> vaticanReports;
	private Integer currentPosition;
	private ArrayList<Tile> track;
	private ArrayList<Integer> reportPoints;

	/**
	 * Constructor that initializes all the tiles
	 * @param insideVaticanValues array of arrays of booleans containing the values of insideVatican of each tile
	 * @param papalSpaceValues array of the papalSpace's values
	 * @param victoryPointsValues array of integer that indicates the victory points of every tile
	 * @param reportPoints points assigned by every report
	 */
	public FaithTrack(ArrayList<ArrayList<Boolean>> insideVaticanValues, ArrayList<Boolean> papalSpaceValues,
					  ArrayList<Integer> victoryPointsValues, ArrayList<Integer> reportPoints) {

		this.vaticanReports = new ArrayList<>();
		this.currentPosition = 0;
		this.track = new ArrayList<>();
		this.reportPoints = reportPoints;

		//Creates all the tiles with their relative parameters
		for(int i = 0; i < victoryPointsValues.size(); i++)
			track.add(new Tile(victoryPointsValues.get(i), insideVaticanValues.get(i), papalSpaceValues.get(i)));

	}

	
	/**
	 * Move the marker and control if the current player triggers a Papal Report
	 * @param faithPoints the number of faith points to be moved
	 * @return true if the report has been claimed
	 */
	public boolean moveMarker(int faithPoints) {
		boolean reportClaimed = false;
		while(faithPoints > 0) {
			
			currentPosition = currentPosition + 1;
			
			if (track.get(currentPosition).isPapalSpace())
				reportClaimed = true;
			
			faithPoints--;
		}
		return reportClaimed;
	}
	
	
	/**
	 * When someone triggers one report, checks if this player is in the right position update the vaticaReports array
	 */
	public void checkVaticanReport(){
		if(track.get(currentPosition).isInsideVatican(vaticanReports.size()))
			vaticanReports.add(true);
		else
			vaticanReports.add(false);
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