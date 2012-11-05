package edu.skynet.featureextraction;

import java.util.List;

import edu.skynet.ml.Dataset;

public interface FeatureExtractor<T> {

	public Dataset extract(List<T> stream, int sampleRate, String label);

	public T parse(String value);

}
