package it.polimi.ingsw.network.messages;

public enum MessageType {
	
	// Creation of the lobby and pre-game messages

	//Server to Client messages
	LOBBY_LIST, 			// the server tells the client the available lobbies
	LOGIN_CONFIRMATION, 	// the server communicates if the lobby joining has been successful or not
	PLAYER_NUMBER_REQUEST, 	// the server asks the creator of the lobby how many players to put in the lobby
	NICKNAME_REQUEST,		// the server asks the client to choose a nickname
	NICKNAME_CONFIRMATION, 	// the server communicates if the nickname is valid or already chosen
	GAME_CONFIG_REQUEST, 	// the server asks for the game configuration file path to the game host


	//Client to Server messages
	LOBBY_ACCESS, 			// the client chooses the lobby to join, or creates a new one
	PLAYER_NUMBER_REPLY, 	// the game host communicates the number of players to play with
	NICKNAME_REPLY, 		// the client replies with the chosen nickname
	GAME_CONFIG_REPLY,		// the client replies with the game config file chosen (standard or custom)



	//Game logic messages

	//Server to Client messages
	LOBBY_SHOW,
	MATCH_INFO_SHOW,
	ACTION_REQUEST,
	CARDS_DECK_SHOW,
	CARDS_SHOW,
	DEPOT_SHOW,
	FAITH_TRACK_SHOW,
	PRODUCTION_SHOW,
	MARKET_SHOW,
	STRONGBOX_SHOW,
	LEADER_SHOW,
	PLAYER_CARDS_AND_PRODUCTION_SHOW,
	WIN_MESSAGE,
	RESOURCE_CHOICE,


	//Client to Server messages
	ACTION_REPLY,
	BUY_CARD,
	DEPOT_INTERACTION,
	LEADER_SELECTION,
	INTERACTION_WITH_MARKET,
	RESOURCES_LIST,
	LEADER_ACTION,
	PRODUCTION_SELECTION,


	//Generic messages
	ERROR_MESSAGE,
	GENERIC_MESSAGE,
	DISCONNECTION_MESSAGE,
	
}
