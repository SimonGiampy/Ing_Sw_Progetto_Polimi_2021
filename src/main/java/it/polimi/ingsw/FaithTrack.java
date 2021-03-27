package it.polimi.ingsw;

public class FaithTrack {
	
	private boolean[] vaticanReports;
	private int currentPosition;
	private final Tile[] track;
	private int lastReportClaimed;
	
	/**
	 * constructor that initializes the faith track with the initial values. Needs to be called from the Player
	 */
	public FaithTrack() {
		vaticanReports = new boolean[]{false, false, false};
		this.currentPosition = 0;
		this.lastReportClaimed = 0;
		track = new Tile[25];
		
		//Creates all the tiles with their relative parameters
		for(int i = 0; i < 25; i++){
			
			int victoryPoints = 0;
			boolean insideVatican1 = false, insideVatican2 = false, insideVatican3 = false, papalSpace = false;
			
			//victoryPoints values
			if(i >= 3 && i <=5)
				victoryPoints = 1;
			else if(i >= 6 && i <= 8)
				victoryPoints = 2;
			else if(i >= 9 && i <= 11)
				victoryPoints = 4;
			else if(i >= 12 && i <= 14)
				victoryPoints = 6;
			else if(i >= 15 && i <= 17)
				victoryPoints = 9;
			else if(i >= 18 && i <= 20)
				victoryPoints = 12;
			else if(i >= 21 && i <= 23)
				victoryPoints = 16;
			else if(i == 24)
				victoryPoints = 20;
			
			//insideVatican values, one for every Vatican zone
			if(i >= 5)
				insideVatican1 = true;
			if(i >= 12)
				insideVatican2 = true;
			if(i >= 19)
				insideVatican3 = true;
			
			//papalSpace values
			if(i == 8 || i == 16 || i==24)
				papalSpace = true;
			
			track[i] = new Tile(victoryPoints, insideVatican1, insideVatican2, insideVatican3, papalSpace);
		}
	}
	
	/**
	 * Move the market and control if the current player triggers a Papal Report
	 * @param faithPoints the number of faith points to be moved
	 * @return true if the report has been claimed
	 */
	public boolean moveMarker(int faithPoints) {
		boolean reportClaimed = false;
		while(faithPoints > 0) {
			
			currentPosition = currentPosition + 1;
			
			if (track[currentPosition].isPapalSpace())
				reportClaimed = true;
			
			faithPoints--;
		}
		return reportClaimed;
	}
	
	
	/**
	 * When someone triggers one report, checks if the this player is in the right position to gain the points
	 */
	public void checkVaticanReport(){
		
		if(lastReportClaimed == 0 && track[currentPosition].isInsideVatican1()) {
			vaticanReports[0] = true;
		}
		else if(lastReportClaimed == 1 && track[currentPosition].isInsideVatican2()){
			vaticanReports[1] = true;
		}
		else if(lastReportClaimed == 2 && track[currentPosition].isInsideVatican3()){
			vaticanReports[2] = true;
		}
		lastReportClaimed = lastReportClaimed + 1;
		
	}
	
	/**
	 * Count victory points when the game ends
	 * @return the number of victory points
	 */
	public int countFaithTrackVictoryPoints(){
		int totalPoints = 0;
		
		//Add points of the reports
		if(vaticanReports[0])
			totalPoints = totalPoints + 2;
		if(vaticanReports[1])
			totalPoints = totalPoints + 3;
		if(vaticanReports[2])
			totalPoints = totalPoints + 4;
		
		//Add points of the current position
		totalPoints = totalPoints + track[currentPosition].tilePoints();
		
		return totalPoints;
	}
	
	//Methods that just return info. I leave them here because maybe they'll be useful in the future
	
	public Tile[] getTrack(){
		return track;
	}
	
	public int getCurrentPosition(){
		return currentPosition;
	}
	
	public boolean getVaticanReports(int num){
		return vaticanReports[num - 1];
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