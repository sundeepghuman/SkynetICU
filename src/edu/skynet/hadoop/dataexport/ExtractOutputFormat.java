package edu.skynet.hadoop.dataexport;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import edu.skynet.hadoop.writables.PathWritable;

/**
 * OutputFormat that data into a file
 * 
 */
public class ExtractOutputFormat extends FileOutputFormat<PathWritable, Text> {

	@Override
	public RecordWriter<PathWritable, Text> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {

		return new ExtractRecordWriter(context);
	}

}
