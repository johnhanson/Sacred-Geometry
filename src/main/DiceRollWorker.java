package main;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import util.MySet;
import util.Operator;
import util.PostfixPermutation;
import util.PostfixTemplate;

/**
* Worker thread to compute stuff. Originally same class, separated because I like small and concise classes.
* @author Ben
*
*/
public class DiceRollWorker implements Runnable {
	private Thread thread;
	Set<MySet> operandSet;
	Set<PostfixTemplate> templates;
	Operator[][] operatorSets;
	int startIndex, stopIndex, i;
	long endTime;
	Set<PostfixPermutation> permutationSet = new HashSet<>();
//	List<PostfixPermutation> permutationSet = new LinkedList<>();
	
	public int[] tiers = new int[] {
		3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107
	};
	
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}
	
	public int getRange() {
		return stopIndex-startIndex;
	}
	
	public int getIndex() {
		return i - startIndex;
	}
	
	public float getProgress() {
		return (float)getIndex()/(float)getRange();
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public Collection<PostfixPermutation> getPermutationSet() {
		return permutationSet;
	}
	
	
	
	public int rank (double value) {
		if ( Double.isInfinite(value)
		  || Double.isNaN(value)
		  || value < 0) return 0;
		
		for (int t = 0; t < tiers.length; t++) {
			if (tiers[t] == value) {
				return (t/3) + 1;
			}
		}
		return 0;
	}
	
	public void run() {
		System.out.println("thread starting!");
		for (i = startIndex; i < stopIndex; i++) {
			Operator[] operators = operatorSets[i];
//			System.out.printf("%5d/%5d\n",i,operatorSets.length);
			for (MySet operands : operandSet) {
				for (PostfixTemplate template : templates) {
					Object[] entities = template.apply(operands.getList(), Arrays.asList(operators));
					PostfixPermutation permutation = new PostfixPermutation(entities, i);
					try {
						permutation.calculate();
						int rank = rank(permutation.getResult());
						if (rank > 0) {
							permutation.setTier(rank);
							permutationSet.add(permutation);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		System.out.println("thread ending!");
		endTime = System.currentTimeMillis();
	}
}