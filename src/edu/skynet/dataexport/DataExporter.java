package edu.skynet.dataexport;

import java.util.List;

import edu.skynet.ml.Dataset;

public interface DataExporter {

	public String export(List<Dataset> dataSets);
}
