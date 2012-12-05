package edu.skynet.hadoop.dataexport;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import edu.skynet.hadoop.writables.PathWritable;

public class ExtractRecordWriter extends RecordWriter<PathWritable, Text> {

	TaskAttemptContext context;

	public ExtractRecordWriter(TaskAttemptContext context) {
		this.context = context;
	}

	@Override
	public void close(TaskAttemptContext context) throws IOException, InterruptedException {

	}

	@Override
	public void write(PathWritable writable, Text text) throws IOException, InterruptedException {

		Configuration conf = context.getConfiguration();

		File inputFile = new File(writable.getPath());

		String outputPath = "extractedData" + "/" + inputFile.getName().split("\\.")[0] + ".txt";

		Path file = new Path(outputPath);
		FileSystem fs = file.getFileSystem(conf);

		FSDataOutputStream out = fs.create(file, true);

		out.write(text.getBytes(), 0, text.getLength());
	}
}
