package it.polimi.ingsw;

import it.polimi.ingsw.xml_parsers.XMLParserDraft;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

public class FaithTrackTest {
	
	/**
	 * tests the instantiation of the faith track with the parameters read from the xml configuration file
	 */
	@Test
	public void instanceOfTrack() {
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		//System.out.println(fullPath);
		
		XMLParserDraft parser = new XMLParserDraft();
		FaithTrack faithTrack = parser.readTiles(fullPath);
		
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