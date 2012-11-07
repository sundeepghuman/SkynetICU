package edu.skynet.dataimport;

public class Sample<T> {

	public T data;
	public int sampleIndex;

	public Sample(T data, int sampleIndex) {
		this.data = data;
		this.sampleIndex = sampleIndex;
	}

	@Override
	public String toString() {
		return "[" + data.toString() + " @ " + sampleIndex + "]";
	}
}
