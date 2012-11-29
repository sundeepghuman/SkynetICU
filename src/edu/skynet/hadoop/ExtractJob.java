package edu.skynet.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class ExtractJob {

	// private static int count = 0;

	public static class ExtractorMapper extends Mapper<LongWritable, Text, LongWritable, Text> {

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			context.write(key, value);
			// count++;
		}
	}

	public static class ExtractorReducer extends Reducer<LongWritable, Text, LongWritable, Text> {

		@Override
		protected void reduce(LongWritable arg0, Iterable<Text> arg1, Context context) throws IOException, InterruptedException {

			while (arg1.iterator().hasNext()) {
				context.write(arg0, arg1.iterator().next());
			}
		}
	}
}
