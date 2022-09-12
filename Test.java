import java.util.ArrayList;
import java.util.Scanner;

/*
 * 1. 
 * 		Hello!
 * 		-----
 * 		-AB-
 * 		B---A-
 * 		-A---B--B---A--A-
 * 		-C---C---C----
 * 2. 
 * 		-A---B--A--
 * 		A--B--
 * 		A-B---B--A--B---A---->
 * 		C--C---->
 * 3. 
 * 	 a.
 * 		12
 * 		apaI,  BanII, -1
 * 		1, 1, 2, 8, -1
 * 		2, 4, 6, -1
 * 		1, 1, 2, 2, 3, 3, -1
 *   b.
 *   	15
 *   	Apa1, banII, -1
 *   	4, 5, 6, -1
 *   	3, 4, 8, -1
 *   	1, 2, 2, 3, 3, 4, -1
 *   c.
 *   	10
 *   	Csp8I, -1
 *   	3, 3, 4, 7, -1
 *   d.
 *   	9
 *  	a,  B, -1
 *  	8, -1
 *  	8, -1
 *  	2, 6, -1
 *  4. 
 *  	-10, 0, 3
 *  	3
 *  	10
 *  	2
 *  	1, 5
 *  5. 
 */

public class Test {
	public static final Scanner INPUT = new Scanner(System.in);
	
	public static void main(String[] args) {		
		System.out.println("Please input:");
		System.out.println("\t1 to analyse a given linear DNA segment;");
		System.out.println("\t2 to analyse a given circular DNA segment;");
		System.out.println("\t3 to analyse a given electrophoresis track;");
		System.out.println("\t4 to analyse a randomly generated DNA segment.");
		System.out.println("\t5 to perform a pre-arranged test of all functionalities.");
		int inp = INPUT.nextInt();
		while (inp < 1 || inp > 5) {
			printInvalid();
			inp = INPUT.nextInt();
		}
		System.out.println();
		
		switch (inp) {
			case 1: 
				analyzeLDNA();
				break;
			case 2: 
				analyzeCDNA();
				break;
			case 3:
				analyzeElectrophoresis();
				break;
			case 4:
				randomTest();
				break;
			case 5:
				myTest();
				break;
		}
	}
	 
	/**
	 * Prompts user to input everything needed to analyse a LinearDNA object
	 */
	public static void analyzeLDNA() {
		System.out.println("Please input your linear DNA segment with the desired enzyme cuts, using the following example as a guide.");
		System.out.println("\t\"-A---B--B---A--A-\" represents a linear DNA segment that is 12 kilobases long because there are 12 dashes.");
		System.out.println("\tIt was cut 3 times by enzyme A and 2 times by enzyme B, producing a total of 6 fragments.");
		System.out.println("\tNote that enzyme names must be denoted by a single uppercase letter;");
		System.out.println("\tmultiple enzymes cannot make the same cut (for example, \"-AB-\" is invalid);");
		System.out.println("\tan enzyme cannot make a cut at either end of the DNA segment (for example, \"B---A-\" is invalid).");
        String inp = INPUT.nextLine();
        while (!LinearDNA.isValidInput(inp)) {
        	if (!inp.equals("")) { printInvalid(); }
        	inp = INPUT.nextLine();
        }
        printAll(new LinearDNA(inp));
	}
	
	/**
	 * Prompts user to input everything needed to analyse a CircularDNA object
	 */
	public static void analyzeCDNA() {
		System.out.println("Please input your circular DNA segment with the desired enzyme cuts, using the following example as a guide.");
		System.out.println("\t\"A-B---B--A--B---A---->\" represents a circular DNA segment that is 15 kilobases long because there are 15 dashes.");
		System.out.println("\tIt was cut 3 times by enzyme A and 3 times by enzyme B, producing a total of 6 fragments.");
		System.out.println("\tNote that enzyme names must be denoted by a single uppercase letter;");
		System.out.println("\tmultiple enzymes cannot make the same cut (for example, \"-AB-\" is invalid);");
		System.out.println("\tyour input must begin with a cut and end with a fragment followed by \">\" to denote that the end connects to the start;");
        String inp = INPUT.nextLine();
        while (!CircularDNA.isValidInput(inp)) {
        	if (!inp.equals("")) { printInvalid(); }
        	inp = INPUT.nextLine();
        }
        printAll(new CircularDNA(inp));
	}
	
	/**
	 * Prompts user to input everything needed to analyse a given Electrophoresis object
	 */
	public static void analyzeElectrophoresis() {
		System.out.println("Please input the maximum number of kilobases on your electorphoresis scale.");
		System.out.println("\tThis must be a positive non-zero integer.");
		int maxScale = INPUT.nextInt();
		while (maxScale <= 0) {
			printInvalid();
			maxScale = INPUT.nextInt();
		}		
		System.out.println("Your electrophoresis has a maximum of " + maxScale + " kilobases on its scale.");
		
		System.out.println("Please input the names of all enzymes used on your DNA segment or -1 to quit.");
		System.out.println("\tNote that only the first letter of each enzyme name will be saved,");
		System.out.println("\tthus, each enzyme must start with a different letter;");
		System.out.println("\tyour electrophoresis must include at least 1 enzyme.");
		ArrayList<Object> enzymeNames = new ArrayList<Object>();
		String inp = INPUT.nextLine();
		while (!inp.equals("-1") || enzymeNames.size() == 0) {
			if (inp.length() > 0 && Character.isLetter(inp.charAt(0))) {
				String enzymeLetter = ("" + inp.charAt(0)).toUpperCase();
				if (enzymeNames.indexOf(enzymeLetter) == -1) {
					enzymeNames.add(enzymeLetter);
				}
			}
			inp = INPUT.nextLine();
		}
		System.out.println("Your electrophoresis includes the following enzymes: " + enzymeNames);
		
		ArrayList<ArrayList<Object>> fragmentsByEnzyme = new ArrayList<ArrayList<Object>>();		
		for (int i = 0; i < enzymeNames.size() + ((enzymeNames.size() > 1) ? 1 : 0); i++) {
			fragmentsByEnzyme.add(new ArrayList<Object>());
			System.out.println("Please input the length of the fragments produced by " + 
								((i < enzymeNames.size()) ? "enzyme " + enzymeNames.get(i) : "all enzymes") + " or -1 to quit.");
			System.out.println("\tEach fragment size must be a positive non-zero integer less than or equal to " + maxScale + " kilobases.");
			int inp2 = INPUT.nextInt();
			while (inp2 != -1) {
				if (inp2 > 0 && inp2 <= maxScale) {
					fragmentsByEnzyme.get(i).add(inp2);
				}
				inp2 = INPUT.nextInt();
			}
		}
		for (int i = 0; i < enzymeNames.size(); i++) {
			System.out.println("When individually applied to the DNA segment, enzyme " + enzymeNames.get(i) + " made fragments of the following sizes: " + 
								fragmentsByEnzyme.get(i));;
		}
		if (enzymeNames.size() > 1) {
			System.out.println("When simultaneously applied to the DNA segment, all the enzymes made fragments of the following sizes: " + 
								fragmentsByEnzyme.get(fragmentsByEnzyme.size()-1));
		}
		
		Enzyme[] enzymes = new Enzyme[enzymeNames.size()];
		for (int i = 0; i < enzymeNames.size(); i++) {
			enzymes[i] = new Enzyme((String) enzymeNames.get(i), Helper.objectArrToIntArr(Helper.listToArray(fragmentsByEnzyme.get(i))));
		}
		
		printAll((new Electrophoresis(maxScale, enzymes, enzymeNames.size() > 1 ? 
				Helper.objectArrToIntArr(Helper.listToArray(fragmentsByEnzyme.get(fragmentsByEnzyme.size()-1))) : new int[0])).getDNA());
}
	
	/**
	 * Creates random DNA and associated Electrophoresis objects based on given parameters
	 * @param numEnzymes	maximum number of different types of enzymes that can be applied to the DNA segment, inclusive
	 * 						numEnzymes > 0
	 * 						numEnzymes <= 26
	 * @param minSize		minimum  number of fragments and cuts to be included in dna ArrayList, inclusive
	 * 						minSize >= 3
	 * @param maxSize		maximum  number of fragments and cuts to be included in dna ArrayList, inclusive
	 * 						maxSize >= minSize
	 * @param minFragment	minimum length of each fragment produced by cutting the DNA segment with all the enzymes simultaneously
	 * 						minFragment > 0
	 * @param maxFragment	maximum length of each fragment produced by cutting the DNA segment with all the enzymes simultaneously
	 * 						maxFragment >= minFragment
	 */
	public static void randomTest(int numEnzymes, int minSize, int maxSize, int minFragment, int maxFragment) {
		String enzymes = Helper.LETTERS.substring(0, numEnzymes);
		int num = Helper.randomInt(minSize, maxSize);
		boolean isLinear = num % 2 == 1;
		ArrayList<Object> dna = new ArrayList<Object>(num);
		for (int i = 0; i < num; i++) {
			if ((isLinear && i % 2 == 0) || (!isLinear && i % 2 == 1)) {
				dna.add(Helper.randomInt(minFragment, maxFragment));
			} else {
				dna.add(String.valueOf(enzymes.charAt(Helper.randomInt(0, numEnzymes-1))));
			}
		}
		if (isLinear) {
			printAll(new LinearDNA(dna));
		} else {
			printAll(new CircularDNA(dna));
		}
	}

	/**
	 * Prompts user to input everything needed to create random DNA and associated Electrophoresis objects
	 */
	public static void randomTest() {
		System.out.println("Please input the maximum number of enzymes that may be applied to the DNA segment.");
		System.out.println("\tThis must be a positive non-zero integer less than or equal to 26.");
		int numEnzymes = INPUT.nextInt();
		while (numEnzymes <= 0 || numEnzymes > 26) {
			printInvalid();
			numEnzymes = INPUT.nextInt();
		}
		System.out.println("Your DNA segment will be cut by at most " + numEnzymes + " enzymes.");
		
		System.out.println("Please input the minimum number of fragments and cuts you would like on your DNA segment.");
		System.out.println("\tThis must be a positive integer greater than or equal to 3.");
		int minSize = INPUT.nextInt();
		while (minSize < 3) {
			printInvalid();
			minSize = INPUT.nextInt();
		}
		System.out.println("Your DNA segment will have at least " + minSize + " fragments and cuts.");
		
		System.out.println("Please input the maximum number of fragments and cuts you would like on your DNA segment.");
		System.out.println("\tThis must be a positive integer greater than or equal to " + minSize + ".");
		int maxSize = INPUT.nextInt();
		while (maxSize < minSize) {
			printInvalid();
			maxSize = INPUT.nextInt();
		}
		System.out.println("Your DNA segment will have at most " + maxSize + " fragments and cuts.");
		
		System.out.println("Please input the minimum size of each fragment on your DNA segment.");
		System.out.println("\tThis must be a positive non-zero integer.");
		int minFragment = INPUT.nextInt();
		while (minFragment <= 0) {
			printInvalid();
			minFragment = INPUT.nextInt();
		}
		System.out.println("Each fragment on your DNA segment will be at least " + minFragment + " kilobases long.");
		
		System.out.println("Please input the maximum size of each fragment on your DNA segment.");
		System.out.println("\tThis must be a positive integer greater than or equal to " + minFragment + " kilobases.");
		int maxFragment = INPUT.nextInt();
		while (maxFragment < minFragment) {
			printInvalid();
			maxFragment = INPUT.nextInt();
		}
		System.out.println("Each fragment on your DNA segment will be at most " + maxFragment + " kilobases long.");
		
		randomTest(numEnzymes, minSize, maxSize, minFragment, maxFragment);
	}
	
	/**
	 * Perform a pre-arranged test of all functionalities
	 */
	@SuppressWarnings("serial")
	public static void myTest() {
		System.out.println("LinearDNA String --> Electrophoresis\n");
		printAll(new LinearDNA("-A---B--B---A--A-"));
		System.out.println("CircularDNA String --> Electrophoresis\n");
		printAll(new CircularDNA("A-B---B--A--B---A---->"));
		System.out.println("LinearDNA ArrayList --> Electrophoresis\n");
		printAll(new LinearDNA(new ArrayList<Object>() {{
			add(1);
			add("A");
			add(3);
			add("B");
			add(2);
			add("B");
			add(3);
			add("A");
			add(2);
			add("A");
			add(1);
		}}));
		System.out.println("CircularDNA ArrayList --> Electrophoresis\n");
		printAll(new CircularDNA(new ArrayList<Object>() {{
			add("A");
			add(1);
			add("B");
			add(3);
			add("B");
			add(2);
			add("A");
			add(2);
			add("B");
			add(3);
			add("A");
			add(4);
		}}));
		System.out.println("Electrophoresis --> LinearDNA\n");
		printAll((new Electrophoresis(12,
				new Enzyme[]{new Enzyme("A", new int[]{1, 1, 2, 8}), new Enzyme("B", new int[]{2, 4, 6})},
				new int[]{1, 1, 2, 2, 3, 3})).getDNA());
		System.out.println("Electrophoresis --> CircularDNA\n");
		printAll((new Electrophoresis(15,
				new Enzyme[]{new Enzyme("A", new int[]{4, 5, 6}), new Enzyme("B", new int[]{3, 4, 8})},
				new int[]{1, 2, 2, 3, 3, 4})).getDNA());
	}
	
	/**
	 * Print all information associated with a DNA object and its associated Electrophoresis
	 * @param dna	a DNA object
	 * 				dna != null
	 */
	public static void printAll(DNA dna) {
		Helper.printLine(80);
		System.out.println(dna);
		if (dna.isValid()) {
			System.out.println();
			System.out.println(dna.getEle());
			System.out.println();
			for (Enzyme enzyme : dna.getEnzymes()) {
				System.out.println(enzyme);
				System.out.println();
			}
		}
		Helper.printLine(80);
	}
		
	/**
	 * Inform the user that their input was invalid and prompt them to try again
	 */
	public static void printInvalid() {
		System.out.println("Your input was inavlid. Please try again.");
	}
}