package edu.skynet.dataimport;

/**
 * A single sample of the time-based data that contains data in a double format
 * 
 */
public class Sample {

	public Double data;
	public int sampleIndex;

	public Sample(Double data, int sampleIndex) {
		this.data = data;
		this.sampleIndex = sampleIndex;
	}

	@Override
	public String toString() {
		return "[" + data.toString() + " @ " + sampleIndex + "]";
	}
}
