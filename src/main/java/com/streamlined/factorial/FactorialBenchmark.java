package com.streamlined.factorial;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FactorialBenchmark {

	private static final int MAX_NUMBER = 1000;

	@Param({ "1000" })
	private int size;

	private List<Integer> testData;
	private FactorialCalculator factorialCalculator;

	@Setup
	public void createTestData() {
		factorialCalculator = new FactorialCalculator();
		Random random = new SecureRandom();
		testData = new ArrayList<>(size);
		for (int k = 0; k < size; k++) {
			testData.add(random.nextInt(MAX_NUMBER));
		}
	}

	@Benchmark
	@Fork(value = 1, warmups = 1)
	@Warmup(iterations = 1)
	@Measurement(iterations = 3)
	public void test(Blackhole blackhole) {
		for (var value : testData) {
			blackhole.consume(factorialCalculator.compute(value));
		}
	}

	public static void main(String... args) throws IOException {
		org.openjdk.jmh.Main.main(args);
	}

}
