package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FaithTrackTest {

	@Test
	void moveMarker() {
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();

		XMLParser parser = new XMLParser();
		ArrayList<Tile> tilesTrack = parser.readTiles(fullPath);
		ArrayList<Integer> reportPoints = parser.readReportPoints(fullPath);
		FaithTrack faithTrack = new FaithTrack(tilesTrack, reportPoints);
		ArrayList<Tile> track = faithTrack.getTrack();

		faithTrack.moveMarker(3);
		assertTrue(faithTrack.getCurrentPosition() == 3);
		
		//TODO: change assert type in junit type

	}

	@Test
	void checkVaticanReport() {
		int lastReportClaimed = 0;
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();

		XMLParser parser = new XMLParser();
		ArrayList<Tile> tilesTrack = parser.readTiles(fullPath);
		ArrayList<Integer> reportPoints = parser.readReportPoints(fullPath);
		FaithTrack faithTrack = new FaithTrack(tilesTrack, reportPoints);
		ArrayList<Tile> track = faithTrack.getTrack();
		FaithTrack faithTrack2 = new FaithTrack(tilesTrack, reportPoints);
		ArrayList<Tile> track2 = faithTrack2.getTrack();

		int i = 0;
		while(!track.get(i).isPapalSpace())
			i++;
		//Move player 1 to the first report
		faithTrack.moveMarker(i);
		//Activate report, player 1 must have true, player 2 must have false
		if(faithTrack.checkVaticanReport(lastReportClaimed) || faithTrack2.checkVaticanReport(lastReportClaimed))
			lastReportClaimed++;
		assertTrue(faithTrack.getVaticanReport(lastReportClaimed));
		assertFalse(faithTrack2.getVaticanReport(lastReportClaimed));
		//Move player 2 to the tile of the first report but it was already activated so he must have false again
		
		//assert faithTrack2.moveMarker(i);
		
		if(faithTrack.checkVaticanReport(lastReportClaimed) || faithTrack2.checkVaticanReport(lastReportClaimed))
			lastReportClaimed++;
		assertTrue(faithTrack.getVaticanReport(lastReportClaimed));
		assertFalse(faithTrack2.getVaticanReport(lastReportClaimed));
	}

	@Test
	void countFaithTrackVictoryPoints() {
		int lastReportClaimed = 0;
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();

		XMLParser parser = new XMLParser();
		ArrayList<Tile> tilesTrack = parser.readTiles(fullPath);
		ArrayList<Integer> reportPoints = parser.readReportPoints(fullPath);
		FaithTrack faithTrack = new FaithTrack(tilesTrack, reportPoints);
		ArrayList<Tile> track = faithTrack.getTrack();

		int i = 0;
		while(!track.get(i).isPapalSpace())
			i++;

		//Move player 1 to the first report
		faithTrack.moveMarker(i);
		
		if(faithTrack.checkVaticanReport(lastReportClaimed))
			lastReportClaimed++;
		assertTrue(faithTrack.getVaticanReport(lastReportClaimed));

		assertTrue(faithTrack.countFaithTrackVictoryPoints() == 2+track.get(i).tilePoints());


	}

	@Test
	void isTrackFinished() {
		int lastReportClaimed = 0;
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();

		XMLParser parser = new XMLParser();
		ArrayList<Tile> tilesTrack = parser.readTiles(fullPath);
		ArrayList<Integer> reportPoints = parser.readReportPoints(fullPath);
		FaithTrack faithTrack = new FaithTrack(tilesTrack, reportPoints);

		while(faithTrack.getCurrentPosition() < faithTrack.getTrack().size()){
			faithTrack.moveMarker(1);
			System.out.println(faithTrack.getCurrentPosition());
			if(faithTrack.checkVaticanReport(lastReportClaimed))
				lastReportClaimed++;
		}
		assertTrue(faithTrack.isTrackFinished());
	}
}