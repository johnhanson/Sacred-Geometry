package main;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
//			DiceRollComputer DiceRoll = new DiceRollComputer();
//			DiceRoll.takeUserInput();
			new GUI();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
