package edu.skynet.dataimport.parsers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Parses a given stream of samples
 * 
 */
public class SampleParser {

	String data;
	String sampleDelimeter;
	String columnDelimeter;
	// the offset of these samples from the beginning of a longer stream
	long sampleOffset;
	int dataColumnIndex;

	/**
	 * @param data The actual data that will needs to be parsed
	 * @param delimeter The delimeter that separates samples from each other
	 * @param columnDelimeter The delimiter that separates columns in a single sample from each other
	 * @param dataColumnIndex The index of which column of data contains the sampes to be parsed (zero-indexed)
	 * 
	 * @throws FileNotFoundException
	 */
	public SampleParser(String data, String sampleDelimeter, String columnDelimeter, int dataColumnIndex) throws FileNotFoundException {
		this.data = data;
		this.sampleDelimeter = sampleDelimeter;
		this.columnDelimeter = columnDelimeter;
		this.dataColumnIndex = dataColumnIndex;
	}

	/**
	 * Iterates through and parses all samples in the stream
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Double> parse() throws FileNotFoundException {

		List<Double> samples = new ArrayList<Double>();

		Scanner scanner = new Scanner(data).useDelimiter(sampleDelimeter);
		while (scanner.hasNext()) {

			String line = scanner.nextLine();
			samples.add(parseLine(line));
		}
		return samples;
	}

	/**
	 * Parses a single sample line
	 * 
	 * @param line The line of text that makes up 1 sample
	 * @return
	 */
	protected Double parseLine(String line) {
		String[] parts = line.split(columnDelimeter);
		return Double.parseDouble(parts[dataColumnIndex]);
	}

	protected long getSampleOffset() {
		return sampleOffset;
	}

	public void setSampleOffset(long sampleOffset) {
		this.sampleOffset = sampleOffset;
	}
}
