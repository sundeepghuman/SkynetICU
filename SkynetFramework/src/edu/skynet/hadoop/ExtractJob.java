package edu.skynet.hadoop;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import edu.skynet.dataexport.ArffExporter;
import edu.skynet.dataexport.DataExporter;
import edu.skynet.dataimport.Datastream;
import edu.skynet.dataimport.parsers.AnnotationParser;
import edu.skynet.dataimport.parsers.SampleParser;
import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.hadoop.writables.PathWritable;
import edu.skynet.hadoop.writables.TimeSeriesData;
import edu.skynet.ml.Dataset;

public class ExtractJob {

	public static class ExtractorMapper extends Mapper<PathWritable, TimeSeriesData, PathWritable, Text> {

		@Override
		protected void map(PathWritable key, TimeSeriesData value, Context context) throws IOException, InterruptedException {

			String samples = value.getData();
			String annotations = value.getAnnotations();

			Configuration conf = context.getConfiguration();
			final int sampleRate = conf.getInt("sampleRate", 1);
			final String extractorName = conf.get("extractor.className");
			final String sampleParserName = conf.get("sampleParser.className");
			final String annotationParserName = conf.get("annotationParser.className");

			FeatureExtractor extractor = (FeatureExtractor) instantiateClass(extractorName);
			SampleParser sampleParser = (SampleParser) instantiateClass(sampleParserName, samples);
			AnnotationParser annotationParser = (AnnotationParser) instantiateClass(annotationParserName, annotations);
			DataExporter exporter = null;

			// try to get optional data exporter
			if (conf.get("exporter.class") != null) {
				exporter = (DataExporter) instantiateClass(conf.get("exporter.class"));
			} else {
				exporter = new ArffExporter();
			}

			// pass in slicing params
			if (conf.get("maxSlice") != null)
				extractor.setMaxSamplesPerSlice(Integer.parseInt(conf.get("maxSlice")));
			if (conf.get("minSlice") != null)
				extractor.setMinSamplesPerSlice(Integer.parseInt(conf.get("minSlice")));

			Datastream ecgStream = new Datastream(sampleRate, sampleParser, annotationParser);

			// extract features
			List<Dataset> datasets = new ArrayList<Dataset>();
			datasets.add(extractor.extract(ecgStream));

			// export features to a string
			String exportedData = exporter.export(datasets);

			context.write(key, new Text(exportedData));
		}

		/**
		 * Instantiate the given class with the given params
		 */
		private Object instantiateClass(String className, Object... params) {
			Class<?>[] args = null;
			Constructor<?> ctor = null;
			try {
				// get types of args to reflect the ctor
				args = new Class<?>[params.length];
				int index = 0;
				for (Object param : params) {
					args[index] = param.getClass();
					index++;
				}

				Class<?> clazz = Class.forName(className);
				// return clazz.newInstance();
				ctor = clazz.getConstructor(args);
				return ctor.newInstance(params);

			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to instantiate class: " + className + ". " + e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to instantiate class: " + className + ". " + e.getMessage());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot find class: " + className);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException("Given arguments not valid for instantiating class: " + className + ". " + e.getMessage());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to instantiate class: " + className);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot find a constructor to match given arguments for this class: " + className + ". " + e.getMessage());
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new RuntimeException("Unable to instantiate class: " + className + ". " + e.getMessage());
			}
		}
	}

	public static class ExtractorReducer extends Reducer<PathWritable, Text, PathWritable, Text> {

		private MultipleOutputs<PathWritable, Text> mos;

		public void setup(Context context) {
			mos = new MultipleOutputs<PathWritable, Text>(context);
		}

		@Override
		protected void reduce(PathWritable key, Iterable<Text> data, Context context) throws IOException, InterruptedException {

			// just pass the data through the reducer
			while (data.iterator().hasNext()) {
				Text timeData = data.iterator().next();
				mos.write("extract", key, timeData);
			}
		}

	}
}
