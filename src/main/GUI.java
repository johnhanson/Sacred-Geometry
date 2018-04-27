package main;

import java.awt.event.*;
import javax.swing.*;

import util.Dice;

public class GUI{
	private JTextArea output;
	private JTextField userRolls;
	private JTextField numDice;
	private JButton genRandomRoll;
	private JButton useUserRoll;
	private boolean calculatingMind = false;
	private JFrame frame;
	
	private JLabel diceNum;
	private JLabel diceRoll;
	
	DiceRollComputer DRC;
	
	
	
	public GUI() {
		DRC = new DiceRollComputer();
		DRC.numThreads = Runtime.getRuntime().availableProcessors();
		
		frame = new JFrame("Sacred Geometry");
		frame.setSize(1500, 800);
		
		diceNum = new JLabel("Dice #");
		diceNum.setBounds(50, 50, 100, 20);
		frame.add(diceNum);
		
		numDice = new JTextField("5", 1);
		numDice.setBounds(170, 50, 30, 20);
		frame.add(numDice);
		
		
		
		
		
		genRandomRoll = new JButton("Generate Random Roll");
		genRandomRoll.setBounds(80, 150, 100, 40);
		frame.add(genRandomRoll);
		genRandomRoll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					if (!calculatingMind) {
						int[] diceRolls = Dice.rollNDie6(Integer.valueOf(numDice.getText()));
						String outputText = "";
						for (int i : diceRolls) {
							outputText += i + " ";
						}
						userRolls.setText(outputText);
					}
					else {
						output.setText("Calculating Mind is not implemented");
					}
				}
				catch (Exception e) {
					output.setText(e.getMessage());
				}
			}
		});
		
		diceRoll = new JLabel("Dice roll");
		diceRoll.setBounds(200, 150, 75, 20);
		frame.add(diceRoll);
		userRolls = new JTextField("1 2 3 4 4 4 4", 20);
		userRolls.setBounds(300, 150, 100, 20);
		frame.add(userRolls);
		
		
		useUserRoll = new JButton("Calculate Geometry");
		useUserRoll.setBounds(410, 150, 150, 40);
		frame.add(useUserRoll);
		useUserRoll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					String[] tempRolls = userRolls.getText().split(" ");
					int[] actualRolls = new int[tempRolls.length];
					for (int i = 0; i < tempRolls.length; i++) {
						actualRolls[i] = Integer.valueOf(tempRolls[i]);
					}
					output.setText(DRC.simulate(actualRolls));
				}
				catch (Exception e) {
					output.setText(e.getMessage());
				}
			}
		});
		output = new JTextArea(300, 50);
		output.setBounds(500, 300, 300, 300);
		output.setText("");
		frame.add(output);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
