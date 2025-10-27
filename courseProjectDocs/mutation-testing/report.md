# Mutation Testing Report

## Overview
This report documents the setup, execution, and results of mutation testing for the **PCGen** project using **PIT (Pitest)**, a mutation testing tool for Java.  

Mutation testing evaluates the effectiveness of unit tests by introducing small code mutations and checking whether existing tests detect them (“kill” the mutants). Surviving mutants highlight potential gaps in test coverage or logical conditions not exercised by the current suite.

---

## Tool and Configuration

**Tool Used:** PIT (Pitest)  
**Version:** 1.15.0  
**Build Tool Integration:** Gradle (`info.solidsoft.pitest` plugin)  
**Report Location:** `build/reports/pitest/index.html`  

**Gradle Configuration Snippet:**
```gradle
pitest {
    pitestVersion = '1.15.0'
    targetClasses = ['pcgen.*']
    targetTests = ['pcgen.*']
    outputFormats = ['HTML']
    threads = 4
    junit5PluginVersion = '1.2.0'
}
```

## Component Under Test

- File: pcgen/cdom/converter/AddFilterConverter.java
- Purpose: The AddFilterConverter class acts as a wrapper for another Converter and PrimitiveFilter. It combines filter logic, allowing conditional transformations of objects in ObjectContainers. It also defines equality and hash code behavior.

- File: pcgen/core/character/SpellBook.java
- Purpose: The SpellBook class tracks a certain set of actions (spells) available to a player character. Almost every attribute of a SpellBook can be modified, and a SpellBook instance can be cloned.

This file had 0% line coverage and 0% mutation coverage initially.

## Initial Results (Before Adding Tests)

| **Metric** | **Value** |
| ----- | ----- |
| Line Coverage | 0 / 38 (0%) |
| Mutation Coverage | 0 / 26 (0%) |
| Test Strength | 0% |
| Mutants Killed | 0 |
| Mutants Survived | 26 |

**Observations**:
- The class was completely untested
- Surviving mutants involved return replacements (Collections.emptyList()), negated conditionals, and forced boolean returns in equals() and hashCode()

## Tests Added

New test suites were added at:
code/src/utest/pcgen/cdom/converter/AddFilterConverterTest.java
code/src/utest/pcgen/core/character/SpellBookTest.java

**Key Additions**:
- Created stub implementations for Converter, PrimitiveFilter, and ObjectContainer
- Wrote unit tests that covered:
    - convert(ObjectContainer) and convert(ObjectContainer, PrimitiveFilter) paths
    - Compound filter logic indirectly through filter disagreements
    - Equality and hashCode consistency
    - Null and non-object comparisons

**Added Test Methods**:
- AddFilterConverterTest
    - testConvertWithoutLimits()
    - testConvertWithLimit()
    - testEqualsAndHashCode()
    - testNotEqualsToOtherObject()

- SpellBookTest
    - testSetEquip()
    - testToString()
    - testIdenticalBooksEqual()
    - testClone()

These tests specifically targeted previously surviving mutants in return statements and conditionals.

## Final Results (After Adding Tests)

| **Metric** | **Value** |
| ----- | ----- |
| Line Coverage | 17 / 38 (45%) |
| Mutation Coverage | 10 / 26 (36%) |
| Test Strength | 83% |
| Mutants Killed | 10 |
| Mutants Survived | 12 |
| Mutants Timed Out | 0 |

**Global Project Change**:
| **Metric** | **Before** | **After** | 
| ----- | ----- | ----- |
| Total Mutations Killed | 1613 | 1623 |
| Total Mutations | 40240 | 40240 |
| Overall Mutation Coverage | 4% | 4% |
| Total Test Strength | 68% | 68% |
| Project Line Coverage | 8% | 8% |

(Values taken from the before-and-after PIT reports in build/reports/pitest/index.html)

## Mutant Analysis

| **Category** | **Description** | **Status** |
| ----- | ----- | ----- |
| Return Replacement | Mutants that replaced return converter.convert(orig, filter) with Collections.emptyList() | Killed |
| Boolean Conditional | Mutants that negated conditions in equals() and allow() | Killed |
Forced Boolean Returns | Mutants that replaced returns with true or false | Killed |
| Null Return | Mutants replacing normal return with null | Survived (partially untested code paths in equals/hashCode) |
| Conditional Boundary | Mutants that changed logical operators in filter equality comparison | Survived (indirect logic not fully exercised) |

**Summary**:
The newly added test suite successfully validated the filter logic and equality/hash consistency, killing most mutants associated with control flow and return paths. Some survived mutants remain in rarely executed conditions inside nested private structures.

**Lessons Learned**:
- PIT revealed untested paths that were invisible under regular code coverage (JaCoCo)
- Even small, isolated utility classes can contribute many mutants due to overloaded equals/hash behavior
- Mutation testing is more effective when paired with precise, boundary-oriented unit tests rather than broad ones

## Group Contributions

| **Member** | **Task / Contribution** | **Notes** |
| ----- | ----- | ----- | 
| Shahmir M. Khan | Added, and configured, PIT in Gradle (for the mutation testing requirements), ran initial and final mutation tests (for `AddFilterConverter.java`), and prepared full mutation-testing documentation. | Improved coverage of pcgen.cdom.converter package from 0% to 45% line coverage and 0% to 38% mutation coverage. |
| Tyler Jaafari | Added SpellBookTest.java and corresponding documentation. | For some reason PITest was not working for me. I'll have to try it on another machine in order to actually gather metrics. |

