package edu.skynet.dataimport.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.skynet.dataimport.Annotation;

public class AnnotationParser {

	private int startSampleIndexColumn;
	private int dataColumn;
	private File file;
	private String delimeter;

	/**
	 * 
	 * @param path Path to the annotation file
	 * @param delimeter The delimeter between columns in a single annotation line
	 * @param startSampleIndexColumn The column index of the annotation's start index
	 * @param dataColumn The column index of the annotation's label
	 * @throws FileNotFoundException
	 */
	public AnnotationParser(String path, String delimeter, int startSampleIndexColumn, int dataColumn) throws FileNotFoundException {
		this.file = new File(path);
		this.delimeter = delimeter;
		this.startSampleIndexColumn = startSampleIndexColumn;
		this.dataColumn = dataColumn;
	}

	/**
	 * Iterates through and parses all samples in the stream
	 * 
	 * @param maxSamples The number of samples in the stream. Used to set the final annotation's end
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Annotation> parse(int maxSamples) throws FileNotFoundException {

		List<Annotation> samples = new ArrayList<Annotation>();

		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {

			String line = scanner.nextLine();
			samples.add(parseLine(line));
		}

		// go through each sample and set the end time to be right before the next annotation
		for (int x = 0; x < samples.size() - 1; x++) {
			Annotation thisAnnotation = samples.get(x);
			Annotation nextAnnotation = samples.get(x + 1);

			thisAnnotation.endIndex = nextAnnotation.startIndex - 1;

		}

		samples.get(samples.size() - 1).endIndex = maxSamples - 1;

		return samples;
	}

	private Annotation parseLine(String line) {

		String[] parts = line.trim().split(delimeter);

		return new Annotation(Integer.parseInt(parts[startSampleIndexColumn]), parts[dataColumn]);
	}
}
