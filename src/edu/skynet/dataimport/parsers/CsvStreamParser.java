package edu.skynet.dataimport.parsers;

import java.io.FileNotFoundException;

public abstract class CsvStreamParser<T> extends TextStreamParser<T> {

	protected int dataColumn;

	public CsvStreamParser(String file, int dataColumn) throws FileNotFoundException {
		this(file, "\n", dataColumn);
	}

	public CsvStreamParser(String file, String delimeter, int dataColumn) throws FileNotFoundException {
		super(file, delimeter);
		this.dataColumn = dataColumn;
	}
}
