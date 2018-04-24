package util;
/**
* Group of operators because I over engineer everything.
* @author Ben
*/
public enum Operator {
    ADD      ('+'),
    SUBTRACT ('-'),
    MULTIPLY ('*'),
    DIVIDE   ('/');
   
    char c;
   
    Operator(char c) {
        this.c = c;
    }
   
    public double operate(double a, double b) {
        switch(c) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            default:
                return a / b;
        }
    }
   
    @Override
    public String toString() {
        return c + "";
    }
   
    public static Operator getOperator(char c) {
        switch (c) {
            case '+':
                return ADD;
            case '-':
                return SUBTRACT;
            case '*':
                return MULTIPLY;
            case '/':
                return DIVIDE;
        }
        return null;
    }
 
    public static Operator getOperator(int j) {
        switch (j) {
            case 0:
                return ADD;
            case 1:
                return SUBTRACT;
            case 2:
                return MULTIPLY;
            case 3:
                return DIVIDE;
        }
        return null;
    }
   
    /**
     * Creates all possible orders of operators.
     * This is done by counting in base 4 to 4^numOperators and mapping each digit to one operator.
     * @return an array of all arrays of operators.
     */
    public static Operator[][] getAllOperatorCombos (int numOperators) {
        int base = values().length;
        int numOpCombos = (int)Math.pow(base, numOperators);
       
        Operator[][] operators = new Operator[numOpCombos][numOperators];
 
        // precalculate powers. no need to calculate more than once.
        int[] powers4 = new int[numOperators];
        for (int i = 0; i < numOperators; i++) {
            powers4[i] = (int)Math.pow(base, i);
        }
       
        for (int n = 0; n < numOpCombos; n++) {
            int t = n;
            Operator[] combo = new Operator[numOperators];
           
            for (int m = numOperators-1; m >= 0; m--) { 
                int power4 = powers4[m];
                int index = t / power4;
 
                while (t >= power4)
                    t -= power4;
               
                combo[m] = values()[index];
            }
            operators[n] = combo;
        }
        return operators;   
    }
}