package main;

import java.awt.event.*;
import javax.swing.*;

import util.Dice;

public class GUI{
	private JTextArea output;
	private JTextField userRolls;
	private JTextField numDice;
	private JTextField numUserThreads;
	private JButton genRandomRoll;
	private JButton useUserRoll;
	private boolean calculatingMind = false;
	private JFrame frame;
	
	private JLabel diceNum;
	private JLabel diceRoll;
	private JLabel numThreads;
	private JLabel specifyLevel;
	
	private JRadioButton level1 = new JRadioButton("1"); 
	private JRadioButton level2 = new JRadioButton("2"); 
	private JRadioButton level3 = new JRadioButton("3"); 
	private JRadioButton level4 = new JRadioButton("4"); 
	private JRadioButton level5 = new JRadioButton("5"); 
	private JRadioButton level6 = new JRadioButton("6"); 
	private JRadioButton level7 = new JRadioButton("7"); 
	private JRadioButton level8 = new JRadioButton("8"); 
	private JRadioButton level9 = new JRadioButton("9"); 
	private JRadioButton allLevel = new JRadioButton("all (not implemented)");
	private JRadioButton first10 = new JRadioButton("first 10 of each level");
	
	
	DiceRollComputer DRC;
	
	
	
	public GUI() {
		DRC = new DiceRollComputer();
		DRC.numThreads = Runtime.getRuntime().availableProcessors();
		
		
		output = new JTextArea(300, 50);
		output.setBounds(10, 400, 500, 300);
		output.setText("");
		
		frame = new JFrame("Sacred Geometry");
		frame.setSize(600, 800);
		frame.setLayout(null);
		
		diceNum = new JLabel("Dice #");
		diceNum.setBounds(10, 50, 50, 20);
		frame.add(diceNum);
		
		numDice = new JTextField("5", 1);
		numDice.setBounds(60, 50, 30, 20);
		frame.add(numDice);
		
		genRandomRoll = new JButton("Roll Dice");
		genRandomRoll.setBounds(10, 70, 85, 30);
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
		diceRoll.setBounds(10, 10, 70, 20);
		frame.add(diceRoll);
		userRolls = new JTextField("1 2 3 4 4", 20);
		userRolls.setBounds(60, 10, 85, 20);
		frame.add(userRolls);
		
		int spacing = 20;
		int currentyLocation = 150;
		ButtonGroup levelList = new ButtonGroup();
		level1.setBounds(10, currentyLocation, 50, 20);
		level1.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 1 equation!");
					DRC.radioButtonOption = "1";
				}
			}
		});
		levelList.add(level1);
		frame.add(level1);
		currentyLocation += spacing;
		
		level2.setBounds(10, currentyLocation, 50, 20);
		level2.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 2 equation!");
					DRC.radioButtonOption = "2";
				}
			}
		});
		levelList.add(level2);
		frame.add(level2);
		currentyLocation += spacing;
		
		level3.setBounds(10, currentyLocation, 50, 20);
		level3.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 3 equation!");
					DRC.radioButtonOption = "3";
				}
			}
		});
		levelList.add(level3);
		frame.add(level3);
		currentyLocation += spacing;
		
		level4.setBounds(10, currentyLocation, 50, 20);
		level4.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 4 equation!");
					DRC.radioButtonOption = "4";
				}
			}
		});
		levelList.add(level4);
		frame.add(level4);
		currentyLocation += spacing;
		
		level5.setBounds(10, currentyLocation, 50, 20);
		level5.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 5 equation!");
					DRC.radioButtonOption = "5";
				}
			}
		});
		levelList.add(level5);
		frame.add(level5);
		currentyLocation += spacing;
		
		level6.setBounds(10, currentyLocation, 50, 20);
		level6.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 6 equation!");
					DRC.radioButtonOption = "6";
				}
			}
		});
		levelList.add(level6);
		frame.add(level6);
		currentyLocation += spacing;
		
		level7.setBounds(10, currentyLocation, 50, 20);
		level7.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 7 equation!");
					DRC.radioButtonOption = "7";
				}
			}
		});
		levelList.add(level7);
		frame.add(level7);
		currentyLocation += spacing;
		
		level8.setBounds(10, currentyLocation, 50, 20);
		level8.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 8 equation!");
					DRC.radioButtonOption = "8";
				}
			}
		});
		levelList.add(level8);
		frame.add(level8);
		currentyLocation += spacing;
		
		level9.setBounds(10, currentyLocation, 50, 20);
		level9.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first level 9 equation!");
					DRC.radioButtonOption = "9";
				}
			}
		});
		levelList.add(level9);
		frame.add(level9);
		currentyLocation += spacing;
		
		allLevel.setBounds(10, currentyLocation, 200, 20);
		allLevel.setEnabled(false);
		levelList.add(allLevel);
		frame.add(allLevel);
		currentyLocation += spacing;
		
		first10.setBounds(10, currentyLocation, 150, 20);
		first10.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					output.setText("Going to search for the first 10 equations for each level!");
					DRC.radioButtonOption = "first10";
				}
			}
		});
		levelList.add(first10);
		frame.add(first10);
		first10.setSelected(true);
		
		
		
		
		
		
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
		frame.add(output);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
