package edu.skynet.dataexport;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import edu.skynet.featureextraction.FeatureExtractor;
import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

public class ArffExporter {

	public String export(String relationName, List<Dataset> dataSets) throws IOException {

		StringBuffer sb = new StringBuffer();

		sb.append("@relation ");
		sb.append(relationName);
		sb.append("\n\n");

		for (String attribute : dataSets.get(0).getAttributes()) {
			sb.append("@attribute ");
			sb.append(attribute);
			sb.append(" ");
			sb.append(getDataTypeName(dataSets.get(0), attribute));
			sb.append("\n");
		}
		sb.append("\n\n@data\n");

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

	private String getDataTypeName(Dataset data, String attributeName) {

		// get the annotation values if this is the label attribute
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
			Float.parseFloat(value.toString());
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
