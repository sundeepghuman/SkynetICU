package edu.skynet.afib;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

public class Heartbeats implements FeatureExtractor<Float> {

	private Float[] voltages;
	private float sampleRate;

	private List<Point> findQValues(List<Point> R, List<Point> P) {
		// Q - smallest between P and R
		LinkedList<Point> Q = new LinkedList<Point>();

		for (int x = 1; x < R.size(); x++) {
			Point r = R.get(x);
			Point p = P.get(x - 1);

			// get smallest value between the 2 r values
			float min = Float.MAX_VALUE;
			int minTime = -1;

			for (int i = p.time; i < r.time; i++) {
				float current = voltages[i];
				if (current < min) {
					min = current;
					minTime = i;
				}
			}

			Q.addLast(new Point(min, minTime));
		}

		return Q;

	}

	private List<Point> findPValues(List<Point> R) {
		// T - largest values between R and halfway to the next R

		LinkedList<Point> P = new LinkedList<Point>();

		for (int x = 0; x < R.size() - 1; x++) {
			Point r0 = R.get(x);
			Point r1 = R.get(x + 1);

			// get smallest value between the 2 r values
			float max = -Float.MAX_VALUE;
			int maxTime = -1;
			int start = r0.time + (int) (((float) (r1.time - r0.time)) * (3f / 4f));
			for (int i = start; i < r1.time - (int) (sampleRate / 10); i++) {
				float current = voltages[i];
				if (current > max) {
					max = current;
					maxTime = i;
				}
			}

			P.addLast(new Point(max, maxTime));
		}

		return P;
	}

	/**
	 * Finds the T values of the signal
	 * 
	 * @param R
	 * @return
	 */
	private List<Point> findTValues(List<Point> R) {

		// T - largest values between R and halfway to the next R

		LinkedList<Point> T = new LinkedList<Point>();

		for (int x = 0; x < R.size() - 1; x++) {
			Point r0 = R.get(x);
			Point r1 = R.get(x + 1);

			float max = -Float.MAX_VALUE;
			int maxTime = -1;
			for (int i = r0.time + (int) (sampleRate / 10); i < ((r1.time - r0.time) / 2f) + r0.time; i++) {
				float current = voltages[i];
				if (current > max) {
					max = current;
					maxTime = i;
				}
			}

			T.addLast(new Point(max, maxTime));
		}

		return T;
	}

	/**
	 * Find the S values in the signal
	 * 
	 * @param R The R values in the signal
	 * @return
	 */
	private List<Point> findSValues(List<Point> R) {

		// S - smallest voltage between 2 R values
		LinkedList<Point> S = new LinkedList<Point>();

		for (int x = 0; x < R.size() - 1; x++) {
			Point r0 = R.get(x);
			Point r1 = R.get(x + 1);

			// get smallest value between the 2 r values
			float min = Float.MAX_VALUE;
			int minTime = -1;
			for (int i = r0.time; i < r0.time + (int) ((1f / 4f) * (r1.time - r0.time)); i++) {
				float current = voltages[i];
				if (current < min) {
					min = current;
					minTime = i;
				}
			}

			S.addLast(new Point(min, minTime));
		}

		return S;
	}

	/**
	 * Find the time and voltage value of each R peak in the signal
	 * 
	 * @return
	 */
	private List<Point> findRValues() {

		LinkedList<Point> R = new LinkedList<Point>();

		// find the single highest point in the first 480 points (2 sec)
		// this point must be a R value
		float maxVoltage = Float.MIN_VALUE;
		int maxIndex = -1;
		for (int x = 0; x < (int) sampleRate * 2; x++) {
			if (voltages[x] > maxVoltage) {
				maxVoltage = voltages[x];
				maxIndex = x;
			}
		}
		R.add(new Point(maxVoltage, maxIndex));

		// find all R values from here back to the beginning of the signal
		for (Point r : scanRValues(maxVoltage, (int) (-sampleRate / 2f), maxIndex - (int) (sampleRate / 2f), -1)) {
			R.addFirst(r);
		}

		// now find all the R valuse from the inital R to the end of the signal
		for (Point r : scanRValues(maxVoltage, (int) (sampleRate / 2f), maxIndex + (int) (sampleRate / 2f), 1)) {
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
	private LinkedList<Point> scanRValues(float initalRVoltage, int minROffset, int startIndex, int signalStep) {

		LinkedList<Point> R = new LinkedList<Point>();

		int currentIndex = startIndex;
		float RVoltage = initalRVoltage; // voltage of last seen R value

		boolean inRPeak = false;
		float maxSeen = Float.MIN_VALUE;
		int maxSeenIndex = -1;

		while (currentIndex >= 0 && currentIndex < voltages.length) {

			float current = voltages[currentIndex];

			// is this voltage higher than the previous best
			if (current > maxSeen) {
				maxSeen = current;
				maxSeenIndex = currentIndex;
			}

			// if in the R peak -> 80% of last R value
			if ((float) Math.abs(current - RVoltage) / (float) RVoltage < .2) {
				inRPeak = true;
			}
			// if leaving the R peak
			else if (inRPeak) {
				// R must be the best thing we've seen so far
				R.addLast(new Point(maxSeen, maxSeenIndex));

				// skip values because another R can't appear so soon
				// if an R value appeared before 100 checks (100/240s), the
				// heartrate is <= 30 bpm
				currentIndex += minROffset;

				RVoltage = maxSeen;
				maxSeen = Integer.MIN_VALUE;
				maxSeenIndex = -1;
				inRPeak = false;
			}
			currentIndex += signalStep;
		}

		return R;
	}

	@Override
	public Dataset extract(List<Float> stream, int sampleRate, String label) {
		voltages = new Float[stream.size()];
		stream.toArray(voltages);

		this.sampleRate = (float) sampleRate;

		List<Point> R = findRValues();
		List<Point> S = findSValues(R);
		List<Point> T = findTValues(R);
		List<Point> P = findPValues(R);
		List<Point> Q = findQValues(R, P);

		ArrayList<Heartbeat> heartbeats = new ArrayList<Heartbeat>();

		for (int x = 0; x < P.size(); x++) {
			float hr = (float) (R.get(x + 1).time - R.get(x).time) / sampleRate * 60;
			heartbeats.add(new Heartbeat(P.get(x), Q.get(x), R.get(x + 1), S.get(x), T.get(x), hr));
		}

		// Schema heartbeatSchema = new Schema("heartrate", "R", "S", "T", "P", "Q");

		Dataset set = new Dataset();

		boolean isAfib = Boolean.parseBoolean(label);
		for (Heartbeat hb : heartbeats) {
			Instance i = new Instance();
			i.addAttribute("heartrate", hb.heartrate);
			i.addAttribute("Q", hb.q.voltage);
			i.addAttribute("R", hb.r.voltage);
			i.addAttribute("T", hb.t.voltage);
			i.addAttribute("S", hb.s.voltage);
			i.addAttribute("P", hb.p.voltage);
			i.addAttribute("afib", isAfib);
			set.addInstance(i);
		}

		return set;
	}

	@Override
	public Float parse(String value) {
		return Float.parseFloat(value);
	}
}
