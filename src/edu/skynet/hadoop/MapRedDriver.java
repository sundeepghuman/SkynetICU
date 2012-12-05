package edu.skynet.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.skynet.hadoop.dataexport.ExtractOutputFormat;
import edu.skynet.hadoop.dataimport.ExtractInputFormat;
import edu.skynet.hadoop.writables.PathWritable;

public class MapRedDriver extends Configured implements Tool {
	public int run(String[] args) throws Exception {

		final String jobName = "extractedData";

		Job job = new Job();

		job.setJarByClass(ExtractJob.ExtractorReducer.class);

		job.setInputFormatClass(ExtractInputFormat.class);

		job.setMapOutputKeyClass(PathWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(ExtractJob.ExtractorMapper.class);
		job.setReducerClass(ExtractJob.ExtractorReducer.class);

		// set job name to name output folder that contains extracted data
		job.getConfiguration().set("job.name", jobName);

		MultipleOutputs.addNamedOutput(job, "extract", ExtractOutputFormat.class, PathWritable.class, Text.class);

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