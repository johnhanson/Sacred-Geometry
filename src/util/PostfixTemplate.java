package util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
* Thanks to John for this approach.
* My first approach would calculate all postfix expressions (both valid and invalid) for each permutation of operators and operands.
* John pointed out that there was a finite number of valid templates that could ever exist at all.
*
* This class encapsulates one valid postfix template, which determines the ordering of the operators and operands that are given to it.
* This really is the optimization that made the whole project fast enough to compute 7 die.
* @author Ben, John
*
*/
public class PostfixTemplate {
	
	Character[] template;
	public boolean equals(Object o) {
		if (o instanceof PostfixTemplate) {
			return hashCode() == o.hashCode();
		}
		return false;
	}
	
	public int hashCode() {
		// A Hash is a Hash is a Hash. Don't know how to suppress the warning.
		return Objects.hash(template);
	}
	
	public String toString() {
		return Arrays.toString(template);
	}
	
	/**
	 * Given a set of operators and operands, order them to the template
	 */
	public Object[] apply(List<Object> operands, List<Operator> operators) {
		Object[] entities = new Object[template.length];
		int operandIndex = 0;
		int operatorIndex = 0;
		for (int i = 0; i < template.length; i++) {
			if (template[i] == 'N')
				entities[i] = operands.get(operandIndex++);
			else
				entities[i] = operators.get(operatorIndex++);
		}
		return entities;
	}
	
	/**
	 * Generates a set of all valid templates that contain the specified number of operands.
	 */
	public static Set<PostfixTemplate> generateValidTemplates(int numOperands) {
		int numOperators = numOperands - 1;
		LinkedList<Character> expression = new LinkedList<>();
		Set<PostfixTemplate> templates = new LinkedHashSet<>();
		
		// Collision checks were used to make sure there were no duplicates or extra work being done.
		@SuppressWarnings("unused")
		int collisions = generateValidTemplates(numOperands, numOperators, expression, 0, templates);
		System.out.println("Collided " + collisions + " times");
		return templates;
	}
	
	
	/**
	 * Recursive function to generate only valid templates.
	 * @param numOperands  - The number of operands remaining. When this hits zero, only operators can be used.
	 * @param numOperators - The number of operators remaining. This should never hit zero before operands hit zero.
	 * @param expression   - The current expression that is being constructed. When finished, this will be used to create the template.
	 * @param numStack     - The depth of the number stack. Pulling from the operands increases this by 1. Pulling from the operators decreases this by 1. This number is not allowed to go below 1, ensuring only valid templates are generated.
	 * @param templates    - The Set of templates to add this to. Should a duplicate be created, it will be discarded.
	 * @return a count of all collisions that occurred during this operation. If this count is greater than zero, then extra work done.
	 */
	private static int generateValidTemplates(int numOperands, int numOperators, LinkedList<Character> expression, int numStack, Set<PostfixTemplate> templates) {
		int collisions = 0;
		if (numOperands == 0 && numOperators == 0) {
			PostfixTemplate pt = new PostfixTemplate();
			pt.template = expression.toArray(new Character[expression.size()]);
			if (templates.contains(pt)) collisions++;
			else templates.add(pt);
			return collisions;
		}
		
		if (numOperands > 0) {
			expression.add('N');
			collisions += generateValidTemplates(numOperands-1, numOperators,expression, numStack+1, templates);
			expression.removeLast();
		}
		
		if (numStack < 2 || numOperators <= 0) return collisions;
		
		expression.add('O');
		collisions += generateValidTemplates(numOperands, numOperators-1, expression, numStack-1, templates);
		expression.removeLast();
		
		return collisions;
	}
}