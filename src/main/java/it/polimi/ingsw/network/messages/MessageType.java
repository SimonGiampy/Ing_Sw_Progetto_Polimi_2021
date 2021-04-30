package it.polimi.ingsw.network.messages;

public enum MessageType {
	
	// Creation of the lobby and pre-game messages

	//Server to Client messages
	LOBBY_LIST, 						// the server tells the client the available lobbies
	LOGIN_CONFIRMATION, 				// the server communicates if the lobby joining has been successful or not
	PLAYER_NUMBER_REQUEST, 				// the server asks the creator of the lobby how many players to put in the lobby
	NICKNAME_REQUEST,					// the server asks the client to choose a nickname
	NICKNAME_CONFIRMATION, 				// the server communicates if the nickname is valid or already chosen
	GAME_CONFIG_REQUEST, 				// the server asks for the game configuration file path to the game host


	//Client to Server messages
	LOBBY_ACCESS, 						// the client chooses the lobby to join, or creates a new one
	PLAYER_NUMBER_REPLY, 				// the game host communicates the number of players to play with
	NICKNAME_REPLY, 					// the client replies with the chosen nickname
	GAME_CONFIG_REPLY,					// the client replies with the game config file chosen (standard or custom)



	//Game logic messages

	//Server to Client messages
	MATCH_INFO,					// the server sends match's info (players nicknames)
	ACTION_REQUEST,						// the server asks the player to choose the action in the turn
	RESOURCE_CHOICE,					// the server sends the number of free choice resources
	CARDS_DECK_SHOW,					// the server sends cards deck
	CARDS_SHOW,							// the server sends buyable cards to the player
	DEPOT_CONFIRMATION,					// the server sends the current depot to the player, alongside with various reply parameters
	DEPOT_SHOW,					        // the server asks the view to show the current depot
	FAITH_TRACK_SHOW,					// the server sends the faith track to the player
	PRODUCTION_SHOW,					// the server sends available production to the player
	MARKET_SHOW,						// the server sends the market to the player
	STRONGBOX_SHOW,						// the server sends the strongbox to the player
	LEADER_SHOW,						// the server sends leader cards to the player
	PLAYER_CARDS_AND_PRODUCTION_SHOW,	// the server sends cards and productions to the player
	WHITE_MARBLE_CHOICE,				// the server asks into which resource it has to transform the white marbles
	WIN_MESSAGE,						// the server sends the winner of the game


	//Client to Server messages
	ACTION_REPLY,						// the client sends chosen action index to the server
	BUY_CARD,							// the client sends chosen color and chosen level
	DEPOT_INTERACTION,					// the client sends depot interaction info ( where, from, destination)
	LEADER_SELECTION,					// the client sends chosen leaders
	INTERACTION_WITH_MARKET,			// the client sends market interaction info (which,where)
	RESOURCES_LIST,						// the client sends selected resources
	LEADER_ACTION,						// the client sends chosen leader and selected action
	PRODUCTION_SELECTION,				// the client sends selected production
	WHITE_MARBLE_REPLY,					// the client sends which leader to use

	//Generic messages
	ERROR_MESSAGE,						// the server sends an error message
	GENERIC_MESSAGE,					// the server sends a generic message
	DISCONNECTION_MESSAGE,				// the server sends info about a disconnection
	
}
