package com.streamlined.factorial;

import org.junit.jupiter.api.Test;

class FactorialCalculatorPerformanceTest {

	@Test
	void measureExecutionTime() {
		int[] limits = new int[] { 10, 100, 1000, 10_000, 100_000 };
		var calculator = new FactorialCalculator();
		for (var limit : limits) {
			long startTime = System.currentTimeMillis();
			calculator.compute(limit);
			System.out.printf("Execution time %d msec%n", System.currentTimeMillis() - startTime);
		}
	}

}
