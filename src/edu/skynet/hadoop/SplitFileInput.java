package edu.skynet.hadoop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class SplitFileInput extends InputFormat<LongWritable, Text> {

	@Override
	public RecordReader<LongWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext arg1) throws IOException, InterruptedException {

		return new ExtractRecordReader((ExtractInputSplit) split);
	}

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException, InterruptedException {

		List<InputSplit> splits = new ArrayList<InputSplit>();

		File folder = new File(context.getWorkingDirectory().toString());
		for (File file : folder.listFiles()) {
			splits.add(new ExtractInputSplit("hey", "bro"));
		}

		return splits;
	}
}
