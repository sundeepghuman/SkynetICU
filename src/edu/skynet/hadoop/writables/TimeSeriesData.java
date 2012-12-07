package edu.skynet.hadoop.writables;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 * Writable that contains all the sample and annotation raw data
 * 
 */
public class TimeSeriesData implements Writable, WritableComparable<TimeSeriesData> {

	String data;
	String annotations;

	public TimeSeriesData() {
		super();
	}

	public TimeSeriesData(String data, String annotations) {
		super();
		this.data = data;
		this.annotations = annotations;
	}

	public String getData() {
		return data;
	}

	public String getAnnotations() {
		return annotations;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		data = Text.readString(in);
		annotations = Text.readString(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, data.toString());
		Text.writeString(out, annotations.toString());
	}

	@Override
	public int compareTo(TimeSeriesData writable) {
		if (writable.getData().compareTo(data) == 0 && writable.getAnnotations().compareTo(annotations) == 0) {
			return 0;
		} else {
			return -1;
		}
	}
}
