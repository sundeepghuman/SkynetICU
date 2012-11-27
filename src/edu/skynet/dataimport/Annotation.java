package edu.skynet.dataimport;

public class Annotation {

	public int startIndex;
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
