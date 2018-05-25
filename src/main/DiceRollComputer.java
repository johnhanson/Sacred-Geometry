package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import util.Dice;
import util.MySet;
import util.Operator;
import util.PostfixPermutation;
import util.PostfixTemplate;

/**
* Main class. Takes user input and does stuff.
* @author Ben
*
*/
public class DiceRollComputer {


	public int numThreads = 4;
	private float tickRate = 2;
	private Object[] lastRoll;
	
	//instead of an enum to hold the goal level configuration
	public String radioButtonOption = "first10";

	/** number of samples used to calculate rate */
	private int numRateSamples = 10;
	/** Rate weights - for smoothing multiple rate samples and preventing microsoft-esque eta calculation */
	private float[] rateWeights;

	
	public int[] tiers = new int[] {
		3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107
	};
	
	private void updateRateWeights() {
		rateWeights = new float[numRateSamples-1];
		for (int i = 0; i < rateWeights.length; i++) {
			rateWeights[i] = (float)Math.pow(2, -1*(i+1));
		}
	}


	
	public DiceRollComputer() {
		
	}

	public void takeUserInput() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		System.out.println("Stupid Dice Mather a Go-Go.");
		System.out.print(" > ");
		boolean command = false;
		numThreads = Runtime.getRuntime().availableProcessors();
		while ((line = reader.readLine())!=null) {
			if (line.length() == 0) break;
			String[] tokens = line.split("[\\s,]+");
			try {
				if (tokens.length == 2) {
					if (tokens[0].equalsIgnoreCase("r")) {
						if (tokens[1].equalsIgnoreCase("r")) {
							simulate(lastRoll);
						} else {
							useRandomRoll(Integer.parseInt(tokens[1]));
						}
						command = true;
					} else if (tokens[0].equalsIgnoreCase("rate")) {
						tickRate = Float.parseFloat(tokens[1]);
						System.out.println("set rate to " + tickRate);
						command = true;
					} else if (tokens[0].equalsIgnoreCase("samples")) {
						numRateSamples = Integer.parseInt(tokens[1]);
						updateRateWeights();
						System.out.println("set samples to " + numRateSamples);
						command = true;
					} else if (tokens[0].equalsIgnoreCase("threads")) {
						System.out.println("trying to set threads");
						numThreads = Integer.parseInt(tokens[1]);
						System.out.println("Set threads to " + numThreads);
						command = true;
					}
				}
			   
				if (!command) {
					int[] nums = new int[tokens.length];
					for (int i = 0; i < nums.length; i++) {
						nums[i] = Integer.parseInt(tokens[i]);
					}
					simulate(nums);
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Something was not a number.");
			}
			System.out.print(" > ");
			command = false;
		}
		
	}

	private void useRandomRoll(int n) {
		int[] diceRoll = Dice.rollNDie6(n);
		System.out.println("Rolling " + n + " die.");
		simulate(diceRoll);
	}

	String simulate(int[] diceRoll) {
		Object[] array = new Object[diceRoll.length];
		for (int i = 0; i < diceRoll.length; i++)
			array[i] = diceRoll[i];
		return simulate(array);
	}

	private String simulate(Object[] diceRoll) {
		updateRateWeights();
		long startTime = System.currentTimeMillis();
		lastRoll = diceRoll;
		
		System.out.println("ROLL: " + Arrays.toString(diceRoll));
		Operator[][]         operatorSets = Operator.getAllOperatorCombos(diceRoll.length-1);
		System.out.printf("Calculated %5d Unique Operator Sequences.\n", operatorSets.length);
		Set<MySet>           operandSets  = MySet.getUniquePermutations(diceRoll);
		System.out.printf("Calculated %5d Unique Operand Permutations.\n", operandSets.size());
		Set<PostfixTemplate> templates    = PostfixTemplate.generateValidTemplates(diceRoll.length);
		System.out.printf("Calculated %5d Unique Postfix Templates.\n", templates.size());
		
		/*
		 * Where d is the number of die.
		 *
		 * OperatorSets represents all possible orders of operators. Constant case = 4^(d-1)
		 * OperandSets represents all unique permutations of the operands. Worst case = 6! as any more die than 6 will guarantee many duplicate permutations that won't need to be calculated.
		 * Templates represents all valid postfix templates of length 2d-1. I'm not sure how to estimate this. It's exponential, though.
		 *
		 * Multiplying these three numbers will tell how many equations will be created and solved.
		 */
		
		
		long bigTotal = templates.size();
		bigTotal *= operandSets.size();
		bigTotal *= operatorSets.length;
		
		System.out.printf("Total possible postfix expressions: %d.\n", bigTotal);

		int operatorSetsPerThread = operatorSets.length / numThreads;
		// The work is divided between some threads.
		
		int goalLevel = 0;
		if(!radioButtonOption.equals("first10")) {
			System.out.println("going to search for one equation of level " + radioButtonOption);
			goalLevel = Integer.valueOf(radioButtonOption);
		}
		
		System.out.println("Forking " + numThreads + " worker threads.");
		DiceRollWorker[] workers = new DiceRollWorker[numThreads];
		for (int i = 0; i < numThreads; i++) {
			DiceRollWorker cdr = new DiceRollWorker();
			cdr.operandSet = operandSets;
			cdr.templates = templates;
			cdr.operatorSets = operatorSets;
			cdr.startIndex = i*operatorSetsPerThread;
			cdr.stopIndex = (i+1)*operatorSetsPerThread;
			cdr.goalLevel = goalLevel;
			cdr.threadNum = i;
			workers[i] = cdr;
			System.out.printf("  Thread #%d using range (%4d..%4d)\n", i, cdr.startIndex, cdr.stopIndex);
		}
		DiceRollWorker.foundEquation = false;
		workers[numThreads-1].stopIndex=operatorSets.length;

		for (DiceRollWorker cdr : workers) {
			cdr.start();
		}
		
		System.out.println("Waiting for thread completion...");

		/*
		 * This is all unnecessary to the calculation of the dice problem. I just wanted
		 * some feedback as to how fast it was going. Every tick, the progress is
		 * checked on each thread and the rate for the last tick is calculated. Then the
		 * last few tick rates are averaged together using rough weighting to smooth it
		 * out. If the time estimate is less than the tick length, then the loop breaks
		 * and just waits for thread completion.
		 *
		 * This means that the calculations must always take a minimum of one tick.
		 * However, it will never add overhead to a calculation which takes more than
		 * one tick.
		 */

		float[] rateSamples = new float[numRateSamples];
		float lastProgress = 0;
		float neta = 0;
		int numSamples = 0;
		long tickRateMillis = (long) (tickRate * 1000);
		do {
			try {
				Thread.sleep(tickRateMillis);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			float progress = getAverageProgress(workers);
			if (progress >= 100) {
				break;
			}

			shiftRight(rateSamples);
			// Rate = Delta / Time
			rateSamples[0] = (progress - lastProgress) / tickRate;
			numSamples++;
			float elapsedTime = tickRate * numSamples;
			float smoothRate = getSmoothRate(rateSamples, numSamples);
			// Time = Delta / Rate
			neta = (100 - progress) / smoothRate;
			float total = elapsedTime + neta;
			System.out.printf("  %05.2f%%; Elapsed: %5.2f seconds; NETA: %5.2f seconds; Total: %5.2f\n", progress,
					elapsedTime, neta, total);

			// The next tick will waste time.
			if (numSamples > 0 && neta < tickRate) {
				System.out.println("next tick will waste time!");
				break;
			}
			lastProgress = progress;
		} while (true);
		System.out.println("done!");
		
		/*
		 * This loop is just a standard thread joiner.
		 * It also tracks maximum thread end time to use as the total computation time for the problem
		 * and adds all the prime postfix permutations to the larger set.
		 */
		
//		Set<PostfixPermutation> permutationSet = new HashSet<>();
		List<PostfixPermutation> permutationSet = new LinkedList<>();
		long maxEndTime = startTime;
		for (DiceRollWorker cdr : workers) {
			try {
				cdr.join();
				if (cdr.getEndTime() > maxEndTime)
					maxEndTime = cdr.getEndTime();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			permutationSet.addAll(cdr.getPermutationSet());
		}
	   
		long timeSpentCalculating = maxEndTime - startTime;
		System.out.printf("Calculated %5d Prime Solutions in %d millis (%d seconds).\n", permutationSet.size(), timeSpentCalculating, timeSpentCalculating/1000);

		List<PostfixPermutation> permutations = new LinkedList<>(permutationSet);
		permutations.sort(comp);
		printPermutations(permutations);
		try {
			writeToFile(permutations);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return getPermutations(permutations);
	}
	
	private void shiftRight(float[] array) {
		for (int i = array.length-1; i > 0; i--)
			array[i] = array[i-1];
	}
	
	private float getAverageProgress(DiceRollWorker[] workers) {
		float progress = 0;
		for (DiceRollWorker cdr : workers)
			progress += cdr.getProgress();
		
		// an attempt to avoid floating point errors hanging everything
		if (progress == workers.length) return 100;
		
		progress*=(100f/workers.length);
		return progress;
	}
	
	private float getSmoothRate(float[] rateSamples, int numSamples) {
//		System.out.println("    " + Arrays.toString(rateSamples));
//		if (numSamples == 1) return rateSamples[0];
		float smoothRate = 0;
 
		int useSamples = Math.min(numSamples-1, rateSamples.length-1);
		float multiplier = 1;
		for (int i = 0; i < useSamples; i++) {
			float sample = rateSamples[i];
			multiplier = rateWeights[i];
			smoothRate += sample * multiplier;
		}
		float sample = rateSamples[useSamples];
//		System.out.printf("    %05.2f x %03.3f\n", sample, multiplier);
		smoothRate += sample * multiplier;
		
		return smoothRate;
	}
	
	private void printPermutations(Collection<PostfixPermutation> permutations) {
		List<PostfixPermutation> permutationsList = new LinkedList<>(permutations);
		Collections.sort(permutationsList, comp);
		int lastTier = -1;
		int numPrinted = 0;
//		if (permutationsList.size() == 1) {
//			System.out.println("There's only one in the list. Printing that.");
//			PostfixPermutation p = permutationsList.get(1);
//			System.out.println(p.toString());
//			System.out.printf("%3d. %s = %3d [Tier %d]\n", numPrinted+1, p.getExpression(), (int) p.getResult().doubleValue(), p.getTier());
//			return;
//		}
		for (PostfixPermutation p : permutationsList) {
			int currentTier = p.getTier();
			
			if (currentTier != lastTier) {
				System.out.println(" ====== TIER " + currentTier + " ====== ");
				numPrinted = 0;
			} else if (numPrinted == 10) {
				continue;
			}
			System.out.printf("%3d. %s = %3d [Tier %d]\n", numPrinted+1, p.getExpression(), (int) p.getResult().doubleValue(), currentTier);
			numPrinted++;
			lastTier = currentTier;
		}
	}
	
	private String getPermutations(Collection<PostfixPermutation> permutations) {
		System.out.println("getPermutations invoked");
		List<PostfixPermutation> permutationsList = new LinkedList<>(permutations);
		Collections.sort(permutationsList, comp);
		
		int lastTier = -1;
		int numPrinted = 0;
		StringBuilder sb = new StringBuilder();

//		if (permutationsList.size() == 1) {
//			System.out.println("There's only one in the list. Returning that.");
//			PostfixPermutation p = permutationsList.get(1);
//			System.out.println(p.toString());
//			String output = String.format("%3d. %s = %3d [Tier %d]\n", numPrinted+1, p.getExpression(), (int) p.getResult().doubleValue(), p.getTier());
//			System.out.println("output from getPermutations: "  + output);
//			return output;
//		}
//		System.out.println("permutationsList.size() is not 1");
		String temp = "";
		for (PostfixPermutation p : permutationsList) {
			int currentTier = p.getTier();
			
			if (currentTier != lastTier) {
				sb.append(" ====== TIER " + currentTier + " ====== \n");
				numPrinted = 0;
			} else if (numPrinted == 10) {
				continue;
			}
			temp = String.format("%3d. %s = %3d [Tier %d]\n", numPrinted+1, p.getExpression(), (int) p.getResult().doubleValue(), currentTier);
			sb.append(temp);
			numPrinted++;
			lastTier = currentTier;
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	private void writeToFile(Collection<PostfixPermutation> permutations) throws FileNotFoundException {
		File file = new File("allOutput.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintWriter pw = new PrintWriter(fos);
		for (PostfixPermutation pp : permutations) {
			String s = String.format("%d = %s", (int)pp.getResult().doubleValue(), pp.getExpression().replaceAll("\\s+",""));
			pw.println(s);
		}
		pw.flush();
		pw.close();
	}
	
	private Comparator<PostfixPermutation> comp = new Comparator<PostfixPermutation>() {
		@Override
		public int compare(PostfixPermutation o1, PostfixPermutation o2) {
			if (o2.getTier() == o1.getTier())
				return (int)(o2.getResult() - o1.getResult());
			return o2.getTier() - o1.getTier();
		}
	};

}