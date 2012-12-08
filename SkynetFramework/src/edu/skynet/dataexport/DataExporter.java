package edu.skynet.dataexport;

import java.util.List;

import edu.skynet.ml.Dataset;

/**
 * Exports processed data into a text file format
 * 
 */
public interface DataExporter {

	/**
	 * Turns the extracted data into text to write to a file
	 * 
	 * @param dataSets The extracted data
	 */
	public String export(List<Dataset> dataSets);
}
