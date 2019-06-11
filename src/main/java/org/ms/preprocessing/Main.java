package org.ms.preprocessing;

import weka.core.Instances;

public class Main {

	public static void main(String[] args) throws Exception {
		Instances dataSet = DataManager.loadData();
		dataSet = DataPreProcessor.calibrate(dataSet);
		dataSet = DataPreProcessor.removeLocalMean(dataSet);
		DataManager.saveData(dataSet);
	}
}
