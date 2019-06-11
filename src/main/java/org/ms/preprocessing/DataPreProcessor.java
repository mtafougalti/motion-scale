package org.ms.preprocessing;

import java.math.BigDecimal;
import java.util.Collections;

import org.ms.preprocessing.model.MovingAverage;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RenameAttribute;

public class DataPreProcessor {
	
	private static final int SLOPE = 5;
	private static final int PERIOD = 5;
	
	public static Instances calibrate(Instances dataSet) throws Exception {
		RenameAttribute filter = new RenameAttribute();
		filter.setFind("voltage");
		filter.setReplace("weight");
		filter.setReplaceAll(true);
		filter.setInputFormat(dataSet);

		Instances filtredDataSet = Filter.useFilter(dataSet, filter);
		return estimateWeight(SLOPE, filtredDataSet);
	}
	
	public static Instances removeLocalMean(Instances dataSet) {
		Instances instances = dataSet;
		MovingAverage movingAverage = new MovingAverage(PERIOD);
		int i = 0;
		for (Instance instance : Collections.list(instances.enumerateInstances())) {
			movingAverage.add(new BigDecimal(instance.value(1)));
			if(i++ % PERIOD == 0) {
				
			}
			double average = movingAverage.getAverage().doubleValue();
			for (Attribute attribute : Collections.list(instance.enumerateAttributes())) {
				if (attribute.index() == 1) {
					instance.setValue(attribute, instance.value(1)-average);
				}
			}
		}
		return instances;
	}
	
	private static Instances estimateWeight(double slope, Instances dataSet) {
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

}
