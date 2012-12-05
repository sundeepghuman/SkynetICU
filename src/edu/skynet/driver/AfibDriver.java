package edu.skynet.driver;

import java.io.IOException;

public class AfibDriver {

	public static void main(String[] args) throws IOException {

		// final int sampleRate = 250;
		//
		// List<Dataset> datasets = new ArrayList<Dataset>();
		//
		// SampleParser dataParser = new SampleParser("test-afib-data/04043.txt", "\n", "\\s+", 1);
		// AnnotationParser annotationParser = new AnnotationParser("test-afib-data/04043_annotations.txt", "\\s+", 1, 6);
		//
		// Datastream ecgStream = new Datastream(sampleRate, dataParser, annotationParser);
		// AfibExtractor extractor = new AfibExtractor();
		// extractor.setMaxSamplesPerSlice(5000);
		// extractor.setMinSamplesPerSlice(2500);
		//
		// datasets.add(extractor.extract(ecgStream));
		//
		// ArffExporter exporter = new ArffExporter();
		// exporter.export(args[0], "relation-name", datasets);
		//
		// System.out.println("Finished");
	}
}
