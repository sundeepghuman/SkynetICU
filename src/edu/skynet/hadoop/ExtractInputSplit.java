package edu.skynet.hadoop;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class ExtractInputSplit extends InputSplit implements Writable {

	private Path path;

	public ExtractInputSplit() {

	}

	public ExtractInputSplit(Path path) {
		this.path = path;
	}

	@Override
	public long getLength() throws IOException, InterruptedException {
		return 100;
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {

		return new String[] {};
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		path = new Path(Text.readString(in));
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, path.toString());
	}

}
