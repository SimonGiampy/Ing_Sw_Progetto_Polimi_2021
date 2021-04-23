package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.server.Client;
import it.polimi.ingsw.view.CLI;

import java.util.Scanner;

public class ClientDemo {
	
	public static void main(String[] args) {
		
		CLI cli = new CLI();
		ClientController clientcontroller = new ClientController(cli);
		cli.attach(clientcontroller);
		cli.initialize();
	
	}
	
	
}
