package edu.skynet.dataimport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.SampleParser;

/**
 * A stream of data imported from file
 * 
 */
public class Datastream {

	private int sampleRate;
	private List<Double> samples;
	private List<Annotation> annotations;
	private int sampleOffset;

	/**
	 * Constructs the datastream by running the samples and annotations through their respective parsers
	 * 
	 * @param sampleRate The number of samples per second of this data stream
	 * @param parser The SampleParser that will parse the samples
	 * @param annotationParser The AnnotationParse that will parse the annotations
	 * @throws FileNotFoundException
	 */
	public Datastream(int sampleRate, SampleParser parser, AnnotationParser annotationParser) throws FileNotFoundException {
		this.sampleRate = sampleRate;
		samples = parser.parse();
		this.annotations = annotationParser.parse(samples.size());
	}

	public Datastream(int sampleRate, SampleParser parser, AnnotationParser annotationParser, int sampleOffset) throws FileNotFoundException {
		this(sampleRate, parser, annotationParser);
		this.sampleOffset = sampleOffset;
	}

	/**
	 * Gets the wall clock time of a sample from the sample index and sampling rate
	 * 
	 * @param base The time at sample 0
	 * @param sampleIndex Which sample is being converted
	 * @return
	 */
	public Date getSampleTime(Date base, int sampleIndex) {
		int numSeconds = sampleIndex / sampleRate;

		Calendar cal = Calendar.getInstance();
		cal.setTime(base);
		cal.add(Calendar.SECOND, numSeconds);
		return cal.getTime();
	}

	/**
	 * Get a sample in the stream located at an index
	 * 
	 * @param sampleIndex The desired sample index
	 * @return
	 */
	public Double getSample(int sampleIndex) {
		return samples.get(sampleIndex);
	}

	/**
	 * Gets the data from the start to end index inclusive. Allocates data in a new array
	 * 
	 * @return
	 */
	public Double[] getSamples(int startIndex, int endIndex) {
		int length = endIndex - startIndex + 1;
		List<Double> data = new ArrayList<Double>(length);

		for (int x = startIndex; x <= endIndex; x++) {
			data.add(samples.get(x));
		}

		Double[] dataArray = new Double[length];
		return data.toArray(dataArray);
	}

	public int getSampleRate() {
		return sampleRate;
	}

	public int getLength() {
		return samples.size();
	}

	public int getSampleOffset() {
		return sampleOffset;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}
}
