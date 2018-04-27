package util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
* A quick class for creating all permutations of an input set of things.
* Duplicates prevented by hashing
* Order preserved
* @author Ben
*
*/
public class MySet {
	/** The list of data represented by this data */
	List<Object> data;
	
	/**
	 * Private constructor to prevent misuse. Makes a local copy of the source data
	 * @param source - the source data.
	 */
	private MySet(List<Object> source) {
		data = new LinkedList<>(source);
	}
	
	/**
	 * Override of {@link java.lang.Object#equals(Object)} to allow hash matches to be considered equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof MySet) {
			return hashCode() == o.hashCode();
		}
		return false;
	}
	
	/**
	 * Override of {@link java.lang.Object#hashCode()} to make identical data return identical hash codes.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(data);
	}
	
	/**
	 * Override of {@link java.lang.Object#toString()} to make the output actually readable.
	 */
	@Override
	public String toString() {
		return Arrays.toString(data.toArray());
	}
	
	/**
	 * @return the list of data represented by this permutation
	 */
	public List<Object> getList() {
		return data;
	}
	
	/**
	 * @param objects - the input array
	 * @return - a set containing all unique permutations of the array
	 */
	public static Set<MySet> getUniquePermutations(Object[] objects) {
		return getUniquePermutations(Arrays.asList(objects));
	}
	
	/**
	 * @param objects - the input list
	 * @return - a set containing all unique permutations of the list
	 */
	public static Set<MySet> getUniquePermutations(List<Object> objects) {
		Set<MySet> set = new HashSet<>();
		@SuppressWarnings("unused")
		int collisions = permute(objects,0,set);
		// Used for testing, no longer needed.
//		System.out.println("Collided " + collisions + " times.");
		return set;
	}
	
	/**
	 * Recursive permutation method. Starting at k, it swaps each value and the value next to it.
	 * Then it recurses at k+1.
	 * Max stack depth is entities.size() <br/>
	 *
	 * No Duplicates. <br/>
	 * Order Preserved.
	 *
	 * @param entities - the list of entities to permute
	 * @param k - the start index
	 * @param permutations - the current set of permutations
	 * @return - the number of collisions that occurred (for testing)
	 */
	private static int permute(List<Object> entities, int k, Set<MySet> permutations) {
		int collisions = 0;
		for(int i = k; i < entities.size(); i++){
			Collections.swap(entities, i, k);
			collisions += permute(entities, k+1, permutations);
			Collections.swap(entities, k, i);
		}
		
		if (k == entities.size() -1 || entities.size() == 0) {
			MySet set = new MySet(entities);
			if (permutations.contains(set)) collisions++;
			else permutations.add(set);
		}
		return collisions;
	}
}