package it.polimi.ingsw.model;

import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FaithTrackTest {
	
	private FaithTrack faithTrack;
	private FaithTrack faithTrack2;

	@BeforeEach
	void instantiateTrack(){
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();

		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tilesTrack = parser.readTiles();
		ArrayList<Integer> reportPoints = parser.readReportPoints();
		faithTrack = new FaithTrack(tilesTrack, reportPoints, false);
		faithTrack2 = new FaithTrack(tilesTrack, reportPoints, false);
		assertFalse(faithTrack.isLorenzosTrack());
		ArrayList<Boolean> reports = new ArrayList<>();
		reports.add(false);
		reports.add(false);
		reports.add(false);
		assertEquals(reports, faithTrack.getVaticanReports());
	}

	@Test
	void moveMarker() {
		faithTrack.moveMarker(3);
		assertEquals(faithTrack.getCurrentPosition(), 3);
		faithTrack.moveMarker(1);
		assertNotEquals(faithTrack.getCurrentPosition(), 3);
	}

	@Test
	void checkActivationVaticanReport() {
		ArrayList<Tile> track = faithTrack.getTrack();
		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		assertTrue(faithTrack.checkActivationVaticanReport(0));
		assertFalse(faithTrack.checkActivationVaticanReport(1));
		faithTrack.moveMarker(1);
		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		assertTrue(faithTrack.checkActivationVaticanReport(1));
		assertFalse(faithTrack.checkActivationVaticanReport(2));
	}

	@Test
	void checkVaticanReport(){
		ArrayList<Tile> track1 = faithTrack.getTrack();
		ArrayList<Tile> track2 = faithTrack2.getTrack();
		while(!track2.get(faithTrack2.getCurrentPosition()).isInsideVatican(0)) {
			faithTrack2.moveMarker(1);
		}
		while(!track1.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		assertEquals(1, faithTrack.getLastReportClaimed());
		assertEquals(0, faithTrack2.getLastReportClaimed());
		faithTrack.checkVaticanReport(0);
		faithTrack2.checkVaticanReport(0);
		assertTrue(faithTrack.getVaticanReport(1));
		assertTrue(faithTrack2.getVaticanReport(1));
		assertEquals(1, faithTrack.getLastReportClaimed());
		assertEquals(1, faithTrack2.getLastReportClaimed());
		while(!track2.get(faithTrack2.getCurrentPosition()).isPapalSpace()) {
			faithTrack2.moveMarker(1);
		}
		faithTrack2.moveMarker(1);
		while(!track2.get(faithTrack2.getCurrentPosition()).isPapalSpace()) {
			faithTrack2.moveMarker(1);
		}
		faithTrack.checkVaticanReport(1);
		faithTrack2.checkVaticanReport(1);
		assertEquals(2, faithTrack.getLastReportClaimed());
		assertEquals(2, faithTrack2.getLastReportClaimed());
		assertFalse(faithTrack.getVaticanReport(2));
		assertTrue(faithTrack2.getVaticanReport(2));
	}

	@Test
	void countFaithTrackVictoryPoints() {
		ArrayList<Tile> track = faithTrack.getTrack();

		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		faithTrack.moveMarker(1);
		faithTrack.checkVaticanReport(0);
		assertEquals(faithTrack.getReportPoints().get(0) + track.get(faithTrack.getCurrentPosition()).getVictoryPoints(),
				faithTrack.countFaithTrackVictoryPoints());
	}

	@Test
	void isTrackFinished() {
		ArrayList<Tile> track = faithTrack.getTrack();

		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		faithTrack.moveMarker(1);
		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		faithTrack.moveMarker(1);
		while(!track.get(faithTrack.getCurrentPosition()).isPapalSpace()) {
			faithTrack.moveMarker(1);
		}
		faithTrack.moveMarker(1);
		assertTrue(faithTrack.isTrackFinished());
	}
}