package it.polimi.ingsw;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FaithTrackTest {


	@Test
	public void instanceOfTrack() {
		String fileName = "game_configuration_complete.xml";

		XMLParserDraft parser = new XMLParserDraft();

		FaithTrack faithTrack = parser.readTiles(fileName);
		ArrayList<Tile> track = faithTrack.getTrack();
		for (int i = 0; i < track.size(); i++) {
			System.out.println("tile: " + i);
			System.out.println("victory points: " + track.get(i).tilePoints());
			System.out.println("papal space: " + track.get(i).isPapalSpace());
			for (int j = 0; j < 3; j++) {
				System.out.println("inside vatican " + j + ": " + track.get(i).isInsideVatican(j));
			}

		}

	}

}