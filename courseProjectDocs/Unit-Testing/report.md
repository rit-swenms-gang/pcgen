# Unit Testing I — Report

## Summary

- **Goal:** Extend unit test coverage by exercising uncovered/edge-case logic in version-gated migration rules.
- **Targeted code:** `pcgen.core.system.MigrationRule.changeAppliesToVer(int[])`
- **Framework:** JUnit 5 (project standard).
- **New file:** `src/test/java/pcgen/core/system/MigrationRuleExtendedCoverageTest.java`
- **New tests:** 15 focused cases (see list below).

## New test cases & rationale

All tests instantiate a `MigrationRule` and set explicit wide defaults to avoid null parsing, then narrow windows per case.

1. **`appliesToAll_withWideBounds`**  
   Wide bounds (`0.0.0`..`999.999.999`) should accept representative versions --> sanity baseline.

2. **`minOnly_validsAndInvalids`**  
   Narrow **minVer** only; verify inclusive lower bound and rejections just below.

3. **`maxOnly_validsAndInvalids`**  
   Narrow **maxVer** only; verify inclusive upper bound and rejections just above.

4. **`minDevOnly_validsAndInvalids`**  
   Apply floor only to **dev** series (5.17.7+). Stable series remains governed by stable window and should still pass.

5. **`maxDevOnly_validsAndInvalids`**  
   Cap **dev** path at 6.1.3 and **stable** at 6.0.1 so dev limit is meaningful; verify acceptance at cap and rejection above.

6. **`inclusiveAtMinVer`**  
   Inclusivity at **minVer** (exactly min passes; just below fails).

7. **`inclusiveAtMaxVer`**  
   Inclusivity at **maxVer** (exactly max passes; just above fails).

8. **`inclusiveAtMinDevVer`**  
   Inclusivity at **minDevVer** for dev series (exactly minDev and above pass; just below fails).

9. **`inclusiveAtMaxDevVer`**  
   Inclusivity at **maxDevVer** (6.1.3) with **stable** capped at 6.0.1 so 6.1.x can only pass via dev window; 6.1.4 must fail.

10. **`combinedStableAndDevWindows`**  
    Stable window `[5.16.4..6.0.1]` and dev window `[5.17.7..6.1.3]`.  
    - Versions within both pass.  
    - Stable above 6.0.1 fails.  
    - Dev beyond stable max but ≤6.1.3 still pass (dev overlay).  
    - Dev above 6.1.3 fails.  
    - Stable below 5.16.4 fails.

11. **`conflictingStableBounds_yieldNoMatch`**  
    **minVer > maxVer** ⇒ no version should match.

12. **`conflictingDevBounds_yieldNoMatch`**  
    **minDevVer > maxDevVer** and stable capped ≤6.0.1  
    ⇒ dev path is impossible and stable cannot allow 6.1.x; verify rejections.

13. **`nullArray_throwsNPE`**  
    API should reject `null` version arrays with `NullPointerException`.

14. **`wrongLengthArrays_handledGracefully`**  
    Observed behavior:  
    - Empty array triggers `ArrayIndexOutOfBoundsException` via `compareVersions`.  
    - Length 2 and 4 are tolerated under wide bounds and evaluate truthy.

15. **`veryLargeVersionWithinCeiling`**  
    Ensure very large versions still behave with a wide ceiling; boundary exactness and just-above rejection.

## How to reproduce results

### Running our Metrics

```bash 
cd courseProjectCode/Metrics
python metrics.py
```

Expected output:
-- Maintainability Metrics --
Files scanned: 2736
Total lines: 398017
Total lines of code: 339724
Average LOC per file: 124
Total lines of comments: 111386
Comment density: 0.280

-- Testability --
Tests: 16756  Failures: 0  Errors: 0  Skipped: 20
Line coverage: 26.08%  (covered=137361, missed=389404)

### Running gradle Build With JaCoCo (Optional)

```bash
# From repo root
./gradlew clean test jacocoTestReport
```

Expected output is the JaCoCo report which is also available as a .xml file in `build/reports/jacoco/test/jacocoTestReport.xml`

## Comparing Previous Report

### Previous Report

```bash
-- Maintainability Metrics --
Total lines: 398017
Total lines of code: 339724
Total lines of comments: 111386
Comment density: 0.280

-- Testability --
Tests: 16741  Failures: 0  Errors: 0  Skipped: 20
Line coverage: 26.07%  (covered=137351, missed=389414)
```

### New Report

```bash
-- Maintainability Metrics --
Files scanned: 2736
Total lines: 398017
Total lines of code: 339724
Average LOC per file: 124
Total lines of comments: 111386
Comment density: 0.280

-- Testability --
Tests: 16756  Failures: 0  Errors: 0  Skipped: 20
Line coverage: 26.08%  (covered=137361, missed=389404)
```

### Observation

 - The maintainability metrics remain unchanged, no code complexity or comment density regressions.
 - The testability metrics improved with 15 new test cases and a measureable coverage gain.
 - The increast from 26.07% to 26.08% represents roughly 10 additional lines covered, reflecting successful execution of new edge-case and boundary logic.
 - Improvements were concentrated in the MigrationRule class's version comparison paths, particularly dev/stable overlap handling and invalid input branches

 # Unit Testing II - Report

 ## Summary

 - Goal: Introduce deterministic, isolated unit tests through mocking and stubbing to remove randomness as a source of nondeterminism.
 - Targeted code: pcgen.util.AttackCalculator (newly added) and its dependency pcgen.util.DiceRoller.
 - Frameworks: JUnit 5 + Mockito (Mockito was added to the gradle build by our team).
 - New files:
   - code/src/java/pcgen/util/DiceRoller.java
	- code/src/java/pcgen/util/DefaultDiceRoller.java
	- code/src/java/pcgen/util/AttackCalculator.java
	- code/src/utest/org/pcgen/unittesting/mocking/AttackCalculatorTest.java
- Concept introduced: Replace random dice roles with mockable dependency injection to enable reproducability while testing.

## Tests
- hitWhenTotalMeetsAC
   - Mocks DiceRoller.d20() --> 12. Checks that a roll + bonus ≥ AC registers HIT. Tests inclusive boundary.
- missWhenBelowAC
   - Mocks DiceRoller.d20() --> 5 with low bonus. Ensures branch for roll + bonus < AC → MISS.
- critOnNatural20
   - Mocks DiceRoller.d20() --> 20 and verifies CRIT path regardless of attack bonus.

Each case deterministically triggers one of the three outcomes (MISS, HIT, CRIT) and verifies both behavior and mock interactions.

## Mocking Strategy

- Why mock? Randomness (new Random()) introduces non-repeatable results that make regression tests unreliable.
- How: Refactored randomness behind a new DiceRoller interface and injected it into AttackCalculator.
- Mocks: Mockito is used to force specific d20() returns and verify calls.
- Stub: A lightweight FakeDiceRoller implements the interface with fixed output to demonstrate stub vs. mock.
- Design decision: Keep seam minimal – interface + default implementation – so production behavior is unchanged while testing gains control over randomness.

## How to reproduce results

Running the new tests:

```bash
./gradlew test --tests "org.pcgen.unittesting.mocking.*"
```

Expected output is the JaCoCo report which is also available as a .xml file in `build/reports/jacoco/test/jacocoTestReport.xml`





