package edu.skynet.dataexport;

import java.util.HashSet;
import java.util.List;

import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

public class ArffExporter implements DataExporter {

	public String export(List<Dataset> dataSets) {

		// Write text into Weka's Arff file format

		StringBuffer sb = new StringBuffer();

		// output name of the relation
		sb.append("@relation relationName");
		sb.append("\n\n");

		// output attributes and their types
		for (String attribute : dataSets.get(0).getAttributes()) {
			sb.append("@attribute ");
			sb.append(attribute);
			sb.append(" ");
			sb.append(getDataTypeName(dataSets.get(0), attribute));
			sb.append("\n");
		}
		sb.append("\n\n@data\n");

		// output the instances and all their attributes
		for (Dataset set : dataSets) {
			for (Instance i : set.getInstances()) {
				for (String attribute : set.getAttributes()) {
					sb.append(i.getValue(attribute));
					sb.append(",");
				}
				sb.append("\n");
			}
		}

		return sb.toString();

	}

	/**
	 * Get the Weka attribute type from the Java type
	 * 
	 * @param data Dataset that is being examined
	 * @param attributeName Type of the attribute in that dataset
	 * @return
	 */
	private String getDataTypeName(Dataset data, String attributeName) {

		// if the attribute is the label for the set, output a set of all the possible annotation values
		if (attributeName == FeatureExtractor.LABEL_ATTRIBUTE_NAME) {
			HashSet<String> annotationSet = new HashSet<String>();
			for (Instance i : data.getInstances()) {
				annotationSet.add(i.getValue(FeatureExtractor.LABEL_ATTRIBUTE_NAME).toString());
			}
			StringBuilder annotations = new StringBuilder();
			annotations.append("{");
			for (String annotation : annotationSet) {
				annotations.append(annotation);
				annotations.append(",");
			}
			annotations.append("}");

			return annotations.toString();
		}

		Object value = data.getInstances().get(0).getValue(attributeName);
		try {
			Double.parseDouble(value.toString());
			return "numeric";
		} catch (NumberFormatException e) {

		}

		try {
			Boolean.parseBoolean(value.toString());
			return "{true, false}";
		} catch (NumberFormatException e) {

		}
		return "";
	}
}
