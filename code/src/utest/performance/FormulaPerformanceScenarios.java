package performance;

import pcgen.base.formula.AddingFormula;
import pcgen.base.formula.SubtractingFormula;
import pcgen.base.formula.MultiplyingFormula;
import pcgen.base.formula.DividingFormula;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Simple performance harness for PCGen's formula workflow.
 *
 * This is our "load generator" for the assignment:
 * - Load test: steady, moderate concurrency
 * - Stress test: gradually increasing concurrency
 * - Spike test: short bursts of high concurrency
 *
 * Run from IntelliJ (or any IDE) with program args:
 *   load
 *   stress
 *   spike
 */
public class FormulaPerformanceScenarios {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: FormulaPerformanceScenarios <load|stress|spike>");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();
        switch (mode) {
            case "load" -> runLoadTest();
            case "stress" -> runStressTest();
            case "spike" -> runSpikeTest();
            default -> {
                System.err.println("Unknown mode: " + mode);
                System.exit(1);
            }
        }
    }

    /**
     * This is the PCGen-like workload:
     * A small end-to-end chain of formula operations.
     * We’ll invoke this many times under different patterns.
     */
    private static int formulaWorkflow(int base) {
        Number result = base;

        AddingFormula add10 = new AddingFormula(10);
        result = add10.resolve(result);

        SubtractingFormula sub3 = new SubtractingFormula(3);
        result = sub3.resolve(result);

        MultiplyingFormula mult2 = new MultiplyingFormula(2);
        result = mult2.resolve(result);

        DividingFormula div4 = new DividingFormula(4);
        result = div4.resolve(result);

        return result.intValue();
    }

    private static void runLoadTest() throws Exception {
        System.out.println("TEST_TYPE=LOAD");
        int threads = 4;           // steady moderate load
        int operationsPerThread = 100_000;

        runScenario("LOAD", threads, operationsPerThread, 0);
    }

    private static void runStressTest() throws Exception {
        System.out.println("TEST_TYPE=STRESS");
        // Increase concurrency step by step, watch when latency / errors blow up
        int[] threadLevels = {1, 2, 4, 8, 16, 32};
        int operationsPerThread = 30_000;

        for (int level : threadLevels) {
            runScenario("STRESS_THREADS_" + level, level, operationsPerThread, 0);
        }
    }

    private static void runSpikeTest() throws Exception {
        System.out.println("TEST_TYPE=SPIKE");
        // Alternate idle and heavy bursts
        int idleMillis = 2000;
        int spikeThreads = 32;
        int operationsPerThread = 10_000;

        for (int i = 1; i <= 5; i++) {
            System.out.println("SPIKE_ITERATION=" + i + " (idle)");
            Thread.sleep(idleMillis);

            runScenario("SPIKE_BURST_" + i, spikeThreads, operationsPerThread, 0);
        }
    }

    /**
     * Core runner shared by all three test types.
     * Prints CSV to stdout: scenario,threadCount,totalOps,avgLatencyNanos,heapUsedBytes
     */
    private static void runScenario(String label, int threadCount, int operationsPerThread, long thinkTimeMillis)
            throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        long totalOps = (long) threadCount * operationsPerThread;

        List<Callable<Long>> tasks = new ArrayList<>();
        for (int t = 0; t < threadCount; t++) {
            tasks.add(() -> {
                long localCount = 0;
                for (int i = 0; i < operationsPerThread; i++) {
                    long start = System.nanoTime();
                    formulaWorkflow(i);
                    long end = System.nanoTime();
                    localCount += (end - start);

                    if (thinkTimeMillis > 0) {
                        Thread.sleep(thinkTimeMillis);
                    }
                }
                return localCount;
            });
        }

        Runtime rt = Runtime.getRuntime();
        rt.gc();
        Thread.sleep(500);

        long heapBefore = rt.totalMemory() - rt.freeMemory();
        long wallStart = System.nanoTime();

        List<Future<Long>> results = executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        long wallEnd = System.nanoTime();
        long heapAfter = rt.totalMemory() - rt.freeMemory();

        long totalNanos = 0;
        for (Future<Long> f : results) {
            totalNanos += f.get();
        }

        double avgPerOpNanos = (double) totalNanos / totalOps;
        double wallSeconds = (wallEnd - wallStart) / 1_000_000_000.0;
        long heapDelta = heapAfter - heapBefore;

        System.out.printf(
                "SCENARIO=%s,THREADS=%d,TOTAL_OPS=%d,AVG_LATENCY_NS=%.2f,WALL_SECONDS=%.2f,HEAP_DELTA_BYTES=%d%n",
                label, threadCount, totalOps, avgPerOpNanos, wallSeconds, heapDelta
        );
    }
}