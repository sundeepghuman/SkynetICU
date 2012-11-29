package edu.skynet.hadoop;

import java.io.IOException;

import org.apache.hadoop.mapreduce.InputSplit;

public class ExtractInputSplit extends InputSplit {

	private String rawData;
	private String filename;

	public ExtractInputSplit(String rawData, String filename) {
		super();
		this.rawData = rawData;
		this.filename = filename;
	}

	@Override
	public long getLength() throws IOException, InterruptedException {
		return rawData.length() * Character.SIZE;
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {

		return new String[] {};
	}

	public String getRawData() {
		return rawData;
	}

	public String getFilename() {
		return filename;
	}

}
