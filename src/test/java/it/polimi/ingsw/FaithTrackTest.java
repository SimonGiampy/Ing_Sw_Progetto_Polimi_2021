package it.polimi.ingsw;

import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FaithTrackTest {

	XMLParserDraft parser = new XMLParserDraft();

	FaithTrack track = parser.readTiles("game_configuration_complete.xml");

	@Test
	public void moveMarker() {

	}

	@Test
	public void checkVaticanReport() {
	}

	@Test
	public void countFaithTrackVictoryPoints() {
	}
}