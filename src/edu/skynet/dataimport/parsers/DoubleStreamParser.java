package edu.skynet.dataimport.parsers;

import java.io.FileNotFoundException;

public class DoubleStreamParser extends TextStreamParser<Double> {

	public DoubleStreamParser(String file, String delimeter) throws FileNotFoundException {
		super(file, delimeter);
	}

	@Override
	protected Double parseLine(String line) {
		return Double.parseDouble(line);
	}

}
