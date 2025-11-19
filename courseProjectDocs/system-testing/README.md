# System Testing README

## Overview

The system tests are located in:
`pcgen/code/src/test/java/system`

## Prerequisites

Before running the tests, ensure the following tools are installed:
- Java 17+
- Gradle Wrapper (included & configured with this project)

## How to Execute System Tests

From the project root run:
```bash
./gradlew build
```

This will build the project, and also run the tests. If the project is already built you can alternatively run:
```bash
./gradlew test
```

Gradle will:
- Compile the project
- Run all unit tests
- Run all system tests
- Produce a test report (via JaCoCo) in `pcgen/build/reports/tests/test/index.html`

The system test should appear with a status `PASSED`


