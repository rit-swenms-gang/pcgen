# Mocking & Stubbing - Design Decisions

## Objectives
 - Determinism: Eliminate non-determinism from tests caused by randomness.
 - Isolation: Test decision logic without relying on Java's Random.
 - Coverage: Hit all branches of the attack outcome logic `MISS/HIT/CRIT` with minimal production changes.

## Selected Seam
 - Seam: pcgen.util.DiceRoller (interface) with pcgen.util.DefaultDiceRoller (real implementation).
 - Consumer under test: pcgen.util.AttackCalculator which depends on DiceRoller.
 - Why this seam: Randomness is an external factor like I/O or an API. Abstracting it behind an interface enables mocks/stubs and keeps production behavior intact.

## Alternatives Considered
1. Static/global RNG access (e.g., new Random() inside methods)
    - Hard/impossible to mock cleanly
2. Seeding Random in tests
    - Still leaks non-determinism; seeds can change across JVMs/versions and local time
3. Heavier refactor of existing production code
    - More risk for this assignment's scope, and for future development of this project

**Chosen approach**: tiny DI seam with interface and default implementation. Lowest risk, and high test value

## API & Dependency Injection Choices

Interface:

```java
public interface DiceRoller {
    int roll(int sides);
    default int d20() { return roll(20); }
}
```

Default implementation: DefaultDiceRoller(Random rng) for easy injection or default constructor for normal runtime.

Consumer:

```java
public class AttackCalculator {
    private final DiceRoller dice;
    public AttackCalculator(DiceRoller dice) { this.dice = dice; }
    // attack(...) uses dice.d20()
}
```

Reasoning: Constructor injection is simple, explicit, and test-friendly

## Mocking Strategy

- Library: 
    - Mockito (Added Mockito to the gradle build for this project, JUnit 5 was already included and being used)
- Pattern:
    - Mock DiceRoller, override d20() to return exact values (5, 12, 20)
    - Verify interaction (verify(dice).d20() where relevant)


## New Test Cases & Rationale
- critOnNatural20: Force d20() --> 20; asserts CRIT regardless of bonus (auto-crit rule)
- hitWhenTotalMeetsAC: Force d20() --> 12 with +8 vs AC 20; boundary condition (== AC) --> HIT
- missWhenBelowAC: Force d20() --> 5 with low bonus vs higher AC; ensures MISS path
- stubbedHit (optional): Use FakeDiceRoller(15) stub to demonstrate pure stubbing (no Mockito)

## Test Location & Execution
- Production files:
	- code/src/java/pcgen/util/DiceRoller.java
	- code/src/java/pcgen/util/DefaultDiceRoller.java
	- code/src/java/pcgen/util/AttackCalculator.java
- Unit tests:
	- code/src/utest/org/pcgen/unittesting/mocking/AttackCalculatorTest.java

## Running the Tests

```bash
./gradlew test --tests "org.pcgen.unittesting.mocking.*"
./gradlew jacocoTestReport
```

```bash
open code/build/reports/jacoco/test/html/index.html # optional for viewing JaCoCo report manually
```
