package it.polimi.ingsw.controller;

public enum GameState {
	
	CONFIG, // When the game configuration file is about to be set
	INIT, // When the players are choosing the starting resources and the leader cards
	GAME, // Game phase
	ENDGAME // When the game is about to end

}
