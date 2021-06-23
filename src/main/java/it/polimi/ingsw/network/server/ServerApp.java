package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.ClientSideController;

import java.util.Scanner;

public class ServerApp {
	
	public static void main(String[] args) {
		String port;
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Host local server on the port: ");
			port = scanner.nextLine();
		} while (!ClientSideController.isValidPort(port));
		System.out.println("Server is starting...");
		Server server = new Server(Integer.parseInt(port));
		new Thread(server).start();
	}
}
