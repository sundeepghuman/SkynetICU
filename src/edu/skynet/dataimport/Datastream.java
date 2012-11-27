package edu.skynet.dataimport;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.TextStreamParser;

/**
 * A generic stream of data imported from file
 * 
 * @param <T> The type of data that makes up the samples of the stream
 */
public class Datastream<T> {

	private int sampleRate;
	private List<T> samples;
	private List<Annotation> annotations;
	private int sampleOffset;

	public Datastream(int sampleRate, TextStreamParser<T> parser, AnnotationParser annotationParser) throws FileNotFoundException {
		this.sampleRate = sampleRate;
		samples = parser.parse();
		this.annotations = annotationParser.parse(samples.size());
	}

	public Datastream(int sampleRate, TextStreamParser<T> parser, AnnotationParser annotationParser, int sampleOffset) throws FileNotFoundException {
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
	public T getSample(int sampleIndex) {
		return samples.get(sampleIndex);
	}

	/**
	 * Gets the data from the start to end index inclusive. Allocates data in a new array
	 * 
	 * @return
	 */
	public T[] getSamples(int startIndex, int endIndex) {
		int length = endIndex - startIndex + 1;
		List<T> data = new ArrayList<T>(length);

		for (int x = startIndex; x <= endIndex; x++) {
			data.add(samples.get(x));
		}

		@SuppressWarnings("unchecked")
		// TODO: fix this by removing all generics or changing arrays to lists
		T[] dataArray = (T[]) new Double[length];
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
