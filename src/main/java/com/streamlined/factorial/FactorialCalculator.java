package com.streamlined.factorial;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FactorialCalculator {

	private final ForkJoinPool pool;

	public FactorialCalculator() {
		this.pool = (ForkJoinPool) Executors.newWorkStealingPool(ForkJoinPool.getCommonPoolParallelism());
	}

	public BigInteger compute(int number) {
		return new FactorialTask(2, number).compute();
	}

	private class FactorialTask extends RecursiveTask<BigInteger> {

		private static final int THRESHOLD = 5;
		private static final int PART_COUNT = 2;

		private final int start;
		private final int finish;

		private FactorialTask(int start, int finish) {
			this.start = start;
			this.finish = finish;
		}

		@Override
		protected BigInteger compute() {
			if (start + THRESHOLD > finish) {
				return calculate(start, finish);
			} else {
				List<FactorialTask> tasks = distributeTasks();
				submitTasks(tasks);
				return aggregateResult(tasks);
			}
		}

		private List<FactorialTask> distributeTasks() {
			final int partLength = getPartLength();
			List<FactorialTask> tasks = new ArrayList<>(PART_COUNT);
			int from = start;
			for (int k = 0; k < PART_COUNT && from <= finish; k++) {
				int to = Math.min(from + partLength - 1, finish);
				tasks.add(new FactorialTask(from, to));
				from = to + 1;
			}
			return tasks;
		}

		private void submitTasks(List<FactorialTask> tasks) {
			for (var task : tasks) {
				pool.submit(task);
			}
		}

		private BigInteger calculate(int start, int finish) {
			BigInteger value = BigInteger.ONE;
			for (int k = start; k <= finish; k++) {
				value = value.multiply(BigInteger.valueOf(k));
			}
			return value;
		}

		private int getPartLength() {
			int length = (finish - start + 1) / PART_COUNT;
			if (length * PART_COUNT < (finish - start + 1)) {
				length++;
			}
			return length;
		}

		private BigInteger aggregateResult(List<FactorialTask> tasks) {
			BigInteger value = BigInteger.ONE;
			for (var task : tasks) {
				value = value.multiply(task.join());
			}
			return value;
		}

	}

}
