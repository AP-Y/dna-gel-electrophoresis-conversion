import java.util.Arrays;

public class Enzyme {
	private String name;
	private int[] fragmentSizes;
	private int numCuts;
	private int numFragments;
	
	/**
	 * Create Enzyme object with a given name
	 * @param name	a String representing the name of this enzyme
	 */
	public Enzyme(String name) {
		this.name = name;
	}
	
	/**
	 * Create Enzyme object with a given name that produces fragments of given sizes when applied to a DNA segment
	 * @param name			a String representing the name of this enzyme
	 * @param fragmentSizes	an array of integers, where each integer represents the size of a fragment produced when this enzyme is applied to a DNA segment
	 * 						
	 */
	public Enzyme(String name, int[] fragmentSizes) {
		this(name);
		this.fragmentSizes = fragmentSizes;
		numFragments = fragmentSizes.length;
	}
	
	/**
	 * Create Enzyme object with a given name that makes a given number of cuts when applied to a DNA segment
	 * @param name		a String representing the name of this enzyme
	 * @param numCuts	an integer representing the number of cuts this enzyme makes when individually applied to a DNA segment
	 */
	public Enzyme(String name, int numCuts) {
		this(name);
		this.numCuts = numCuts;
	}

	/**
	 * @return	single uppercase letter representing name of enzyme
	 * 			name.length() == 1
	 * 			Character.isUpperCase(name.charAt(0))
	 */
	public String getName() { return name; }
	
	/**
	 * @return	array of integers, where each integer represents the length of a fragment produced by individually applying this enzyme to the DNA segment
	 * 			fragmentSizes.length > 0
	 * 			fragmentSizes[i] > 0 for i in [0, fragmentSizes.length)
	 */
	public int[] getFragmentSizes() { return fragmentSizes; }
	
	/**
	 * @return	an integer number of cuts produced by individually applying this enzyme to the DNA segment
	 * 			numCuts > 0
	 */
	public int getNumCuts() { return numCuts; }
	
	/**
	 * @return an integer number of fragments produced by individually applying this enzyme to the DNA segment
	 * 			numFragments > 0
	 */
	public int getNumFragments() { return numFragments; }
	
	public void setNumCuts(int numCuts) { this.numCuts = numCuts; }
	
	public void setNumFragments(int numFragments) { this.numFragments = numFragments; }
	
	public String toString() {
		return "Enzyme " + name + ": " +
					"\n\tNumber of Cuts: " + numCuts +
					"\n\tNumber of Fragments: " + numFragments +
					(fragmentSizes != null ? "\n\tSize of Fragments: " + Arrays.toString(fragmentSizes) : "");
	}
}