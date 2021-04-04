package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class GameTest {
	
	@Test
	public void gameTest() {
		
		GameMechanicsMultiPlayer mech = new GameMechanicsMultiPlayer(2);
		
		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		
		XMLParser parser = new XMLParser();
		ArrayList<Tile> tiles = parser.readTiles(fullPath);
		ArrayList<Integer> report = parser.readReportPoints(fullPath);
		ArrayList<DevelopmentCard> devcards = parser.readDevCards(fullPath);
		ArrayList<LeaderCard> leaderCards = parser.readLeaderCards(fullPath);
		ProductionRules baseProdu = parser.parseBaseProductionFromXML(fileName);
		mech.instantiateGame(devcards, leaderCards, baseProdu, tiles, report);
		
		for (Player p: mech.getPlayers()) {
			p.chooseTwoLeaders(1, 4); // chooses 1 and 4 leader
		}
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("what do you want to do?\nOptions: 1 (Market), 2 (Dev Card), 3 (Production)");
		int input = Integer.parseInt(scanner.nextLine());
		String which;
		int where;
		if (input == 1) {
			System.out.println("where do you want to shift the marbles (row / col)?");
			which = scanner.nextLine();
			System.out.println("where do you want to shift the marbles (1-3) / (1-4)?");
			where = Integer.parseInt(scanner.nextLine());
			
			try {
				Player player0 = mech.getPlayers()[0];
				player0.interactWithMarket(which, where);
				
				for (Player p: mech.getPlayers()) {
					boolean check = p.getMyFaithTrack().checkVaticanReport(mech.lastReportClaimed);
					if (check) {
						mech.lastReportClaimed ++;
					}
				}
				
			} catch (InvalidUserRequestException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
