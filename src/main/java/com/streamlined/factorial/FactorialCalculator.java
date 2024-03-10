package com.streamlined.factorial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FactorialCalculator {

	public BigInteger compute(int number) {
		return new FactorialTask(2, number).compute();
	}

	private static class FactorialTask extends RecursiveTask<BigInteger> {

		private final int start;
		private final int finish;

		private FactorialTask(int start, int finish) {
			this.start = start;
			this.finish = finish;
		}

		@Override
		protected BigInteger compute() {
			if (finish == start) {
				return BigInteger.valueOf(start);
			} else if (finish - start == 1) {
				return BigInteger.valueOf(start).multiply(BigInteger.valueOf(finish));
			} else {
				List<FactorialTask> tasks = distributeTasks();
				invokeAll(tasks);
				return computeResult(tasks);
			}
		}

		private List<FactorialTask> distributeTasks() {
			final int workerCount = getNumberOfWorkers();
			final int workerRangeLength = (finish - start + 1) / workerCount + 1;
			List<FactorialTask> tasks = new ArrayList<>(workerCount);
			int from = start;
			for (int k = 0; k < workerCount && from <= finish; k++) {
				int to = Math.min(from + workerRangeLength - 1, finish);
				tasks.add(new FactorialTask(from, to));
				from = to + 1;
			}
			return tasks;
		}

		private BigInteger computeResult(List<FactorialTask> tasks) {
			BigInteger value = BigInteger.ONE;
			for (var task : tasks) {
				value = value.multiply(task.join());
			}
			return value;
		}

		private int getNumberOfWorkers() {
			return Runtime.getRuntime().availableProcessors();
		}

	}

}
