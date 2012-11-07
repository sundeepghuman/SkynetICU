package edu.skynet.dataimport.parsers;

import java.io.FileNotFoundException;

public class DoubleCsvStreamParser extends CsvStreamParser<Double> {

	public DoubleCsvStreamParser(String file, int dataColumn) throws FileNotFoundException {
		super(file, dataColumn);
	}

	@Override
	protected Double parseLine(String line) {
		String[] parts = line.split(",");
		return Double.parseDouble(parts[dataColumn]);
	}

}
