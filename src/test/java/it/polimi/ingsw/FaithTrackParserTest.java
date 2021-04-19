package it.polimi.ingsw;

import it.polimi.ingsw.model.FaithTrack;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

public class FaithTrackParserTest {
	
	/**
	 * tests the instantiation of the faith track with the parameters read from the xml configuration file
	 */
	@Test
	public void faithTrackInstanceFromXMLData() {
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		//System.out.println(fullPath);
		
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tilesTrack = parser.readTiles();
		ArrayList<Integer> reportPoints = parser.readReportPoints();
		FaithTrack faithTrack = new FaithTrack(tilesTrack, reportPoints);
		FaithTrack faithTrack2 = new FaithTrack(tilesTrack, reportPoints);
		ArrayList<Tile> track = faithTrack.getTrack();
		ArrayList<Boolean> vaticanReports = faithTrack.getVaticanReports();

		faithTrack2.moveMarker(8);
		faithTrack.moveMarker(3);
		faithTrack.checkVaticanReport(0);
		faithTrack2.checkVaticanReport(0);
		faithTrack2.moveMarker(4);
		faithTrack.moveMarker(11);
		faithTrack.checkVaticanReport(1);
		faithTrack2.checkVaticanReport(1);
		faithTrack.showFaithTrack(0);
		System.out.print("\n");
		faithTrack2.showFaithTrack(1);
		faithTrack.helpMe();
		/*
		for (int i = 0; i < track.size(); i++) {
			System.out.println("tile: " + i);
			System.out.println("victory points: " + track.get(i).tilePoints());
			System.out.println("papal space: " + track.get(i).isPapalSpace());
			for (int j = 0; j < vaticanReports.size(); j++) {
				System.out.println("inside vatican " + j + ": " + track.get(i).isInsideVatican(j));
			}
		}
		System.out.println("reading report points:");
		ArrayList<Integer> rep = faithTrack.getReportPoints();
		for (int i = 0; i < rep.size(); i++) {
			System.out.println("report " + i + ": points = " + rep.get(i));
		}

		 */



	}

}