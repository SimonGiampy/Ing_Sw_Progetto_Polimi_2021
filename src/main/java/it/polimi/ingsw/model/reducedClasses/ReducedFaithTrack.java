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

	public ReducedFaithTrack(FaithTrack track){
		vaticanReports=track.getVaticanReports();
		currentPosition=track.getCurrentPosition();
		this.track=track.getTrack();
		reportPoints=track.getReportPoints();
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
}
