# Integration Testing README

## Overview

Integration testing verifies that multiple modules within a software system work correctly *together* rather than in isolation.

In this project, our tests focus on validating interactions inside the PCGen formula subsystem—specifically ensuring that data passed through the `EvaluationManager` is properly consumed and processed by a `NEPCalculation` module.

Integration testing helps ensure:
- Modules communicate correctly  
- Data flows through interfaces as expected  
- No unexpected errors occur when subsystems interact  
- Defects introduced by combining components are detected early  

## How to Run the Integration Tests

These tests run using the standard Gradle build process.

### **To compile and execute all tests (including integration tests):**
```bash
./gradlew build
```

This will:
- Compile all source code
- Compile all tests
- Run the entire test suite (unit + integration)
- Produce a JaCoCo .html report

## Test Location

Integration tests are located under:
`pcgen/code/src/utest/pcgen/base/calculation`

