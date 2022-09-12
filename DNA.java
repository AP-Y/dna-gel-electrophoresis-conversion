import java.util.ArrayList;
import java.util.Arrays;

public abstract class DNA {
	private Electrophoresis ele;
	private ArrayList<Object> dna;
	private String rep;
	private int lengthDNA;
	private int totalCuts;
	private Enzyme[] enzymes;
	
	/**
	 * Create DNA object based on given ArrayList of Objects
	 * @param dna	an ArrayList containing alternating Strings and Integers
	 * 				dna != null
	 */
	public DNA(ArrayList<Object> dna) {
		this.dna = dna;
		codeRepresentation();
		setLengthDNA();
		setCuts();
		if (!isValid()) {
			clearObject();
			return;
		}
		ele = new Electrophoresis(this);
	}
	
	/**
	 * Create DNA object based on user input in a String format
	 * @param inp	a String representing a DNA segment, formated the same as that produced in toString()
	 * 				inp != null
	 */
	public DNA(String inp) {
		if (!isValidInput(inp)) {
			return;
		}
		dna = new ArrayList<Object>();
		rep = inp;
		decodeRepresentation(inp);
		setLengthDNA();
		setCuts();
		if (!isValid()) {
			clearObject();
			return;
		}
		ele = new Electrophoresis(this);
	}
	
	/**
	 * Create one possible DNA object based on given Electrophoresis
	 * (a single Electrophoresis object could come from multiple DNA objects,
	 * but a DNA object can only produce a single Electrophoresis object)
	 * @param ele	an Electrophoresis object
	 * 				ele != null
	 */
	public DNA(Electrophoresis ele) {
		this.ele = ele;
		dna = new ArrayList<Object>();
		setLengthDNA();
		enzymes = ele.getEnzymes();
		setCuts();
		
		if (enzymes.length == 1 && !(this instanceof LinearDNA && enzymes[0].getFragmentSizes().length == 1)) {
			rep = testTrack(enzymes[0].getName(), enzymes[0].getFragmentSizes(), (this instanceof LinearDNA) ? false : true);
			if (this instanceof CircularDNA) { rep += ">"; }
			decodeRepresentation(rep);
			return;
		}
		
		if (this instanceof CircularDNA && ele.moreThanOneEnzyme()) {
			boolean singleCuts = true;
			for (Enzyme enzyme : enzymes) {
				if (!(enzyme.getNumFragments() == 1 && enzyme.getFragmentSizes()[0] == lengthDNA)) {
					singleCuts = false;
					break;
				}
			}
			if (singleCuts) {
				for (int i = 0; i < enzymes.length; i++) {
					dna.add(enzymes[i].getName());
					dna.add(ele.getAllEnzymesTrack()[i]);
				}
				codeRepresentation();
				return;
			}
		}
		
		int totalCuts = 0;
		int totalFragments = (this instanceof LinearDNA) ? -1 : 0;
		for (Enzyme enzyme : enzymes) {
			totalCuts += enzyme.getNumCuts();
			totalFragments += enzyme.getNumFragments();
		}
		
		if (totalCuts == this.totalCuts && totalFragments == ele.getTotalFragments()) {
			ArrayList<ArrayList<int[]>> permsByEnzyme = new ArrayList<ArrayList<int[]>>();
			if (ele.moreThanOneEnzyme()) {
				permsByEnzyme.add(new ArrayList<int[]>());			
				Helper.permutations(ele.getAllEnzymesTrack(), 0, permsByEnzyme.get(0));
			}
			for (Enzyme enzyme : enzymes) {
				ArrayList<int[]> tmp = new ArrayList<int[]>();
				Helper.permutations(enzyme.getFragmentSizes(), 0, tmp);
				permsByEnzyme.add(tmp);
			}
			
			int[] count = new int[permsByEnzyme.size()];
			int[] sizes = new int[permsByEnzyme.size()];
			for (int i = 0; i < sizes.length; i++) {
				sizes[i] = permsByEnzyme.get(i).size() - 1;
			}
									
			for (int i = 0; i < count.length; i++) {
				test(i, count, sizes, permsByEnzyme);
			}
		}
		
		if (!isValid()) {
			clearObject();
		}
	}
	
	/**
	 * @return	the Electrophoresis object created from this DNA object
	 */
	public Electrophoresis getEle() { return ele; }
	
	/**
	 * @return	an ArrayList of alternating Strings and Integers, 
	 * 			where each String represents a cut by an enzyme,
	 * 			and each Integer represents the number of kilobases between the previous and subsequent cut
	 * 			If LinearDNA object: dna.get(0) && dna.get(dna.size()-1) are Integers
	 * 			If CircularDNA object: dna.get(0) is a String && dna.get(dna.size()-1) is an Integer
	 */
	public ArrayList<Object> getDNA() { return dna; }
	
	/**
	 * @return	a String representation of this DNA object, 
	 * 			where each "-" represents 1 kilobase in a fragment,
	 * 			and each letter represents a cut made by an enzyme
	 * 			If LinearDNA object: rep.charAt(0) == '-' && rep.charAt(rep.length()-1) == '-'
	 * 			If CircularDNA object: Character.isUpperCase(rep.charAt(0)) && rep.charAt(rep.length()-2) == '-' && rep.charAt(rep.lenght()-1) == '>'
	 */
	public String getRep() { return rep; }
	
	/**
	 * @return	an integer representing the number of kilobases in the DNA segment
	 * 			lengthDNA > 0
	 */
	public int getLengthDNA() { return lengthDNA; }
	
	/**
	 * @return	an integer representing the total number of cuts all enzymes made on the DNA segment
	 * 			totalCuts > 0
	 */
	public int getTotalCuts() { return totalCuts; }
	
	/**
	 * @return	an array of Enzymes used on the DNA segment
	 * 			enzymes.length > 0
	 */
	public Enzyme[] getEnzymes() { return enzymes; }
	
	/**
	 * @return	true if more than one enzyme was used on this DNA segment; false otherwise
	 */
	public boolean moreThanOneEnzyme() { return enzymes.length > 1; }
	
	/**
	 * @return	true if this DNA object is valid; false otherwise
	 */
	public boolean isValid() { return dna != null && dna.size() > 0 && lengthDNA > 0; }
	
	public void setEle(Electrophoresis ele) { this.ele = ele; }
	
	public void setDNA(ArrayList<Object> dna) { this.dna = dna; }
	
	public void setRep(String rep) { this.rep = rep; }
	
	public void setLengthDNA(int lengthDNA) { this.lengthDNA = lengthDNA; }
	
	public void setTotalCuts(int totalCuts) { this.totalCuts = totalCuts; }
	
	public void setEnzymes(Enzyme[] enzymes) { this.enzymes = enzymes; }
	
	/**
	 * Set lengthDNA
	 */
	public void setLengthDNA() {
		lengthDNA = 0;
		if (dna.size() > 0) {
			for (Object o : dna) {
				if (o instanceof Integer) {
					lengthDNA += (int) o;
				}
			}
		} else {
			int c = ele.getTracks()[0].length-1;
			for (int r = 0; r < ele.getTracks().length; r++) {
				if (ele.getTracks()[r][c] > 0) {
					lengthDNA += ele.getTracks()[r][c]*(r + 1);
				}
			}
		}
	}
	
	/**
	 * Set numCuts for each enzyme used on this DNA segment and totalCuts
	 */
	public void setCuts() {
		ArrayList<String> enzymeNames = new ArrayList<String>();		
		for (Object o : dna) {
			if (o instanceof String) {
				if (!enzymeNames.contains(o)) {
					enzymeNames.add((String) o);
				}
			}
		}
		int[] numCuts = new int[enzymeNames.size()];
		for (Object o : dna) {
			if (o instanceof String) {
				numCuts[enzymeNames.indexOf((String) o)]++;
			}
		}
		enzymes = new Enzyme[enzymeNames.size()];
		for (int i = 0; i < enzymeNames.size(); i++) {
			enzymes[i] = new Enzyme(enzymeNames.get(i), numCuts[i]);
		}
		
		for (Integer i : numCuts) {
			totalCuts += i;
		}
	}
	
	/**
	 * Set rep based on dna
	 */
	public void codeRepresentation() {
		String res = "";
		for (Object o : dna) {
			if (o instanceof Integer) {
				for (int i = 0; i < (int) o; i++) {
					res += "-";
				}
			} else {
				res += o;
			}
		}
		rep = res;
	}
	
	/**
	 * Determine if user input represents a valid DNA object
	 * @param inp	a String generated from user input
	 * @return		true if this String represents a valid DNA object; false otherwise
	 */
	public static boolean isValidInput(String inp) {
		if (inp.length() < 3) { return false; }
		boolean atLeastOneCut = false;
		for (int i = 0; i < inp.length(); i++) {
			char c = inp.charAt(i);
			if (Character.isUpperCase(c)) { atLeastOneCut = true; }
			if (!((Character.isUpperCase(c) || c == '-') || (i == inp.length()-1 && c == '>'))) {
				return false;
			}
		}
		if (!atLeastOneCut) { return false; }
		for (int i = 0; i < inp.length()-1; i++) {
			if (Character.isUpperCase(inp.charAt(i)) && Character.isUpperCase(inp.charAt(i+1))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Set dna based on user input
	 * @param inp	a String generated from user input
	 */
	public void decodeRepresentation(String inp) {
		if (Character.isUpperCase(inp.charAt(0))) {
			dna.add("" + inp.charAt(0));
		}
		int lengthFragment = 0;
		for (int i = 0; i < inp.length(); i++) {
			char c = inp.charAt(i);
			if (c == '-') {
				lengthFragment++;
			} else {
				if (lengthFragment > 0) {
					dna.add(lengthFragment);
					dna.add("" + c);
					lengthFragment = 0;
				}
			}
		}
		if (lengthFragment > 0) {
			dna.add(lengthFragment);
		}
	}
	
	public String testTrack(String enzymeName, int[] perm, boolean cutFirst) {
		String res = "";
		for (int fragment : perm) {
			if (cutFirst) { res += enzymeName; }
			for (int i = 0; i < fragment; i++) {
				res += "-";
			}
			if (!cutFirst) { res += enzymeName; }
		}
		if (this instanceof CircularDNA && (enzymeName.equals("!") || cutFirst)) {
			return res;
		}
		return res.substring(0, res.length()-1);
	}
	
	public String testTrack(String enzymeName, int[] perm, int max, String longestTest) {
		String res = testTrack(enzymeName, perm, (this instanceof LinearDNA) ? false : true);
		for (int i = 0; i < longestTest.length(); i++) {
			char c1 = longestTest.charAt(i);
			char c2 = res.charAt(i);			
			if (c1 == '-' && c2 == '-') {
				continue; 
			} else if (c1 == '!' && Character.isAlphabetic(c2)) {
				continue;
			} else if (c1 == '!' && c2 == '-') {
				res = res.substring(0, i) + " " + res.substring(i);
			} else {
				return null;
			}
		}
		return res;
	}
	
	private String addTests(String[] tests) {
		String res = "";
		for (int i = 0; i < tests[0].length(); i++) {
			char enzyme = '-';
			boolean blank = true;
			for (String test : tests) {
				char c = test.charAt(i);
				if (Character.isLetter(c)) {
					if (enzyme == '-') {
						enzyme = c;
					} else {
						enzyme = '?';
						break;
					}
				}
				if (Character.isLetter(c) || c == '-') {
					blank = false;
				}
			}
			if (enzyme != '-') {
				res += enzyme;
			} else if (blank) {
				res += ' ';
			} else {
				res += "-";
			}
		}
		return res;
	}
	
	private boolean testEquality(String base, String test) {
		if (test.indexOf(" ") > -1 || test.indexOf("?") > -1) {
			return false;
		}
		for (int i = 0; i < base.length(); i++) {
			char c1 = base.charAt(i);
			char c2 = test.charAt(i);
			if (c1 == '-' && c2 == '-') {
				continue;
			} else if (c1 == '!' && Character.isAlphabetic(c2)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}
	
	private void test(int start, int[] count, int[] sizes, ArrayList<ArrayList<int[]>> permsByEnzyme) {
		if (getDNA().size() > 0) { return; }
		
		String[] tests = new String[count.length];
		for (int i = 0; i < count.length; i++) {			
			int[] tmp = permsByEnzyme.get(i).get(count[i]);
			if (i == 0) {
				tests[i] = testTrack("!", tmp, (this instanceof LinearDNA) ? false : true);
			} else {
				tests[i] = testTrack(getEnzymes()[i-1].getName(), tmp, tmp[tmp.length-1], tests[0]);
			}
		}
				
		boolean valid = true;
		for (String test : tests) {
			if (test == null) {
				valid = false;
				break;
			}
		}
		if (valid) {
			String fullTest = addTests(Arrays.copyOfRange(tests, 1, tests.length));
			if (testEquality(tests[0], fullTest)) {
				rep = fullTest + ((this instanceof LinearDNA) ? "" : ">");
				decodeRepresentation(fullTest);
				return;
			}
		}
			
		int[] countTmp = count.clone();
		countTmp[start]++;
		for (int i = start; i < count.length; i++) {
			if (countTmp[i] < sizes[i]) {
				test(i, countTmp, sizes, permsByEnzyme);
			}
		}
	}
		
	/**
	 * Clear this DNA object by nullifying all its instance variables
	 */
	public void clearObject() {
		ele = null;
		dna = null;
		rep = null;
		lengthDNA = 0;
		totalCuts = 0;
		enzymes = null;
	}
	
	public String toString() {
		String objName = getClass().getName();
		return  objName.substring(0, objName.indexOf("DNA")) + " DNA: " + ((isValid()) ?
				rep +
				"\n\tLength DNA: " + lengthDNA + 
				"\n\tTotal Cuts: " + totalCuts 
				:
				" Invalid"
				);
	}
}