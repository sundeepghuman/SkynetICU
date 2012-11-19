package edu.skynet.dataimport.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A generic data stream parser
 * 
 * @param <T> The type of data that makes up the samples of the stream
 */
public abstract class TextStreamParser<T> {

	File file;
	String delimeter;
	long sampleOffset;

	/**
	 * @param path The file location that makes up the stream
	 * @param delimeter The delimeter that separates samples
	 * @throws FileNotFoundException
	 */
	public TextStreamParser(String path, String delimeter) throws FileNotFoundException {
		this.file = new File(path);
		this.delimeter = delimeter;
	}

	/**
	 * Iterates through and parses all samples in the stream
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<T> parse() throws FileNotFoundException {

		List<T> samples = new ArrayList<T>();

		Scanner scanner = new Scanner(file).useDelimiter(delimeter);
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
	protected abstract T parseLine(String line);

	protected long getSampleOffset() {
		return sampleOffset;
	}

	public void setSampleOffset(long sampleOffset) {
		this.sampleOffset = sampleOffset;
	}
}
