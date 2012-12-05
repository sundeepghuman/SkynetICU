package edu.skynet.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class PathWritable implements Writable, WritableComparable<PathWritable> {

	String path;

	public String getPath() {
		return path;
	}

	public PathWritable() {
		super();
	}

	public PathWritable(String path) {
		super();
		this.path = path;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		path = Text.readString(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, path.toString());
	}

	@Override
	public int compareTo(PathWritable writable) {
		return path.compareTo(writable.getPath());
	}

	@Override
	public String toString() {
		return path;
	}

}
