package edu.skynet.ml;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents a single instance of data with attributes and a label
 * 
 */
public class Instance {

	private HashMap<String, Object> values;

	public Instance() {
		values = new HashMap<String, Object>();
	}

	public Object getValue(String attributeName) {
		return values.get(attributeName);
	}

	public void addAttribute(String name, Object value) {
		values.put(name, value);
	}

	public Set<String> getAttributeNames() {
		return values.keySet();
	}
}
