package org.ms.preprocessing;

import java.io.File;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class DataManager {
	
	private static final String DATA_FILE_INPUT_NAME = "C:\\Dev\\workspace\\motion-scale\\src\\\\main\\resources\\in.csv";
	private static final String DATA_FILE_OUTPUT_NAME = "C:\\Dev\\workspace\\motion-scale\\src\\\\main\\resources\\out.csv";
	
	public static Instances loadData() throws Exception {
		DataSource ds = new DataSource(DATA_FILE_INPUT_NAME);
		return ds.getDataSet();
	}
	
	public static void saveData(Instances dataSet) throws IOException {
		CSVSaver saver = new CSVSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(DATA_FILE_OUTPUT_NAME));
		saver.writeBatch();
	}

}
