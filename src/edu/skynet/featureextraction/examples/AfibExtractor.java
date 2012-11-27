package edu.skynet.featureextraction.examples;

import java.util.LinkedList;
import java.util.List;

import edu.skynet.dataimport.Sample;
import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Instance;

public class AfibExtractor extends FeatureExtractor<Double> {

	@Override
	public Instance extract(Double[] data, int sampleRate) {

		List<Sample<Double>> R = getRValues(data, sampleRate);

		Instance instance = new Instance();
		instance.addAttribute("r-avg", calculateAverageSampleValue(R));
		instance.addAttribute("heartrate", calculateAverageRatePerMinute(sampleRate, R));
		instance.addAttribute("r-dev", calculateStandardDeviation(R));

		return instance;

	}

	private List<Sample<Double>> getRValues(Double[] stream, int sampleRate) {

		LinkedList<Sample<Double>> R = new LinkedList<Sample<Double>>();

		// find the single highest point in the first 2 sec
		// this point must be a R value
		int maxVoltageIndex = findLocalMax(stream, 0, 2 * sampleRate);
		Double maxVoltage = stream[maxVoltageIndex];
		R.add(new Sample<Double>(maxVoltage, maxVoltageIndex));

		int minOffset = (int) (sampleRate / 4d);

		// find all R values from here back to the beginning of the signal
		for (Sample<Double> r : scanRValues(stream, maxVoltage, -minOffset, maxVoltageIndex - minOffset, -1)) {
			R.addFirst(r);
		}

		// now find all the R valuse from the inital R to the end of the signal
		for (Sample<Double> r : scanRValues(stream, maxVoltage, minOffset, maxVoltageIndex + minOffset, 1)) {
			R.addLast(r);
		}

		return R;

	}

	/**
	 * Scan across the signal looking for R values
	 * 
	 * @param initalRVoltage The initial R value voltage that we're starting from
	 * @param minROffset The minimum distance between R peaks
	 * @param startIndex The signal index where we're starting
	 * @param signalStep How fast to move along the signal each step
	 * @return
	 */
	private LinkedList<Sample<Double>> scanRValues(Double[] stream, Double initalRVoltage, int minROffset, int startIndex, int signalStep) {

		LinkedList<Sample<Double>> R = new LinkedList<Sample<Double>>();
		final Double MAX_R_CHANGE = 0.8; // adjacent peaks must be within 20% of each other

		int currentIndex = startIndex;
		Double RVoltage = initalRVoltage; // voltage of last seen R value

		while (currentIndex >= 0) {

			int nextPeak = findPeakMax(stream, currentIndex, signalStep, RVoltage * MAX_R_CHANGE);
			if (nextPeak == -1) {
				break;
			}
			R.add(new Sample<Double>(stream[nextPeak], nextPeak));
			currentIndex = nextPeak + minROffset;
			RVoltage = stream[nextPeak];

		}

		return R;
	}
}
