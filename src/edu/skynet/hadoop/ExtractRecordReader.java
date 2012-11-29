package edu.skynet.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class ExtractRecordReader extends RecordReader<LongWritable, Text> {

	private String data;
	private boolean isRead;

	public ExtractRecordReader(ExtractInputSplit split) {
		data = split.getRawData();
		isRead = false;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		return new LongWritable(0);
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		isRead = true;
		return new Text(data);
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public void initialize(InputSplit arg0, TaskAttemptContext arg1) throws IOException, InterruptedException {
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		return !isRead;
		// return false;
	}

}
