package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.InvalidUserRequestException;
import it.polimi.ingsw.xml_parsers.XMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameTest {

	public static void main(String[] args) {
		GameTest test = new GameTest();
		test.gameTest();
	}
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
		ProductionRules baseProdu = parser.parseBaseProductionFromXML(fullPath);
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
			mech.getMarket().showMarket();
			System.out.println("where do you want to shift the marbles (row / col)?");
			which = scanner.nextLine();
			System.out.println("where do you want to shift the marbles (1-3) / (1-4)?");
			where = Integer.parseInt(scanner.nextLine()) - 1;
			
			try {
				Player player0 = mech.getPlayers()[0];
				player0.interactWithMarket(which, where);
				ArrayList<Boolean> check = new ArrayList<>();
				for (int i = 0; i < mech.getPlayers().length; i++ ) {
					check.add(mech.getPlayers()[i].getMyFaithTrack().checkVaticanReport(mech.lastReportClaimed));
				}
				if(check.contains(true)) {
					mech.lastReportClaimed++;
				}
				
			} catch (InvalidUserRequestException e) {
				e.printStackTrace();
			}
			for(Player p: mech.getPlayers()){
				System.out.println(p.getMyFaithTrack().getCurrentPosition()+" "+
						p.getMyFaithTrack().countFaithTrackVictoryPoints() +" "+p.getMyFaithTrack()
						.getVaticanReports().toString());
			}

			mech.getMarket().showMarket();
			System.out.println(mech.getPlayers()[0].getPlayersResourceDeck().getResourceList().toString());
		}
		
	}
	
}
