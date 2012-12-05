package edu.skynet.hadoop.dataimport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import edu.skynet.hadoop.writables.PathWritable;
import edu.skynet.hadoop.writables.TimeSeriesData;

public class ExtractRecordReader extends RecordReader<PathWritable, TimeSeriesData> {

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
		return new PathWritable(split.getDataPath().getName());
	}

	@Override
	public TimeSeriesData getCurrentValue() throws IOException, InterruptedException {
		isRead = true;

		String data = readFile(split.getDataPath());
		String annotations = readFile(split.getAnnotationPath());
		TimeSeriesData timeseries = new TimeSeriesData(data, annotations);

		return timeseries;
	}

	private String readFile(Path path) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);

		FSDataInputStream stream = fs.open(path);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1000];

		while ((stream.read(buffer)) > 0) {
			outputStream.write(buffer);
		}
		
		stream.close();

		return new String(outputStream.toByteArray());
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
