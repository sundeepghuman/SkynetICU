package edu.skynet.dataimport.parsers;

import java.io.FileNotFoundException;

public class DoubleStreamParser extends TextStreamParser<Double> {

	int dataColumn;
	String columnDelimeter;

	/**
	 * @param file The file containing the samples
	 * @param delimeter The delimieter between columns in the data
	 * @param dataColumn The column containing the sample data
	 * @throws FileNotFoundException
	 */
	public DoubleStreamParser(String file, String columnDelimeter, int dataColumn) throws FileNotFoundException {
		super(file, "\n");
		this.dataColumn = dataColumn;
		this.columnDelimeter = columnDelimeter;
	}

	@Override
	protected Double parseLine(String line) {
		String[] parts = line.split(columnDelimeter);
		return Double.parseDouble(parts[dataColumn]);
	}

}
