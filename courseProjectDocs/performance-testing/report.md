
# Performance Testing Report

## Load Test

### Test Scope and Design
This test exercises PCGen's formula workflow under a steady moderate load. We used the FormulaPerformanceScenarios test harness in code/src/utest/performance/FormulaPerformanceScenarios.java. The harness repeatedly evaluates a small end to end formula pipeline using PCGen's AddingFormula, SubtractFormula, MultiplyingFormula, and DividingFormula classes. The goal of this load test is to simulate a consistent workload and measure baseline latency, throughput, and memory behavior.

### Configuration
- Test type: Load
- Mode: load
- Threads: 4
- Operations per thread: 100000
- Total operations: 400000
- Command used:

```bash
java -cp "build/classes/java/test:build/classes/java/main:build/resources/test:build/resources/main" performance.FormulaPerformanceScenarios load
```

### Results
Terminal output:

```bash
TEST_TYPE=LOAD
SCENARIO=LOAD,THREADS=4,TOTAL_OPS=400000,AVG_LATENCY_NS=315.23,WALL_SECONDS=0.06,HEAP_DELTA_BYTES=4474136
```

### Interpretation
- Average latency per operation was about 315 nanoseconds
- Total execution time for all operations was roughly 0.06 seconds
- Heap usage increased slightly during the run by approximately 4.5 MB
- VisualVM profiling showed stable CPU activity with small GC events and no memory retention problems

### Findings
The formula workflow remained efficient under load conditions. No stalls occurred and latency stayed low which establishes a strong performance baseline. Memory growth was expected and did not continue to rise which shows no leak risk.

## Stress Test

### Test Scope and Design
The stress test increases concurrency to observe how latency and throughput change as threads scale. This test used thread counts of 1, 2, 4, 8, 16, and 32. The workflow is identical to the load case but designed to push the system toward saturation.

### Configuration
- Test type: Stress
- Mode: stress
- Threads tested: 1, 2, 4, 8, 16, 32
- Operations per thread: 30000
- Command used:

```bash
java -cp "build/classes/java/test:build/classes/java/main:build/resources/test:build/resources/main" performance.FormulaPerformanceScenarios stress
```

### Results
Stress output recorded:

1 thread: avg latency 113 ns wall 0.01 sec  
2 threads: avg latency 160 ns wall 0.01 sec  
4 threads: avg latency 150 ns wall 0.01 sec  
8 threads: avg latency 273 ns wall 0.03 sec  
16 threads: avg latency 566 ns wall 0.05 sec  
32 threads: avg latency 524 ns wall 0.10 sec  

### Interpretation
Performance scales well up to about 8 threads. Latency increases above that point and throughput gains flatten which indicates CPU saturation. Heap deltas rise slightly but remained controlled.

### Findings
The optimal performance range appears to be between 4 and 8 threads. Beyond this threshold increased concurrency only raises latency which confirms contention and diminishing returns.

## Spike Test

### Test Scope and Design
The spike scenario measured how the system reacts to sudden bursts following idle time. Five spikes were executed with 32 threads each separated by two seconds of inactivity.

### Configuration
- Test type: Spike
- Mode: spike
- Threads: 32 per burst
- Bursts run: 5
- Operations per burst: 320000 total
- Idle interval: 2 seconds

### Results
#### Average Latency (nanoseconds)

Iteration 1: 1786.08 ns

Iteration 2: 236.75 ns

Iteration 3: 39.25 ns

Iteration 4: 38.76 ns

Iteration 5: 40.07 ns

#### Wall Time (seconds)

Iteration 1: 0.03 s

Iteration 2: 0.02 s

Iteration 3: 0.01 s

Iteration 4: 0.01 s

Iteration 5: 0.01 s

#### Heap Delta (bytes)

Iteration 1: 190,296 bytes

Iteration 2: 189,160 bytes

Iteration 3: 197,544 bytes

Iteration 4: 288,496 bytes

Iteration 5: 355,640 bytes

| Iteration # | Average Latency (ns) | Wall Time (s) | Heap Delta (bytes |
| Iteration 1 | 1786.08 ns | 0.03 s | 190,296 bytes |

### Findings
We notice the average latency in the first iteration has an extremely high average latency. The next takes a significant decrease before evening out at iterations three four and five. This pattern is reflected in the real world time per iteration, or wall time. The amount of memory used for each shows an opposite pattern, starting stable and then increasing between iterations 3-5.

### Interpretation
The slow start in average latency and wall time is likely because it is being ran on a java virtual machine which uses just in time compliation. Thus, you get the warm up effect of the time efficency beginning slow. Because it stablizes after just a couple iterations, this is not a major worry. The increased heap delta as iterations go on could be a cause of worry if they were on a greater scale. However, as they are only increasing by less than a kilobyte, it is not a strong worry. 


## Group Contributions

| **Member** | **Task / Contribution** | **Notes** |
| ----- | ----- | ----- | 
| Shahmir M. Khan | Added FormulaPerformanceScenarios.java, ran load testing, prepared documentation | N/A |
| Tyler Jaafari | Added documentation and refactored FormulaPerformanceScenarios to handle exceptions. Ran stress testing. | N/A |
|JoJo Kaler| Ran and reported on spike testing |N/A|
