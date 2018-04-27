package test;
import java.util.Set;
import util.MySet;
import util.PostfixTemplate;


public class Tests {
	public static void main(String[] args) {
		testOperatorSets();
		testPrePermutations();
		testPostfixTemplates();
		testGenericPermute();
		testMySet();
		countPostfixTemplates();
		testPrintInfix();
	}
	
	private static void testPrintInfix() {
		String solution = "4 2 2 2 2 2 4 - * - - 3 + - +";
		String[] array = solution.split(" ");
	}
	
	
	@SuppressWarnings("unused")
	private static void countPostfixTemplates() {
		for (int i = 2; i < 20; i++) {
			int size = PostfixTemplate.generateValidTemplates(i).size();
			System.out.printf("f(%d) = %d\n", i, size);
		}
	}
	
	@SuppressWarnings("unused")
	private static void testPostfixTemplates() {
		Set<PostfixTemplate> templates = PostfixTemplate.generateValidTemplates(7);
		for (PostfixTemplate pt : templates)
			System.out.println(pt);
		System.out.println("Found " + templates.size() + " templates");
	}
	
	@SuppressWarnings("unused")
	private static void testMySet() {
		String[] array = "1234".split("");
		Set<MySet> set = MySet.getUniquePermutations(array);
		for (MySet ms : set)
			System.out.println(ms);
	   
		array = "1122".split("");
		set = MySet.getUniquePermutations(array);
		for (MySet ms : set)
			System.out.println(ms);
	}
}