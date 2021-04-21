package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.SocketServer;

public class ServerDemo {
	
	public static void main(String[] args) {
		
		Server server = new Server();
		SocketServer socketServer = new SocketServer(server, 25000);
		
		new Thread(socketServer).start();
	}
}
