package edu.skynet.afib.export;

import java.util.List;

import edu.skynet.dataexport.ArffExporter;
import edu.skynet.ml.Dataset;

public class AfibDataExporter extends ArffExporter {

	@Override
	public String export(List<Dataset> dataSets) {

		// doesn't really extend ArffExporter, but exists to demonstrate how to
		return super.export(dataSets);
	}

}
