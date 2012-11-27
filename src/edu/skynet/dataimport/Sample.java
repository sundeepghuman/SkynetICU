package edu.skynet.dataimport;

/**
 * A single sample of the time-based data
 * 
 * @param <T> The type of data the sample measures
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
