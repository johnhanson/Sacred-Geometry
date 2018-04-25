package main;

import java.awt.*;
import java.awt.event.*;

import util.Dice;

public class GUI extends Frame{
	private TextArea output;
	private TextField userRolls;
	private TextField numDice;
	private Button genRandomRoll;
	private Button useUserRoll;
	private boolean calculatingMind = false;
	DiceRollComputer DRC;
	
	
	
	public GUI() {
		DRC = new DiceRollComputer();
		setLayout(new FlowLayout());
		setTitle("Sacred Geometry");
		setSize(12*80+200, 800);
		add(new Label("Dice #"));
		numDice = new TextField("7", 2);
		add(numDice);
		
		
		
		
		genRandomRoll = new Button("Generate Random Roll");
		add(genRandomRoll);
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
		
		
		add(new Label("Dice roll"));
		userRolls = new TextField("1 2 3 4 5 6 7", 20); 
		add(userRolls);
		
		
		useUserRoll = new Button("Calculate Geometry");
		add(useUserRoll);
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
		output= new TextArea();
		add(output);
		setVisible(true);
	}
}
