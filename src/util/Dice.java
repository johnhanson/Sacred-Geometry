package util;

import java.util.Random;

/**
* Just a bunch of dicey methods.
* @author Ben
*/
public class Dice {
	private static Random random = new Random();
	
	public static void setSeed(long seed) {
		random.setSeed(seed);
	}
	
	/**
	 * Roll n six-sided die
	 * @return
	 */
	public static int[] rollNDie6(int n) {
		return rollNDieM(n, 6);
	}
	
	/**
	 * Roll n m-sided die
	 * @return
	 */
	public static int[] rollNDieM(int n, int m) {
		int[] arr = new int[n];
		for (int i = 0; i < n; i++) {
			arr[i] = random.nextInt(m) + 1;
		}
		return arr;
	}
}