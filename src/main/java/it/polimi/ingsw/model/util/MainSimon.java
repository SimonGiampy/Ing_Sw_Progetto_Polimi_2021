package it.polimi.ingsw.model.util;


public class MainSimon {
	
	public static void main(String[] args) {
		/*
		System.out.print(Unicode.YELLOW_BACKGROUND + Unicode.GREEN_BOLD + "something there\n"); // purple bus emoji test
		System.out.print("\u001B[35m" + "something there\n"); // purple bus emoji test
		System.out.print("\u001B[35m" + "something there"); // purple bus emoji test
		try {
			Thread.sleep(1000);
		} catch (InterruptedException exception) {
			exception.printStackTrace();
		}

		 */
		
		
		
		System.out.print("\033[H\033[2J");
		System.out.flush();
		
		System.out.println("new line here for something random");
		
		
		
	}
}
