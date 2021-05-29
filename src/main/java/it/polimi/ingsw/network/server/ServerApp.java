package it.polimi.ingsw.network.server;

public class ServerApp {
	
	public static void main(String[] args) {

		Server server = new Server(25000);
		new Thread(server).start();
	}
}
