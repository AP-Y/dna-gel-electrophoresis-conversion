import java.util.ArrayList;

public class LinearDNA extends DNA {
	
	public LinearDNA(ArrayList<Object> lDNA) {
		super(lDNA);
	}
	
	public LinearDNA(String inp) {
		super(inp);
	}
	
	public LinearDNA(Electrophoresis ele) {
		super(ele);
	}
	
	public void setCuts() {
		if (getDNA().size() > 0) {
			super.setCuts();
		} else {
			for (Enzyme enzyme : getEnzymes()) {
				setTotalCuts(getTotalCuts() + enzyme.getNumFragments()-1);
				enzyme.setNumCuts(enzyme.getNumFragments()-1);
			}
		}
	}
	
	public static boolean isValidInput(String inp) {
		if (!DNA.isValidInput(inp)) {
			return false;
		}
		if (!(inp.charAt(0) == '-' && inp.charAt(inp.length()-1) == '-')) {
			return false;
		}
		return true;
	}	
}