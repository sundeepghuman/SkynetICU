package edu.skynet.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class SplitFileInput extends FileInputFormat<PathWritable, Text> {

	@Override
	public RecordReader<PathWritable, Text> createRecordReader(InputSplit split, TaskAttemptContext arg1) throws IOException, InterruptedException {

		return new ExtractRecordReader((ExtractInputSplit) split);
	}

	@Override
	public List<InputSplit> getSplits(JobContext job) throws IOException {

		// generate splits
		List<InputSplit> splits = new ArrayList<InputSplit>();
		List<FileStatus> files = listStatus(job);
		for (FileStatus file : files) {
			Path path = file.getPath();
			splits.add(new ExtractInputSplit(path));
		}

		if (splits.size() == 0)
			throw new RuntimeException();

		return splits;
	}
}
