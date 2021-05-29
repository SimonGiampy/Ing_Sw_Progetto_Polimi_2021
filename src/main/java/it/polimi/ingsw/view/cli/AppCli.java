package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientSideController;

public class AppCli {
	
	public static void main(String[] args) {
		
		CLI cli = new CLI();
		ClientSideController clientController = new ClientSideController(cli);
		cli.attach(clientController);
		cli.initialize();
	
	}
	
	
}
