# Mutation Testing Setup

## Introduction
This assignment focused on introducing **mutation testing** into our PCGen codebase to evaluate the quality and effectiveness of existing unit tests.  
Before this work, the project did not have any mutation testing configured. We integrated **PIT (Pitest)** with Gradle, ran an initial baseline analysis, and then added new targeted tests to improve coverage and kill surviving mutants.  

Our selected target files were:
`pcgen/cdom/converter/AddFilterConverter.java`

## How to Run Mutation Testing
1. Run the PIT analysis
```bash
./gradlew pitest
```

2. View the generated report:
Open the HTML report located at:
```gradle
build/reports/pitest/index.html
```

3. Run the regular test suite (optional)
```bash
./gradlew test
```

## Report Location

Full mutation testing report (HTML format):
```bash
build/reports/pitest/index.html
```
Mutation testing documenation and results summary:
```bash
courseProjectDocs/mutation-testing/report.md
```


