package it.polimi.ingsw.model.util;

import java.util.ArrayList;

public class MainMichele {
	
	public static void main(String[] args) {
		ArrayList<Productions> prod = new ArrayList<>();
		prod.add(Productions.BASE_PRODUCTION);
		int num = prod.get(0).ordinal() + 1;
		System.out.println("num = " + num);
	}

}
