package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.SocketConnection;

public class ServerDemo {
	
	public static void main(String[] args) {
		
		Lobby lobby = new Lobby();
		SocketConnection socketConnection = new SocketConnection(lobby, 25000);
		
		new Thread(socketConnection).start();
	}
}
