package edu.skynet.dataimport.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A generic data stream parser
 * 
 */
public class SampleParser {

	File file;
	String sampleDelimeter;
	String columnDelimeter;
	long sampleOffset;
	int dataColumnIndex;

	/**
	 * @param path The file location that makes up the stream
	 * @param delimeter The delimeter that separates samples
	 * @throws FileNotFoundException
	 */
	public SampleParser(String path, String sampleDelimeter, String columnDelimeter, int dataColumnIndex) throws FileNotFoundException {
		this.file = new File(path);
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

		Scanner scanner = new Scanner(file).useDelimiter(sampleDelimeter);
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
