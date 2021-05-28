package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.view.cli.CLI;

public class ClientDemo {
	
	public static void main(String[] args) {
		
		CLI cli = new CLI();
		ClientSideController clientController = new ClientSideController(cli);
		cli.attach(clientController);
		cli.initialize();
	
	}
	
	
}
