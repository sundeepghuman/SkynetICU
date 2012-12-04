package edu.skynet.hadoop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class ExtractRecordReader extends RecordReader<PathWritable, Text> {

	ExtractInputSplit split;
	boolean isRead;

	public ExtractRecordReader(ExtractInputSplit split) {
		this.split = split;
		isRead = false;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public PathWritable getCurrentKey() throws IOException, InterruptedException {
		return new PathWritable(split.getPath().getName());
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		isRead = true;

		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		FSDataInputStream stream = fs.open(split.getPath());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1000];

		while ((stream.read(buffer)) > 0) {
			outputStream.write(buffer);
		}

		return new Text(new String(outputStream.toByteArray()));
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
	}

}
