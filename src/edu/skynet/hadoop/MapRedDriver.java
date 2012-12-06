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

		Configuration conf = getConf();

		// put these here instead of Main() to avoid command line parsing error
		// all setting of config file must be done before Job ctor, or else they don't show up in mapper
		conf.set("inputDirectory", args[0]);
		conf.set("extractor.className", args[1]);
		conf.set("sampleParser.className", args[2]);
		conf.set("annotationParser.className", args[3]);
		conf.setInt("sampleRate", Integer.parseInt(args[4]));

		Job job = new Job(getConf());

		job.setJarByClass(ExtractJob.ExtractorReducer.class);

		job.setInputFormatClass(ExtractInputFormat.class);

		job.setMapOutputKeyClass(PathWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setMapperClass(ExtractJob.ExtractorMapper.class);
		job.setReducerClass(ExtractJob.ExtractorReducer.class);

		MultipleOutputs.addNamedOutput(job, "extract", ExtractOutputFormat.class, PathWritable.class, Text.class);

		FileInputFormat.setInputPaths(job, new Path(conf.get("inputDirectory")));
		FileOutputFormat.setOutputPath(job, new Path("extractedData"));

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {

		Configuration conf = new Configuration();

		int res = ToolRunner.run(conf, new MapRedDriver(), args);
		System.exit(res);
	}
}