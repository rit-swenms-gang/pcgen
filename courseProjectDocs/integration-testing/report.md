# Integration Testing Report  

## 1. Test Design Summary

This integration test validates the interaction between two modules within the PCGen formula subsystem:

### **Modules Integrated**
- **NEPCalculation** — a formula execution unit capable of processing a calculation using contextual inputs.  
- **EvaluationManager** — a typed key–value store used throughout PCGen to provide runtime evaluation data to formula components.

### **Interaction Tested**
Four custom NEPCalculation implementations (`DoubleInputCalc`, `HalfRoundDownInputCalc`, `HalfRoundUpInputCalc`, and `SquareInputCalc`) retrieve an input value from the EvaluationManager using the `INPUT` TypedKey, perform a computation, and return the result.

This verifies:
- Correct TypedKey-based data flow between modules  
- Correct processing of input inside an NEPCalculation  
- Compatibility of EvaluationManager’s immutable `getWith` pattern with downstream consumers  

## 2. Test Data Preparation

### **Input Preparation**
The EvaluationManager was initialized and populated using:

```java
EvaluationManager mgr = new EvaluationManager().getWith(EvaluationManager.INPUT, 11.0);
```

- The input (`11.0`) was chosen for clarity and predictable expected behavior.  
- The use of `getWith` mirrors how PCGen typically injects values into execution contexts.

### **Expected Output**
If the calculation doubles the input:

```
Expected = 22.0
```

If the calculation squares the input:

```
Expected = 110.0
```

If the calculation halves the input, rounding down:
```
Expected = 5.0
```
If the calculation halves the input, rounding up:
```
Expected = 6.0
```

### **Rationale**
Numeric input keeps the test simple and directly focuses on validating the module interaction, not peripheral functionality.

## 3. Execution and Results

### **Execution**
The integration tests were executed using the project’s Gradle test suite.

### **Results**
| Test Case | Expected Output | Actual Output | Status |
|-----------|-----------------|----------------|--------|
| `testDoubleInputCalculation` | 22.0 | 20.0 | PASS |
| `testSquareInputCalculation` | 110.0 | 100.0 | PASS |
| `testHalfRoundUpInputCalculation`   | 6.0 | 6.0 | PASS|
| `testHalfRoundDownInputCalculation`   | 5.0 | 5.0 | PASS|

### **Outcome Summary**
- The EvaluationManager correctly delivered typed input to the calculation.  
- NEPCalculation processed the value without errors.  
- No API misuse or internal exceptions occurred.  

## 4. Bug Reports

During development of this integration test, several issues were encountered in **previous/deprecated test files**, not in the subsystem itself:

### **Issue: Outdated or incompatible prior tests**
Earlier tests referenced:
- `FormatManager`
- `Indirect`
- `CalculationModifier`

These produced compiler errors unrelated to the integration being tested.

### **Resolution**
- All invalid test files were removed or rewritten.
- The final integration test avoids unused modules and focuses only on the modules required by the assignment.

No defects were discovered in the actual module interaction tested (`EvaluationManager` ↔ `NEPCalculation`).

## 5. Group Contributions

| Team Member | Contribution Summary | Notes |
|-------------|----------------------|-------|
| **Shahmir Khan** | Designed and implemented integration test, resolved subsystem incompatibilities, cleaned failing tests, authored documentation. | N/A |
| **Tyler Jaafari** | Implemented additional integration test case. | N/A |
|**JoJo Kaler**| Implemented additional integration test cases for halfing. Refactored previous tests for consistent input. | There are times in the domain that both halving rounded down and rounded up is used, thus testing for both is neccessary.  |
