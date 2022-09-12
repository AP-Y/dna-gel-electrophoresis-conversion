import java.util.Collections;

public class Electrophoresis {
	private DNA dna;
	private int[][] tracks;
	private int[] scale;
	private int totalFragments;
	private Enzyme[] enzymes;
	private int[] allEnzymesTrack;
	
	/**
	 * Create Electrophoresis object based on given LinearDNA object
	 * @param lDNA	an LinearDNA object
	 * 				lDNA != null
	 */
	public Electrophoresis(DNA dna) {
		if (!dna.isValid()) {
			return;
		}
		this.dna = dna;
		enzymes = dna.getEnzymes();
		setScale(dna.getLengthDNA());
		setTracks();
		setFragments();
	}
	
	/**
	 * Create Electrophoresis object based on user input
	 * @param maxScale			an integer representing the maximum number of kilobases on this Electrophoresis scale		
	 * 							maxScale >= 0
	 * @param enzymes			an array of Enzymes used in this Electrophresis
	 * 							enzymes != null
	 * @param allEnzymesTrack	an array of integers representing the length of fragments produced when all enzymes are simultaneously applied to the DNA segment
	 * 							allEnzymesTrack != null
	 */
	public Electrophoresis(int maxScale, Enzyme[] enzymes, int[] allEnzymesTrack) {
		setScale(maxScale);
		this.enzymes = enzymes;
		this.allEnzymesTrack = allEnzymesTrack;
		setTracks();
		setFragments();
		dna = new LinearDNA(this);
		if (!dna.isValid()) {
			dna = new CircularDNA(this);
		}
		if (!dna.isValid()) {
			clearObject();
		}
	}
	
	/**
	 * @return	the DNA object this Electrophoresis object was created from
	 */
	public DNA getDNA() { return dna; }
	
	/**
	 * @return	a 2D array, where each column represents the fragments each corresponding enzyme made when individually applied to the DNA segment;
	 * 			each column holds mainly 0s, but certain rows hold positive integers, 
	 * 			representing the fact that when that enzyme was applied to the DNA segment, it created that many fragments of the corresponding length;
	 * 			if moreThanOneEnzyme(), the last column represents the number of fragments created when all enzymes were simultaneously applied to the segment
	 * 			getTracks.length > 0 && getTracks[0].length > 0
	 * 			getTracks[r][c] >= 0 for r in [0, getTracks.length) and c in [0, getTracks[r].length)
	 */
	public int[][] getTracks() { return tracks; }
	
	/**
	 * @return	an array of all integers between 1 and dna.getLength(), inclusive
	 * 			getScale.length = dna.getLength()
	 */
	public int[] getScale() { return scale; }
	
	/**
	 * @return	an integer representing the total number fragments produced when all enzymes were simultaneously applied to the DNA segment
	 * 			totalFragments > 0
	 */
	public int getTotalFragments() { return totalFragments; }

	/**
	 * @return	an array of Enzymes used on the DNA segment
	 * 			enzymes.length > 0
	 * 			enzymes[i] != null for every i in [0, enzymes.length)
	 */
	public Enzyme[] getEnzymes() { return enzymes; }
	
	/**
	 * @return	an array of integers, where each integer represents the size of a fragment produced when all enzymes are simultaneously applied to a DNA segment
	 * 			if moreThanOneEnzyme(), allEnzymesTrack.lenght > 0; else allEnzymesTrack.length == 0, 
	 * 			allEnzymesTrack[i] > 0 for every i in [0, allEnzymesTrack.length)
	 */
	public int[] getAllEnzymesTrack() { return allEnzymesTrack; }
	
	/**
	 * @return	true if more than one enzyme was used in this electrophoresis; false otherwise
	 */
	public boolean moreThanOneEnzyme() { return enzymes.length > 1; }
		
	private void setScale(int max) {
		scale = new int[max];
		for (int i = 0; i < scale.length; i++) {
			scale[i] = i+1;
		}
	}
	
	private void setEnzymeTrack(int[] track, String enzymeName) {
		if (dna instanceof CircularDNA && Collections.frequency(dna.getDNA(), enzymeName) == 1) {
			track[track.length-1] = 1;
			return;
		}
		
		int lengthFragment = 0;
		for (Object o : dna.getDNA()) {
			if (o instanceof Integer) {
				lengthFragment += (int) o;
			} else {
				if (((String) o).equals(enzymeName) && lengthFragment > 0) {
					track[lengthFragment-1]++;
					lengthFragment = 0;
				}
			}
		}
				
		if (lengthFragment > 0) {
			int tmp = 0;
			if (dna instanceof CircularDNA) {
				for (Object o : dna.getDNA()) {
					if (o instanceof Integer) {
						tmp += (int) o;
					} else if (((String) o).equals(enzymeName)) {
						break;
					}
				}
			}
			track[lengthFragment+tmp-1]++;
			if (dna instanceof CircularDNA) {
				if (tmp > 0) {
					track[tmp-1]--;
				}
			}
		}
	}
	
	private void setEnzymeTrack(int[] track, int[] fragmentSizes) {
		for (int fragmentSize : fragmentSizes) {
			track[fragmentSize-1]++;
		}
	}
	
	private void setAllEnzymesTrack(int[] track) {
		for (Object o : dna.getDNA()) {
			if (o instanceof Integer) {
				track[((int) o)-1]++;
			}
		}
	}
	
	private void setTracks() {
		int numTracks = enzymes.length;
		if (moreThanOneEnzyme()) {
			numTracks++;
		}
		tracks = new int[numTracks][scale[scale.length-1]];
		for (int i = 0; i < enzymes.length; i++) {
			if (enzymes[i].getFragmentSizes() == null) {
				setEnzymeTrack(tracks[i], enzymes[i].getName());
			} else {
				setEnzymeTrack(tracks[i], enzymes[i].getFragmentSizes());
			}
		}
		if (moreThanOneEnzyme()) {
			if (allEnzymesTrack == null) {
				setAllEnzymesTrack(tracks[tracks.length-1]);
			} else {
				setEnzymeTrack(tracks[tracks.length-1], allEnzymesTrack);
			}
		}
		tracks = Helper.transposeMatrix(tracks);
	}
	
	private void setFragments() {
		int[] numFragments = new int[tracks[0].length];
		for (int c = 0; c < tracks[0].length; c++) {
			for (int r = 0; r < tracks.length; r++) {
				numFragments[c] += tracks[r][c];
			}
		}
		for (int i = 0; i < enzymes.length; i++) {
			enzymes[i].setNumFragments(numFragments[i]);
		}
		totalFragments = numFragments[numFragments.length-1];
	}
	
	private void clearObject() {
		tracks = null;
		scale = null;
		totalFragments = 0;
		enzymes = null;
		allEnzymesTrack = null;

	}
	
	public String toString() {
		if (!dna.isValid()) {
			return "Gel Electrophoresis: Invalid";
		}
		String res = "Gel Electrophoresis: \n";
		int max = "Scale".length();
		
		String allEnzymes = null;
		if (moreThanOneEnzyme()) {
			allEnzymes = enzymes[0].getName();
			for (int i = 1; i < enzymes.length; i++) {
				allEnzymes += " + " + enzymes[i].getName();
			}
			if (allEnzymes.length() > "Scale".length()) {
				max = allEnzymes.length();
			}
		}
				
		String[] row = new String[moreThanOneEnzyme() ? enzymes.length + 2 : enzymes.length + 1];
		for (int i = 0; i < enzymes.length; i++) {
			row[i] = enzymes[i].getName();
		}
		if (moreThanOneEnzyme()) {
			row[row.length - 2] = allEnzymes;
		}
		row[row.length - 1] = "Scale";
		
		String format = "|";
		for (int i = 0; i < row.length; i++) {
			format += "%-" + max + "s|";
		}
		
		String line = "-";
		for (int i = 0; i < row.length * (max + 1); i++) {
			line += "-";
		}
		
		res += line + "\n" + String.format(format, (Object[]) row) + "\n" + line + "\n";
		for (int r = 0; r < tracks.length; r++) {
			for (int c = 0; c < tracks[r].length; c++) {
				row[c] = tracks[r][c] == 0 ? " " : String.valueOf(tracks[r][c]);
			}
			row[row.length - 1] = String.valueOf(scale[r]);
			res += String.format(format, (Object[]) row) + "\n";
		}
		
		res += line + "\n\tTotal Fragments: " + totalFragments;
		return res;
	}
}