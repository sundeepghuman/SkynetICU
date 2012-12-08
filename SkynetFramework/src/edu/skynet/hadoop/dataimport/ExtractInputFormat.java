package edu.skynet.hadoop.dataimport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import edu.skynet.hadoop.writables.PathWritable;
import edu.skynet.hadoop.writables.TimeSeriesData;

public class ExtractInputFormat extends FileInputFormat<PathWritable, TimeSeriesData> {

	@Override
	public RecordReader<PathWritable, TimeSeriesData> createRecordReader(InputSplit split, TaskAttemptContext arg1) throws IOException, InterruptedException {

		return new ExtractRecordReader((ExtractInputSplit) split);
	}

	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {

		// generate splits
		List<InputSplit> splits = new ArrayList<InputSplit>();

		HashSet<String> dataFiles = new HashSet<String>();
		HashSet<String> annotationFiles = new HashSet<String>();

		// record all files that are given to hadoop
		List<FileStatus> files = listStatus(job);
		for (FileStatus file : files) {
			String filename = file.getPath().toString();

			if (filename.contains("__annotations")) {
				annotationFiles.add(filename);
			} else {
				dataFiles.add(filename);
			}
		}

		// try to pair the data files with the annotation files
		for (String dataFile : dataFiles) {
			String[] parts = dataFile.split("\\.");
			String annotationFilename = parts[0] + "__annotations." + parts[1];

			if (!annotationFiles.contains(annotationFilename)) {
				throw new RuntimeException("No annotation file given for the data file: " + dataFile + ". Could not find: " + annotationFilename);
			}
			splits.add(new ExtractInputSplit(new Path(dataFile), new Path(annotationFilename)));

		}

		return splits;
	}
}
