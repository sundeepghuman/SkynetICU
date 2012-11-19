package edu.skynet.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.skynet.dataexport.ArffExporter;
import edu.skynet.dataimport.Datastream;
import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.DoubleCsvStreamParser;
import edu.skynet.dataimport.parsers.TextStreamParser;
import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.featureextraction.examples.AfibExtractor;
import edu.skynet.ml.Dataset;

public class AfibDriver {

	public static void main(String[] args) throws IOException {

		List<Dataset> datasets = new ArrayList<Dataset>();

		TextStreamParser<Double> dataParser = new DoubleCsvStreamParser("test-afib-data/04746.txt", 1);
		AnnotationParser annotationParser = new AnnotationParser("test-afib-data/04746_annotations.txt", "\\s+", 1, 6);

		Datastream<Double> ecgStream = new Datastream<>(250, dataParser, annotationParser, 10000);
		FeatureExtractor extractor = new AfibExtractor();

		datasets.add(extractor.extract(ecgStream));

		ArffExporter exporter = new ArffExporter();
		exporter.export(args[0], "ECG", datasets);

		System.out.println("Finished");
	}
}
