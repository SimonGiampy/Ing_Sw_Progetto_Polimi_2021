package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.ClientSideController;
import it.polimi.ingsw.controller.OfflineController;
import it.polimi.ingsw.controller.ServerSideController;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.OfflineVirtualView;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AppCli {

	
	public static void main(String[] args) {
		CLI cli = new CLI();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type <online> or <offline> (Offline mode is available only for the single player game mode). ");
		String mode;
		boolean check;
		do {
			mode = scanner.nextLine().toLowerCase();
			check = Pattern.matches("((offline)|(online))", mode);
			if (!check) {
				System.out.println("Invalid input.");
			}
		} while (!check);
		
		if (mode.equals("offline")) {
			System.out.println("Choose a nickname: ");
			String nickname = scanner.nextLine();
			
			ServerSideController serverSideController = new ServerSideController(1);
			OfflineVirtualView view = new OfflineVirtualView(cli);
			HashMap<String, VirtualView> map = new HashMap<>();
			map.put(nickname, view);
			
			OfflineController controller = new OfflineController(cli, serverSideController, nickname);
			cli.attach(controller);
			
			serverSideController.setVirtualViews(map);
		} else if (mode.equals("online")) {
			cli.initialize();
			cli.attach(new ClientSideController(cli));
		}

	}
	
	
}
