package edu.skynet.featureextraction;

import java.util.List;

import edu.skynet.dataimport.Datastream;
import edu.skynet.ml.Dataset;

/**
 * Extracts features from a stream(s) of data. Ex: extracting the heartrate from an ECG stream *
 * 
 * @param <T> The type of data that makes up the samples of the stream
 */
public abstract class FeatureExtractor {

	/**
	 * Extract the data from the stream and return it in a Dataset
	 * 
	 * @param streams The single stream of data to extract features from
	 * @return
	 */
	public abstract Dataset extract(Datastream<?> stream);

	public abstract Dataset extract(List<Datastream<?>> streams);

	/**
	 * Find the largest sample in the specified range of samples
	 * 
	 * @param stream Stream of data being analyzed
	 * @param first The start index of the stream
	 * @param last The end index of the stream
	 * @return
	 */
	protected static <S extends Comparable<? super S>> int findLocalMax(Datastream<S> stream, int first, int last) {

		int maxIndex = -1;
		S max = null;
		for (int x = first; x < last; x++) {
			S current = stream.getSample(x);
			if (maxIndex == -1 || current.compareTo(max) > 0) {
				max = current;
				maxIndex = x;
			}
		}

		return maxIndex;
	}

	/**
	 * Find the smallest sample in the specified range of samples
	 * 
	 * @param stream Stream of data being analyzed
	 * @param first The start index of the stream
	 * @param last The end index of the stream
	 * @return
	 */
	protected static <S extends Comparable<? super S>> int findLocalMin(Datastream<S> stream, int first, int last) {

		int minIndex = -1;
		S min = null;
		for (int x = first; x < last; x++) {
			S current = stream.getSample(x);
			if (minIndex == -1 || current.compareTo(min) < 0) {
				min = current;
				minIndex = x;
			}
		}

		return minIndex;
	}

	/**
	 * Find the largest sample in the specified range of samples that is over a certain threshold value
	 * 
	 * @param stream Stream of data being analyzed
	 * @param first The start index of the stream
	 * @param last The end index of the stream
	 * @param thresold The minimum allowed value to be considered a max value
	 * @return
	 */
	protected static <S extends Comparable<? super S>> int findLocalMaxAboveThreshold(Datastream<S> stream, int first, int last, S threshold) {

		int maxIndex = -1;
		S max = null;
		for (int x = first; x < last; x++) {
			S current = stream.getSample(x);
			if (maxIndex == -1 || (current.compareTo(max) > 0 && current.compareTo(threshold) > 0)) {
				max = current;
				maxIndex = x;
			}
		}

		return maxIndex;
	}

	/**
	 * Find the max of the next peak of samples that crosses the threshold
	 * 
	 * @param stream Data stream being analyzed
	 * @param start The sample to start searching from
	 * @param sampleOffset How many samples to move by each iteration
	 * @param threshold The minimum value to be considered a max
	 * @return
	 */
	protected static <S extends Comparable<? super S>> int findPeakMax(Datastream<S> stream, int start, int sampleOffset, S threshold) {

		int maxIndex = -1;
		S max = null;
		boolean inPeak = false;

		int i = start;
		while (i >= 0 && i < stream.getLength()) {
			S current = stream.getSample(i);

			if (current.compareTo(threshold) > 0) {
				inPeak = true; // we just entered the peak that crosses the threshold
				if (maxIndex == -1 || current.compareTo(max) > 0) {
					maxIndex = i;
					max = current;
				}
			} else if (inPeak) {
				// we just left the peak that crossed the threshold, so we're done searching
				break;
			}
			i += sampleOffset;
		}

		return maxIndex;
	}
}
