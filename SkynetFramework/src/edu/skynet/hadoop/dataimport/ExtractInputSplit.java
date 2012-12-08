package edu.skynet.hadoop.dataimport;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class ExtractInputSplit extends InputSplit implements Writable {

	private Path dataPath;
	private Path annotationPath;

	public ExtractInputSplit() {

	}

	public ExtractInputSplit(Path dataPath, Path annotationPath) {
		this.dataPath = dataPath;
		this.annotationPath = annotationPath;
	}

	@Override
	public long getLength() throws IOException, InterruptedException {
		return 100; // no idea
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {

		return new String[] {};
	}

	public Path getDataPath() {
		return dataPath;
	}

	public Path getAnnotationPath() {
		return annotationPath;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		dataPath = new Path(Text.readString(in));
		annotationPath = new Path(Text.readString(in));
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, dataPath.toString());
		Text.writeString(out, annotationPath.toString());
	}

}
