package it.polimi.ingsw.model.singleplayer;

import it.polimi.ingsw.controller.GameDemo;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.util.Colors;
import it.polimi.ingsw.model.util.TokenType;
import it.polimi.ingsw.xml_parsers.XMLParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class GameMechanicsSinglePlayerTest {
	private GameMechanicsSinglePlayer mechanicsSinglePlayer;



	@BeforeEach
	void setUp() {


		String fileName = "game_configuration_complete.xml";
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		String fullPath = file.getAbsolutePath();
		XMLParser parser = new XMLParser(fullPath);
		ArrayList<Tile> tiles = parser.readTiles();
		ArrayList<Integer> report = parser.readReportPoints();
		ArrayList<DevelopmentCard> devCards = parser.readDevCards();
		ArrayList<LeaderCard> allLeaderCards = parser.readLeaderCards();
		ProductionRules baseProduction = parser.parseBaseProductionFromXML();
		mechanicsSinglePlayer= new GameMechanicsSinglePlayer(1);
		//DevelopmentCardsDeck gameDevCardsDeck = new DevelopmentCardsDeck(mechanicsSinglePlayer.createCommonCardsDeck(devCards));
		mechanicsSinglePlayer.instantiateGame(devCards,allLeaderCards,baseProduction,tiles,report);
	}

	@Test
	void instantiateGame() {

	}

	@Test
	void revealTop() {
		ArrayList<Token> tokenList = new ArrayList<>();
		tokenList.add(new BlackCrossToken(2, mechanicsSinglePlayer.getLorenzoFaithTrack()));
		tokenList.add(new DiscardToken(2, Colors.BLUE, mechanicsSinglePlayer.getGameDevCardsDeck()));
		tokenList.add(new BlackCrossShuffleToken(1, mechanicsSinglePlayer.getLorenzoFaithTrack()));


		tokenList.add(new BlackCrossToken(2, mechanicsSinglePlayer.getLorenzoFaithTrack()));
		tokenList.add(new DiscardToken(2, Colors.GREEN, mechanicsSinglePlayer.getGameDevCardsDeck()));
		tokenList.add(new DiscardToken(2, Colors.PURPLE, mechanicsSinglePlayer.getGameDevCardsDeck()));
		tokenList.add(new DiscardToken(2, Colors.YELLOW, mechanicsSinglePlayer.getGameDevCardsDeck()));

		mechanicsSinglePlayer.setTokenList(tokenList); //set token for testing

		assertEquals(TokenType.BLACK_CROSS_TOKEN,mechanicsSinglePlayer.revealTop().getTokenType());
		assertEquals(TokenType.DISCARD_TOKEN,mechanicsSinglePlayer.revealTop().getTokenType());
		assertEquals(TokenType.BLACK_CROSS_SHUFFLE_TOKEN,mechanicsSinglePlayer.revealTop().getTokenType());
	}


	@Test
	void winner_LorenzoFaithTrackFinished() {
		mechanicsSinglePlayer.getLorenzoFaithTrack().moveMarker(24);
		assertEquals(-1,mechanicsSinglePlayer.winner()[1]);

	}

	@Test
	void winner_CardsCondition() {
		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);
		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);
		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);
		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);
		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);
		assertEquals(0,mechanicsSinglePlayer.winner()[1]);
		assertEquals(0,mechanicsSinglePlayer.winner()[0]);

		mechanicsSinglePlayer.getGameDevCardsDeck().discard2Cards(Colors.BLUE);

		assertEquals(-1,mechanicsSinglePlayer.winner()[1]);

	}

	@Test
	void winner_PlayerWin() {
		mechanicsSinglePlayer.getPlayer().getPlayerFaithTrack().moveMarker(24);
		assertNotEquals(0,mechanicsSinglePlayer.winner()[0]);
		assertEquals(0,mechanicsSinglePlayer.winner()[1]);

	}

}