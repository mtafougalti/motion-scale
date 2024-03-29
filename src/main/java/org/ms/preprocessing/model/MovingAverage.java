package org.ms.preprocessing.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {
	private final Queue<BigDecimal> window = new LinkedList<BigDecimal>();
	private final int period;
	private BigDecimal sum = BigDecimal.ZERO;

	public MovingAverage(int period) {
		this.period = period;
	}

	public void add(BigDecimal num) {
		sum = sum.add(num);
		window.add(num);
		if (window.size() > period) {
			sum = sum.subtract(window.remove());
		}
	}

	public BigDecimal getAverage() {
		if (window.isEmpty())
			return BigDecimal.ZERO;
		BigDecimal divisor = BigDecimal.valueOf(window.size());
		return sum.divide(divisor, 2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getStandardDeviation() {
		if (window.isEmpty())
			return BigDecimal.ONE;
		BigDecimal divisor = BigDecimal.valueOf(window.size());
		return sum.divide(divisor, 2, RoundingMode.HALF_UP);
	}
}
