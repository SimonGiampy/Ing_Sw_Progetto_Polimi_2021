package it.polimi.ingsw.model.reducedClasses;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Tile;
import java.io.Serializable;
import java.util.ArrayList;

public class ReducedFaithTrack implements Serializable {

	private ArrayList<Boolean> vaticanReports;
	private Integer currentPosition;
	private ArrayList<Tile> track;
	private final ArrayList<Integer> reportPoints;
	
	private final boolean singlePlayer;

	public ReducedFaithTrack(FaithTrack track){
		this.vaticanReports = track.getVaticanReports();
		this.currentPosition = track.getCurrentPosition();
		this.track = track.getTrack();
		this.reportPoints = track.getReportPoints();
		this.singlePlayer = track.isSinglePlayer();
	}


	public ArrayList<Boolean> getVaticanReports() {
		return vaticanReports;
	}

	public Integer getCurrentPosition() {
		return currentPosition;
	}

	public ArrayList<Tile> getTrack() {
		return track;
	}

	public ArrayList<Integer> getReportPoints() {
		return reportPoints;
	}
	
	public boolean isSinglePlayer() {
		return singlePlayer;
	}
}
