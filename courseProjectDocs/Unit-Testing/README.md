# README (Updated for Unit Testing II)

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
   - Mocks DiceRoller.d20() --> 5 with low bonus. Ensures branch for roll + bonus < AC --> MISS.
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
