package org.ms.preprocessing;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.ms.preprocessing.model.MovingAverage;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RenameAttribute;

public class DataPreProcessor {

	private static final int SLOPE = 5;
	private static final int PERIOD = 2;

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
		List<Instance> list = Collections.list(instances.enumerateInstances());
		MovingAverage movingAverage = new MovingAverage(PERIOD);
		for (int i = 0; i < list.size(); i += PERIOD) {
			List<Instance> subList = list.subList(i, Math.min(list.size(), i + 10));
			for (Instance instance : subList) {
				movingAverage.add(new BigDecimal(instance.value(1)));
			}
			double average = movingAverage.getAverage().doubleValue();
			double sd = getSd(subList, average);

			for (Instance instance : subList) {
				for (Attribute attribute : Collections.list(instance.enumerateAttributes())) {
					if (attribute.index() == 1) {
						instance.setValue(attribute, (instance.value(1) - average)/sd);
					}
				}
			}
		}

		return instances;
	}

	private static double getSd(List<Instance> subList, double average) {
		double x = 0;
		for (Instance instance : subList) {
			x += Math.pow(instance.value(1) - average, 2);
		}
		double sd = Math.sqrt(x/subList.size());
		return sd;
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
