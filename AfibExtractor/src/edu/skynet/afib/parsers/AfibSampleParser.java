package edu.skynet.afib.parsers;

import java.io.FileNotFoundException;

import edu.skynet.dataimport.parsers.SampleParser;

public class AfibSampleParser extends SampleParser {

	public AfibSampleParser(String data) throws FileNotFoundException {
		super(data, "\n", "\\s+", 1);
	}
}
