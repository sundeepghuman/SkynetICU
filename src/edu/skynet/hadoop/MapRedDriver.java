package edu.skynet.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class MapRedDriver extends Configured implements Tool {
	public int run(String[] args) throws Exception {

		Job job = new Job();

		job.setJarByClass(ExtractJob.ExtractorReducer.class);

		job.setMapperClass(ExtractJob.ExtractorMapper.class);
		job.setReducerClass(ExtractJob.ExtractorReducer.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new MapRedDriver(), args);
		System.exit(res);
	}
}