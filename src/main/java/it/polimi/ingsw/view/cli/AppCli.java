package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.controller.OfflineClientSideController;
import it.polimi.ingsw.controller.ServerSideController;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.gui.OfflineVirtualView;

import java.util.HashMap;

public class AppCli {

	
	public static void main(String[] args) {

		ServerSideController serverSideController;
		CLI cli = new CLI();
		String s = cli.start();
		if(s!=null) {
			serverSideController = new ServerSideController(1);
			OfflineVirtualView view= new OfflineVirtualView(cli);
			HashMap<String, VirtualView> map= new HashMap<>();
			map.put(s,view);
			serverSideController.setVirtualViews(map);
			cli.attach(new OfflineClientSideController(cli,serverSideController,s));
		}
		else{
			cli.initialize();
			cli.attach(new ClientSideController(cli));
		}

	}
	
	
}
