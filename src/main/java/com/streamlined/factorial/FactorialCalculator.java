package com.streamlined.factorial;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

public class FactorialCalculator {

	public BigInteger compute(int number) {
		return new FactorialTask(number).compute();
	}

	private static class FactorialTask extends RecursiveTask<BigInteger> {

		private final int number;

		private FactorialTask(int number) {
			this.number = number;
		}

		@Override
		protected BigInteger compute() {
			BigInteger value = BigInteger.ONE;
			for (int k = 2; k <= number; k++) {
				value = value.multiply(BigInteger.valueOf(k));
			}
			return value;
		}

	}

}
