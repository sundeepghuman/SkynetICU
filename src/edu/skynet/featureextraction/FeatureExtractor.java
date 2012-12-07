package edu.skynet.featureextraction;

import java.util.ArrayList;
import java.util.List;

import edu.skynet.dataimport.Annotation;
import edu.skynet.dataimport.Datastream;
import edu.skynet.dataimport.Sample;
import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

/**
 * Extracts features from a stream of data
 * 
 */
public abstract class FeatureExtractor {

	// the min/max amount of samples allowed in a slice of data
	private int minSlice;
	private int maxSlice;

	// The name of the attribute in an exported .Arff file for the annotation attribute
	public final static String LABEL_ATTRIBUTE_NAME = "label";

	public FeatureExtractor() {
		minSlice = -1;
		maxSlice = -1;
	}

	/**
	 * Set the minimum number of samples that make up a single extracted instance
	 */
	public void setMinSamplesPerSlice(int numSamples) {
		this.minSlice = numSamples;
	}

	/**
	 * Set the maximum number of samples that make up a single extracted instance
	 */
	public void setMaxSamplesPerSlice(int numSamples) {
		this.maxSlice = numSamples;
	}

	/**
	 * Extract the data from the stream and return it in a Dataset
	 * 
	 * @param streams The single stream of data to extract features from
	 * @return
	 */
	public final Dataset extract(Datastream stream) {

		List<DataSlice> slices = sliceData(stream);
		Dataset dataset = new Dataset();

		for (DataSlice slice : slices) {
			Instance instance = extract(slice.data, stream.getSampleRate());
			instance.addAttribute("time", slice.startIndex);
			instance.addAttribute(LABEL_ATTRIBUTE_NAME, slice.label);
			dataset.addInstance(instance);
		}

		return dataset;
	}

	/**
	 * Slice up data based on annotations and the extractor's slice limits into uniform data slices
	 * 
	 * @param stream
	 * @return
	 */
	private List<DataSlice> sliceData(Datastream stream) {

		List<DataSlice> slices = new ArrayList<DataSlice>();

		for (Annotation annotation : stream.getAnnotations()) {

			int numSamples = annotation.endIndex - annotation.startIndex;
			double numSlices = 1;

			// figure out how to slice up data to meet slicing limits
			// 4 different cases
			if (minSlice == -1) {
				if (maxSlice == -1) {
					// 1. no limits, so just don't slice
					numSlices = 1;
				} else {
					// 2. max limit only
					numSlices = Math.ceil(numSamples / maxSlice);

				}
			} else {
				if (maxSlice == -1) {
					// 3. min limit only
					numSlices = Math.floor(numSamples / minSlice);

				} else {
					// 4. both limits
					numSlices = Math.ceil(numSamples / maxSlice);

					if (numSamples / numSlices < minSlice) {
						// no possible way to slice within limits
						throw new RuntimeException("Unable to slice data within min and max slicing limits.");
					}
				}
			}

			double samplesPerSlice = numSamples / numSlices;

			// slice up data according to parameters
			// use index variable to keep from from having extra samples left over if ints were used
			double index = annotation.startIndex;
			int start, end;
			start = annotation.startIndex;

			for (int sliceNumber = 0; sliceNumber < numSlices; sliceNumber++) {

				index += samplesPerSlice;
				end = (int) Math.floor(index);

				DataSlice slice = new DataSlice();
				slice.data = stream.getSamples(start, end);
				slice.label = annotation.label;
				slice.startIndex = start;

				slices.add(slice);

				start = end + 1;
			}
		}

		return slices;
	}

	/**
	 * Process the given data and extract attributes from it
	 * 
	 * @param data The data to be processed
	 * @param sampleRate The sample rate of this data in samples/second
	 * @return
	 */
	public abstract Instance extract(Double[] data, int sampleRate);

	/**
	 * Find the largest sample in the specified range of samples
	 * 
	 * @param stream Stream of data being analyzed
	 * @param first The start index of the stream
	 * @param last The end index of the stream
	 * @return
	 */
	protected static int findLocalMax(Double[] stream, int first, int last) {

		int maxIndex = -1;
		Double max = null;
		for (int x = first; x < last; x++) {
			Double current = stream[x];
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
	protected static int findLocalMin(Double[] stream, int first, int last) {

		int minIndex = -1;
		Double min = null;
		for (int x = first; x < last; x++) {
			Double current = stream[x];
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
	protected static int findLocalMaxAboveThreshold(Double[] stream, int first, int last, Double threshold) {

		int maxIndex = -1;
		Double max = null;
		for (int x = first; x < last; x++) {
			Double current = stream[x];
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
	protected static int findPeakMax(Double[] stream, int start, int sampleOffset, Double threshold) {

		int maxIndex = -1;
		Double max = null;
		boolean inPeak = false;

		int i = start;
		while (i >= 0 && i < stream.length) {
			Double current = stream[i];

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

	/**
	 * Gets the averaged rate of occurrence of the given samples per minute
	 * 
	 * @param samples The samples to find the rate of occurrence
	 * @return
	 */
	protected static double calculateAverageRatePerMinute(int sampleRate, List<Sample> samples) {

		if (samples == null || samples.size() == 0) {
			return 0;
		}

		// calculate average rate by finding ((end time - start time) / # samples)
		int firstIndex = Integer.MAX_VALUE;
		int lastIndex = -1;

		for (Sample s : samples) {
			if (s.sampleIndex > lastIndex) {
				lastIndex = s.sampleIndex;
			}
			if (s.sampleIndex < firstIndex) {
				firstIndex = s.sampleIndex;
			}

		}

		// get rate in occurrences / samples
		double rate = (double) samples.size() / (double) (lastIndex - firstIndex);

		// convert to occurrences / min
		rate *= sampleRate * 60;

		return rate;
	}

	/**
	 * Calculate the mean value of the sample data
	 * 
	 * @param samples Sample set to calculate on
	 * @return
	 */
	protected static double calculateAverageSampleValue(List<Sample> samples) {
		double sum = 0;

		for (Sample s : samples) {
			sum += s.data;
		}

		return sum / samples.size();
	}

	/**
	 * Calculate the mean value of the sample data
	 * 
	 * @param samples Sample values to calculate on
	 * @return
	 */
	protected static double calculateAverageSampleValue(Double[] samples) {
		double sum = 0;

		for (double sample : samples) {
			sum += sample;
		}

		return sum / samples.length;
	}

	/**
	 * Calculate the population standard deviation of the sample data
	 * 
	 * @param samples Sample set to calculate on
	 * @return
	 */
	protected static double calculateStandardDeviation(List<Sample> samples) {
		double mean = calculateAverageSampleValue(samples);
		double sum = 0;

		for (Sample s : samples) {
			sum += Math.pow(s.data - mean, 2);
		}

		return Math.sqrt(sum / samples.size());

	}

	/**
	 * Calculate the population standard deviation of the sample data
	 * 
	 * @param samples Sample set to calculate on
	 * @return
	 */
	protected static double calculateStandardDeviation(Double[] samples) {
		double mean = calculateAverageSampleValue(samples);
		double sum = 0;

		for (double sample : samples) {
			sum += Math.pow(sample - mean, 2);
		}

		return Math.sqrt(sum / samples.length);

	}
}
