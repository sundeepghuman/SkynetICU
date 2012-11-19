package edu.skynet.dataimport;

import java.io.FileNotFoundException;
import java.util.List;

import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.TextStreamParser;

/**
 * A generic stream of data imported from file
 * 
 * @param <T> The type of data that makes up the samples of the stream
 */
public class Datastream<T> {

	int sampleRate;
	List<T> samples;
	List<Annotation> annotations;
	long sampleOffset;

	public Datastream(int sampleRate, TextStreamParser<T> parser, AnnotationParser annotationParser) throws FileNotFoundException {
		this.sampleRate = sampleRate;
		samples = parser.parse();
		this.annotations = annotationParser.parse();
	}

	public Datastream(int sampleRate, TextStreamParser<T> parser, AnnotationParser annotationParser, long sampleOffset) throws FileNotFoundException {
		this(sampleRate, parser, annotationParser);
		this.sampleOffset = sampleOffset;
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

	public int getSampleRate() {
		return sampleRate;
	}

	public int getLength() {
		return samples.size();
	}

	public long getSampleOffset() {
		return sampleOffset;
	}
}
