package edu.skynet.dataimport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Dataset;

public class TimeSeriesImporter<T> {

	public Dataset extractInstances(String file, FeatureExtractor<T> extractor, int sampleRate) throws FileNotFoundException {

		List<T> values = new ArrayList<>();

		// read in all values
		Scanner scanner = new Scanner(new File(file));

		String label = scanner.nextLine();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String value = line.split(",")[1];
			values.add(extractor.parse(value));
		}

		return extractor.extract(values, sampleRate, label);
	}
}
