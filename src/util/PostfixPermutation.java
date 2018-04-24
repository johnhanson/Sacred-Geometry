package util;
 
import java.util.EmptyStackException;
import java.util.Objects;
import java.util.Stack;
 
/**
* This class encapsulates
* a postfix expression,
* its solution (or the exception raised trying to calculated it),
* and the score of the solution
* @author Ben
*
*/
public class PostfixPermutation {
    Object[] entities;
    Double result;
    Exception exception;
   
    // parent hash was part of the previous approach which helped to remove transitive duplicates (ie: a+b == b+a, therefore dont store both)
    // however, it's possible that it's not really needed anymore.
    Integer parentHash;
    int tier;
   
    public PostfixPermutation(Object[] entities) {
        this.entities = entities;
    }
   
    public PostfixPermutation(Object[] entities, int parentHash) {
        this.entities = entities;
        this.parentHash = parentHash;
    }
   
    public boolean equals(Object o) {
        if (o instanceof PostfixPermutation) {
            return hashCode() == o.hashCode();
        }
        return false;
    }
   
    public int hashCode() {
        return Objects.hash(parentHash, result, exception);
    }   
   
    public Double getResult() {
        if (result != null)
            return result;
       
        if (exception == null) {
            try {
                calculate();
                return result;
            } catch (Exception e) {
                exception = e;
                return null;
            }
        }
        return null;
    }
   
    public Exception getException() {
        return exception;
    }
 
    public int getTier() {
        return tier;
    }
   
    public void setTier(int tier) {
        this.tier = tier;
    }
   
    public String getInfixExpression() {
        // TODO: Unimplemented
    	Object[] stack = new Object[entities.length/2 + 1];
    	// location of the next object in the "stack"
    	// initialized to -1 when stack is empty
    	int currentTop = -1;
    	char c;
    	Object a;
    	Object b;
    	
    	for (Object o : entities) {
    		c = (char) o;
    		if (c == '+' || c == '-' || c == '*' || c == '/') {
    	    	StringBuilder sb = new StringBuilder();
    			b = stack[currentTop];
    			currentTop--;
    			a = stack[currentTop];
    			sb.append("(");
    			sb.append(a);
    			sb.append(c);
    			sb.append(b);
    			sb.append(")");
    			stack[currentTop] = sb.toString();
    		}
    		else {
    			currentTop++;
    			stack[currentTop] = o;
    		}
    	}
    	
    	
        return (String) stack[0];
    }
   
    public String getExpression() {
        StringBuilder b = new StringBuilder();
        for (Object o : entities) {
            b.append(" ").append(o);
        }
        return b.substring(1);   
    }
   
    public String toString() {
        if (result != null)
            return getExpression() + " = " + result;
        return getExpression();
    }
   
    public void calculate() throws Exception {
        Stack<Double> stack = new Stack<>();
        try {
            for (Object entity : entities) {
                if (entity instanceof Operator) {
                    double b = stack.pop();
                    double a = stack.pop();
                    double c = ((Operator)entity).operate(a, b);
                    // There are some shenanigans afoot were even if a divide by zero occurs, it can be recovered from with a subsequent divide.
//                    if (Double.isInfinite(c))
//                        throw new Exception("Divide By Zero Error");
                    stack.push(c);
                } else {
                    stack.push(Double.valueOf(entity.toString()));
                }
            }
            result = stack.pop();
        } catch (EmptyStackException ese) {
            throw new Exception("Invalid Expression.");
        }       
    }
}