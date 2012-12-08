package edu.skynet.featureextraction;

/**
 * Represents a continuous range of samples that all have the same annotation
 * 
 */
public class DataSlice {

	public Double[] data;
	// Annotation of this slice
	public String label;
	// start index of the sample range
	public int startIndex;

}
