package edu.skynet.featureextraction.examples;

import java.util.LinkedList;
import java.util.List;

import edu.skynet.dataimport.Datastream;
import edu.skynet.dataimport.Sample;
import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

public class AfibExtractor extends FeatureExtractor {

	@Override
	public Dataset extract(Datastream<?> stream) {
		List<Sample<Double>> R = getRValues((Datastream<Double>) stream);

		Dataset dataset = new Dataset();

		for (Sample<Double> r : R) {
			Instance i = new Instance();
			i.addAttribute("sampleIndex", r.sampleIndex + stream.getSampleOffset());
			i.addAttribute("R", r.data);
			dataset.addInstance(i);
		}

		return dataset;
	}

	@Override
	public Dataset extract(List<Datastream<?>> streams) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Sample<Double>> getRValues(Datastream<Double> stream) {

		LinkedList<Sample<Double>> R = new LinkedList<Sample<Double>>();

		// find the single highest point in the first 480 points (2 sec)
		// this point must be a R value
		int maxVoltageIndex = findLocalMax(stream, 0, 2 * stream.getSampleRate());
		Double maxVoltage = stream.getSample(maxVoltageIndex);
		R.add(new Sample<Double>(maxVoltage, maxVoltageIndex));

		int minOffset = (int) (stream.getSampleRate() / 4d);

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
	 * @param initalRVoltage The inital R value voltage that we're starting from
	 * @param minROffset The minimum distance between R peaks
	 * @param startIndex The signal index where we're starting
	 * @param signalStep How fast to move along the signal each step
	 * @return
	 */
	private LinkedList<Sample<Double>> scanRValues(Datastream<Double> stream, Double initalRVoltage, int minROffset, int startIndex, int signalStep) {

		LinkedList<Sample<Double>> R = new LinkedList<Sample<Double>>();
		final Double MAX_R_CHANGE = 0.8; // adjacent peaks must be within 20% of each other

		int currentIndex = startIndex;
		Double RVoltage = initalRVoltage; // voltage of last seen R value

		while (currentIndex >= 0) {

			int nextPeak = findPeakMax(stream, currentIndex, signalStep, RVoltage * MAX_R_CHANGE);
			if (nextPeak == -1) {
				break;
			}
			R.add(new Sample<Double>(stream.getSample(nextPeak), nextPeak));
			currentIndex = nextPeak + minROffset;
			RVoltage = stream.getSample(nextPeak);

		}

		return R;
	}
}
