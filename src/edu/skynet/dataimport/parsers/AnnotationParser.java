package edu.skynet.dataimport.parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.skynet.dataimport.Annotation;

public class AnnotationParser {

	int sampleIndexColumn;
	int dataColumn;
	File file;
	String delimeter;

	public AnnotationParser(String path, String delimeter, int sampleIndexColumn, int dataColumn) throws FileNotFoundException {
		this.file = new File(path);
		this.delimeter = delimeter;
		this.sampleIndexColumn = sampleIndexColumn;
		this.dataColumn = dataColumn;
	}

	/**
	 * Iterates through and parses all samples in the stream
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public List<Annotation> parse() throws FileNotFoundException {

		List<Annotation> samples = new ArrayList<Annotation>();

		Scanner scanner = new Scanner(file);
		while (scanner.hasNext()) {

			String line = scanner.nextLine();
			samples.add(parseLine(line));
		}
		return samples;
	}

	private Annotation parseLine(String line) {

		String[] parts = line.trim().split(delimeter);

		return new Annotation(Integer.parseInt(parts[sampleIndexColumn]), parts[dataColumn]);
	}
}
