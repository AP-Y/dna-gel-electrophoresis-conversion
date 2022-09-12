import java.util.ArrayList;
import java.util.Arrays;

/* Resources: 
 * Ternary Operator: https://www.baeldung.com/java-ternary-operator
 * Initialize Array In-Line: https://stackoverflow.com/questions/1154008/any-way-to-declare-an-array-in-line
 * Initialize ArrayList In-Line: https://www.geeksforgeeks.org/initialize-an-arraylist-in-java/
 * Copy Array: https://stackoverflow.com/questions/5785745/make-copy-of-an-array
 * Copy Portion of Array: https://stackoverflow.com/questions/10423134/java-copy-section-of-array
 * Count Occurances of Element in ArrayList: https://stackoverflow.com/questions/505928/how-to-count-the-number-of-occurrences-of-an-element-in-a-list
 * Check if Character is Uppercase Letter: https://kodejava.org/how-do-i-know-if-a-character-is-uppercase/
 * Get Current Class Name: https://stackoverflow.com/questions/6271417/java-get-the-current-class-name
 * Switch Statement: https://www.w3schools.com/java/java_switch.asp
 * Convert Object[] to String[]: https://stackoverflow.com/questions/3880274/how-to-convert-the-object-to-string-in-java
 * printf within String: https://stackoverflow.com/questions/47045/sprintf-equivalent-in-java
 * Transpose Matrix: https://stackoverflow.com/questions/26197466/transposing-a-matrix-from-a-2d-array
 * Permutations of Array: https://stackoverflow.com/questions/2920315/permutation-of-array
 * Method Hiding: https://stackoverflow.com/questions/2475259/can-i-override-and-overload-static-methods-in-java
 */

public class Helper {
	public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * Generate a random integer between min and max, inclusive
	 * @param min	included lower bound of random integer to generate
	 * @param max	included upper bound of random integer to generate
	 * 				max >= min
	 * @return		random integer between min and max, inclusive
	 */
	public static int randomInt(int min, int max) {
		return min + (int) ((max - min + 1) * Math.random());
	}
	
	/**
	 * Add all possible orderings of arr to perms
	 * @param arr	an array of integers to be re-ordered
	 * 				arr.length > 0
	 * @param start	the first index to start re-ordering arr at
	 * 				the first time permuations() is called on a particular arr, start == 0
	 * @param save	an ArrayList of integer arrays that will hold all possible orderings of arr after execution
	 * 				the first time permutations() is called on a particular arr, perms.size() == 0
	 */
	public static void permutations(int[] arr, int start, ArrayList<int[]> perms) {
	    for(int i = start; i < arr.length; i++){
	        int temp = arr[start];
	        arr[start] = arr[i];
	        arr[i] = temp;
	        permutations(arr, start+1, perms);
	        arr[i] = arr[start];
	        arr[start] = temp;
	    }
	    if (start == arr.length - 1) {
	    	perms.add(arr.clone());
	    }
	}
		
	/**
	 * Convert an ArrayList of Objects into an array of Objects
	 * @param list	an ArrayList of Objects to be converted into an array of Objects
	 * 				list.size() > 0
	 * @return		an array of Objects representing the ArrayList of Objects passed in
	 */
	public static Object[] listToArray(ArrayList<Object> list) {
		Object[] res = new Object[list.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = list.get(i);
		}
		return res;
	}
	
	/**
	 * Convert an array of Objects to an Array of Strings
	 * @param o	an array of Objects to be converted into an array of Strings
	 * 			o.get(i) instanceof String for i in [0, o.size())
	 * @return	an array of Strings representing the array of Objects passed in
	 */
	public static String[] objectArrToStringArr(Object[] o) {
		return Arrays.copyOf(o, o.length, String[].class);
	}
	
	/**
	 * Convert an array of Objects to an Array of integers
	 * @param o	an array of Objects to be converted into an array of integers
	 * 			o.get(i) instanceof Integer for i in [0, o.size())
	 * @return	an array of integers representing the array of Objects passed in
	 */
	public static int[] objectArrToIntArr(Object[] o) {
		int[] res = new int[o.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (int) o[i];
		}
		return res;
	}
	
	/**
	 * Transpose a matrix so that the rows become columns and the columns become rows
	 * @param mat	a 2D array of integers to be transposed
	 * 				mat has at least 1 element
	 * @return		a 2D array in which the rows and columns from the original have been switched
	 */
	public static int[][] transposeMatrix(int[][] mat) {
		int[][] res = new int[mat[0].length][mat.length];
		for (int i = 0; i < mat[0].length; i++) {
			for (int j = 0; j < mat.length; j++) {
				res[i][j] = mat[j][i];
			}
		}
		return res;
	}
	
	/**
	 * Print a pretty line
	 * @param size	an integer representing length of the line
	 */
	public static void printLine(int size) {
		for (int i = 0; i < size; i++) {
			if (i % 4 == 0) {
				System.out.print("_");
			} else if (i % 4 == 1 || i % 4 == 3) {
				System.out.print("-");
			} else {
				System.out.print("^");
			}
		}
		System.out.println("\n");
	}
}