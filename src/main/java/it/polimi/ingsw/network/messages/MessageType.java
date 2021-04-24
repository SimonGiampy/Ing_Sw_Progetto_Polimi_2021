package it.polimi.ingsw.network.messages;

public enum MessageType {
	
	// Creation of the lobby and pre-game messages
	LOBBY_LIST, // the server tells the client the available lobbies
	LOBBY_ACCESS, // the client chooses the lobby to join, or creates a new one
	LOGIN_CONFIRMATION, // the server communicates if the lobby joining has been successful or not
	PLAYER_NUMBER_REQUEST, // the server asks the creator of the lobby how many players to put in the lobby
	PLAYER_NUMBER_REPLY, // the game host communicates the number of players to play with
	NICKNAME_REQUEST, // the server asks the client to choose a nickname
	NICKNAME_REPLY, // the client replies with the chosen nickname
	NICKNAME_CONFIRMATION, // the server communicates if the nickname is valid or already chosen
	GAME_CONFIG_REQUEST, // the server asks for the game configuration file path to the game host
	GAME_CONFIG_REPLY, // the client replies with the game config file chosen (standard or custom)
	
	
	//Game logic messages
	ACTION_REQUEST,
	BUY_CARD, CARDS_DECK_SHOW, CARDS_SHOW,
	DEPOT_INTERACTION, DEPOT_SHOW,
	FAITH_TRACK_SHOW,
	PRODUCTION_SHOW,
	LEADER_SELECTION,
	INTERACTION_WITH_MARKET,
	MARKET_SHOW,
	STRONGBOX_SHOW,
	LEADER_SHOW,
	PLAYER_CARDS_AND_PRODUCTION_SHOW,
	RESOURCES_LIST,
	WIN_MESSAGE,
	LOBBY_SHOW,
	MATCH_INFO_SHOW,
	LEADER_ACTION,
	ACTION_REPLY,
	PRODUCTION_SELECTION,
	RESOURCE_CHOICE,
	
	
	//Generic messages
	ERROR_MESSAGE,
	GENERIC_MESSAGE,
	DISCONNECTION_MESSAGE,
	
}
