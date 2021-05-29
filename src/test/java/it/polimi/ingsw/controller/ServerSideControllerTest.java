package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.messages.game.client2server.ResourcesList;
import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.Server;
import org.junit.jupiter.api.Test;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerSideControllerTest {

	@Test
	void onMessageReceived() {
		new ServerSideController(4);
	}
}