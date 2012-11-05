package edu.skynet.ml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dataset {

	private List<Instance> data;

	public Dataset() {
		data = new ArrayList<Instance>();
	}

	public Dataset(List<Instance> instances) {
		this.data = instances;
	}

	public void addInstance(Instance i) {
		data.add(i);
	}

	public List<Instance> getInstances() {
		return data;
	}

	public Set<String> getAttributes() {
		Set<String> attributes = new HashSet<String>();
		if (data != null && data.size() >= 0) {
			for (String attribute : data.get(0).getAttributeNames()) {
				attributes.add(attribute);
			}
		}

		return attributes;
	}
}
