package it.polimi.ingsw.network.messages;

public enum MessageType {

	//Client messages
	LOGIN_REQUEST,
	LOBBY_ACCESS,
	GENERIC_MESSAGE,
	DISCONNECTION_MESSAGE,
	PLAYER_NUMBER_REPLY,

	BUY_CARD, CARDS_DECK_SHOW, CARDS_SHOW,
	DEPOT_INTERACTION, DEPOT_SHOW,
	FAITH_TRACK_SHOW,
	INPUT_SELECTION,
	INTERACTION_WITH_MARKET,
	MARKET_SHOW,
	STRONGBOX_SHOW,
	LEADER_SHOW,
	PLAYER_CARDS_AND_PRODUCTION_SHOW,
	RESOURCES_LIST,

	//Lobby messages
	LOBBY_LIST,
	LOGIN_REPLY,
	PLAYER_NUMBER_REQUEST,
	ERROR_MESSAGE,
	WIN_MESSAGE,
}
