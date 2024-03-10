package com.streamlined.factorial;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialCalculator {

	public BigInteger compute(int number) {
		return new FactorialTask(2, number).compute();
	}

	private static class FactorialTask extends RecursiveTask<BigInteger> {

		private static final int RANGE_LENGTH = 10;

		private final int start;
		private final int finish;

		private FactorialTask(int start, int finish) {
			this.start = start;
			this.finish = finish;
		}

		@Override
		protected BigInteger compute() {
			if (finish - start + 1 <= RANGE_LENGTH) {
				return calculate();
			} else {
				final int middle = (start + finish) / 2;
				FactorialTask task1 = new FactorialTask(start, middle);
				FactorialTask task2 = new FactorialTask(middle + 1, finish);
				invokeAll(task1, task2);
				return task1.join().multiply(task2.join());
			}
		}

		private BigInteger calculate() {
			BigInteger value = BigInteger.ONE;
			for (int k = start; k <= finish; k++) {
				value = value.multiply(BigInteger.valueOf(k));
			}
			return value;
		}

	}

}
