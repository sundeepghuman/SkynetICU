package edu.skynet.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import edu.skynet.hadoop.writables.PathWritable;
import edu.skynet.hadoop.writables.TimeSeriesData;

public class ExtractJob {

	public static class ExtractorMapper extends Mapper<PathWritable, TimeSeriesData, PathWritable, Text> {

		@Override
		protected void map(PathWritable key, TimeSeriesData value, Context context) throws IOException, InterruptedException {
			// String data = value.toString();
			//
			// final int sampleRate = 250;
			// final String extractorName = "AfibExtractor";
			// final int minSamplesPerSlice = 2500;
			// final int maxSamplesPerSlice = 5000;
			//
			// // instantiate user's feature extractor
			// FeatureExtractor extractor = (FeatureExtractor) Class.forName(extractorName).newInstance();
			//
			// // pass in slicing params
			// extractor.setMaxSamplesPerSlice(maxSamplesPerSlice);
			// extractor.setMinSamplesPerSlice(minSamplesPerSlice);
			//
			// SampleParser dataParser = new SampleParser(data, "\n", "\\s+", 1);
			// AnnotationParser annotationParser = new AnnotationParser("test-afib-data/04043_annotations.txt", "\\s+", 1, 6);
			//
			// Datastream ecgStream = new Datastream(sampleRate, dataParser, annotationParser);
			//
			// // extract features
			// List<Dataset> datasets = new ArrayList<Dataset>();
			// datasets.add(extractor.extract(ecgStream));
			//
			// ArffExporter exporter = new ArffExporter();
			// exporter.export(args[0], "relation-name", datasets);

			context.write(key, new Text(value.getData() + "\n" + value.getAnnotations()));
		}
	}

	public static class ExtractorReducer extends Reducer<PathWritable, Text, PathWritable, Text> {

		private MultipleOutputs<PathWritable, Text> mos;

		public void setup(Context context) {
			mos = new MultipleOutputs<PathWritable, Text>(context);
		}

		@Override
		protected void reduce(PathWritable key, Iterable<Text> data, Context context) throws IOException, InterruptedException {
			while (data.iterator().hasNext()) {
				Text timeData = data.iterator().next();
				mos.write("extract", key, timeData);
			}
		}

	}
}
