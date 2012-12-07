package edu.skynet.dataimport;

/**
 * A text note describing a range of samples
 * 
 */
public class Annotation {

	// the first sample this annotation describes
	public int startIndex;
	// the last sample this annotation describes
	public int endIndex;
	public String label;

	public Annotation(int startIndex, String label) {
		this.startIndex = startIndex;
		this.label = label;
	}

	public Annotation(int startIndex, int endIndex, String label) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.label = label;
	}

	@Override
	public String toString() {
		return label + " @ [" + startIndex + ", " + endIndex + "]";
	}

}
