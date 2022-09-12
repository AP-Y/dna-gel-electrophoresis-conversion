import java.util.ArrayList;

public class CircularDNA extends DNA {
	
	public CircularDNA(ArrayList<Object> cDNA) {
		super(cDNA);
	}
	
	public CircularDNA(String inp) {
		super(inp);
	}
	
	public CircularDNA(Electrophoresis ele) {
		super(ele);
	}
	
	public void setCuts() {
		if (getDNA().size() > 0) {
			super.setCuts();
		} else {
			for (Enzyme enzyme : getEnzymes()) {
				setTotalCuts(getTotalCuts() + enzyme.getNumFragments());
				enzyme.setNumCuts(enzyme.getNumFragments());
			}
		}
	}
	
	public void codeRepresentation() {
		super.codeRepresentation();
		setRep(getRep() + ">");
	}
	
	public static boolean isValidInput(String inp) {
		if (!DNA.isValidInput(inp)) {
			return false;
		}
		if (!(Character.isUpperCase(inp.charAt(0)) && inp.charAt(inp.length()-2) == '-' && inp.charAt(inp.length()-1) == '>')) {
			return false;
		}
		return true;
	}

	public void decodeRepresentation(String inp) {
		super.decodeRepresentation(inp.substring(0, inp.length()-1));
	}
	
	public String testTrack(String enzymeName, int[] perm, int max, String longestTest) {	
		String res = testTrack(enzymeName, perm, (perm[perm.length-1] == max) ? true : false);		
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
				if (perm[perm.length-1] == max) {
					int[] permTmp = new int[perm.length+1];
					permTmp[0] = 1;
					for (int j = 1; j < permTmp.length; j++) {
						permTmp[j] = perm[j-1];
					}
					permTmp[permTmp.length-1]--;
					return testTrack(enzymeName, permTmp, max, longestTest);
				} else if (perm[0] < max-1) {
					int[] permTmp = perm.clone();
					permTmp[0]++;
					permTmp[permTmp.length-1]--;
					return testTrack(enzymeName, permTmp, max, longestTest);
				} else {
					return null;
				}
			}
		}
		return res;
	}
}