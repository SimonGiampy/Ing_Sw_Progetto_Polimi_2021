package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.Server;

public class ServerDemo {
	
	public static void main(String[] args) {
		
		Lobby lobby = new Lobby();
		Server server = new Server(25000);
		
		new Thread(server).start();
	}
}
