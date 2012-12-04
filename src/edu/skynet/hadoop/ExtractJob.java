package edu.skynet.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class ExtractJob {

	public static class ExtractorMapper extends Mapper<PathWritable, Text, PathWritable, Text> {

		@Override
		protected void map(PathWritable key, Text value, Context context) throws IOException, InterruptedException {
			// String data = value.toString();

			context.write(key, value);
		}
	}

	public static class ExtractorReducer extends Reducer<PathWritable, Text, PathWritable, Text> {

		@Override
		protected void reduce(PathWritable key, Iterable<Text> arg1, Context context) throws IOException, InterruptedException {

			while (arg1.iterator().hasNext()) {
				context.write(key, arg1.iterator().next());
			}
		}
	}
}
