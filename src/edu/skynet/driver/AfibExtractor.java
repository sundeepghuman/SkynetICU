package edu.skynet.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.skynet.afib.Heartbeats;
import edu.skynet.dataexport.ArffExporter;
import edu.skynet.dataimport.TimeSeriesImporter;
import edu.skynet.ml.Dataset;

public class AfibExtractor {

	public static void main(String[] args) throws IOException {

		List<Dataset> datasets = new ArrayList<Dataset>();
		TimeSeriesImporter<Float> importer = new TimeSeriesImporter<Float>();
		datasets.add(importer.extractInstances("test-afib-data/04746.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/04936.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/05091.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/08434.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/08405.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/08738.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/08219.txt", new Heartbeats(), 240));
		datasets.add(importer.extractInstances("test-afib-data/07162.txt", new Heartbeats(), 240));

		ArffExporter exporter = new ArffExporter();
		exporter.export(args[0], "ECG", datasets);
	}
}
