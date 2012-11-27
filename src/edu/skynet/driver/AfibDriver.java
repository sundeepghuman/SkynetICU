package edu.skynet.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.skynet.dataexport.ArffExporter;
import edu.skynet.dataimport.Datastream;
import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.DoubleStreamParser;
import edu.skynet.featureextraction.examples.AfibExtractor;
import edu.skynet.ml.Dataset;

public class AfibDriver {

	public static void main(String[] args) throws IOException {

		final int sampleRate = 250;

		List<Dataset> datasets = new ArrayList<Dataset>();

		DoubleStreamParser dataParser = new DoubleStreamParser("test-afib-data/04043.txt", "\\s+", 1);
		AnnotationParser annotationParser = new AnnotationParser("test-afib-data/04043_annotations.txt", "\\s+", 1, 6);

		Datastream<Double> ecgStream = new Datastream<>(sampleRate, dataParser, annotationParser);
		AfibExtractor extractor = new AfibExtractor();
		extractor.setMaxSamplesPerSlice(5000);
		extractor.setMinSamplesPerSlice(2500);

		datasets.add(extractor.extract(ecgStream));

		ArffExporter exporter = new ArffExporter();
		exporter.export(args[0], "relation-name", datasets);

		System.out.println("Finished");
	}
}
