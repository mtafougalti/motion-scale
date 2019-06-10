package org.ms.calibration;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;

import org.ms.calibration.model.MovingAverage;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.RenameAttribute;

public class Main {

	public static void main(String[] args) throws Exception {
		meanRemoval();
	}

	private static void calibration() throws Exception {
		DataSource ds = new DataSource("C:\\Dev\\workspace\\motion-scale\\src\\\\main\\resources\\test.csv");
		System.out.println(ds.getDataSet().toSummaryString());

//		Normalize filter = new Normalize();
//		filter.setInputFormat(ds.getDataSet());
//		Instances dataset = Filter.useFilter(ds.getDataSet(), filter);

		RenameAttribute filter = new RenameAttribute();
		filter.setFind("voltage");
		filter.setReplace("weight");
		filter.setReplaceAll(true);
		filter.setInputFormat(ds.getDataSet());

		Instances dataset = Filter.useFilter(ds.getDataSet(), filter);
		Instances datasetCalibrated = calibrate(2, dataset);

		CSVSaver saver = new CSVSaver();
		saver.setInstances(datasetCalibrated);
		saver.setFile(new File("C:\\Dev\\workspace\\motion-scale\\src\\main\\resources\\test1.csv"));
		saver.writeBatch();
	}

	private static void meanRemoval() throws Exception {
		DataSource ds = new DataSource("C:\\Dev\\workspace\\motion-scale\\src\\main\\resources\\test1.csv");
		System.out.println(ds.getDataSet().toSummaryString());


//		Normalize filter = new Normalize();
//		filter.setInputFormat(ds.getDataSet());
//		filter.setScale(10);
//		filter.setTranslation(-5);
//		Instances dataset = Filter.useFilter(ds.getDataSet(), filter);
		
		Instances dataset = removeLocalMean(3, ds.getDataSet());

		CSVSaver saver = new CSVSaver();
		saver.setInstances(dataset);
		saver.setFile(new File("C:\\Dev\\workspace\\motion-scale\\src\\main\\resources\\test2.csv"));
		saver.writeBatch();
	}

	private static Instances calibrate(double slope, Instances dataSet) {
		Instances instances = dataSet;
		for (Instance instance : Collections.list(instances.enumerateInstances())) {
			for (Attribute attribute : Collections.list(instance.enumerateAttributes())) {
				if (attribute.index() == 1) {
					instance.setValue(attribute, instance.value(1) / slope);
				}
			}
		}
		return instances;
	}

	private static Instances removeLocalMean(int period, Instances dataSet) {
		Instances instances = dataSet;
		MovingAverage movingAverage = new MovingAverage(period);
		for (Instance instance : Collections.list(instances.enumerateInstances())) {
			movingAverage.add(new BigDecimal(instance.value(1)));
			double average = movingAverage.getAverage().doubleValue();
			System.out.println(average);
			for (Attribute attribute : Collections.list(instance.enumerateAttributes())) {
				if (attribute.index() == 1) {
					instance.setValue(attribute, instance.value(1)-average);
				}
			}
		}
		return instances;
	}

}
