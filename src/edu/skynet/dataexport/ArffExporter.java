package edu.skynet.dataexport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.skynet.ml.Dataset;
import edu.skynet.ml.Instance;

public class ArffExporter {

	public void export(String outputPath, String relationName, List<Dataset> dataSets) throws IOException {
		File file = new File(outputPath);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		StringBuffer sb = new StringBuffer();

		sb.append("@relation ");
		sb.append(relationName);
		sb.append("\n\n");

		for (String attribute : dataSets.get(0).getAttributes()) {
			sb.append("@attribute ");
			sb.append(attribute);
			sb.append(" ");
			sb.append(getDataTypeName(dataSets.get(0), attribute));
			sb.append("\n ");
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

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(sb.toString());
		bw.close();

	}

	private String getDataTypeName(Dataset data, String attributeName) {
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
