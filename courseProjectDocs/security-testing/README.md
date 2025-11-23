# Security Testing README  
PCGen – SWEN 777

This README explains how to execute the security testing workflow used for the PCGen project.

## Overview
We use **FindSecBugs**, a security-focused plugin for SpotBugs, to identify security weaknesses and vulnerable code patterns in the Java codebase. The tool performs static analysis and produces a detailed HTML report.

## Prerequisites
- JDK 17+
- Gradle installed (wrapper included)
- PCGen source code checked out

No internet access or API keys are required.

## Running the Security Tests

### Ensure FindSecBugs Plugin Is Installed
In `build.gradle`:

```gradle
dependencies {
    spotbugsPlugins "com.h3xstream.findsecbugs:findsecbugs-plugin:1.12.0"
}
```

### Run the Scan

```bash
./gradlew spotbugsMain
```

### View the Results

Open the HTML report under:
`pcgen/build/reports/spotbugs/main.html` or `pcgen/build/reports/spotbugs/main.txt`

## Reproducing Results
All steps above can be repeated by any team member or grader using the repo as-is, as no external configuration or API keys were used.

## Notes

- This scan covers 100% of the PCGen Java codebase
- The tool is static only; no dynamic runtime analysis is performed
