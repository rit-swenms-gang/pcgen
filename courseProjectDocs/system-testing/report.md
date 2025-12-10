# System Testing

This system testing assignment evaluates the end-to-end workflow of the PCGen Formula Subsystem.

Our black-box system tests focused on a subsystem that:
- Has public APIs
- Was actively modfied during this project
- Can be executed and validated programmatically
- Supports realistic end-to-end workflows (i.e. computing character damage/stats/etc.)
- Is testable without inspecting internal structure (just calling the formulaic methods)

## System Workflow Under Test

The workflow validated by out system is:

1. Interpret numeric input values through the FormatManager
2. Apply a sequence of arithmetic formulas (addition, subtraction, multiplication, division)
3. Chain multiple formula transofmrations to simulate PCGen's calculation process
4. Produce a final, resolved output consistent with expected PC-level stat computation

This workflow represents how PCGen would calculate character attributes (STR, DEX, HP, bonuses, penalties) during character sheet processing, but in a simplified, subsystem-level manner.

## Coverage

These tests cover:
- Formula interpretation
- AddingFormula behavior
- SubtractingFormula behavior
- MultiplyingFormula behavior
- DividingFormula behavior
- Multi-step chained formula evaluation
- Public API usage (no internal structure needed to be looked at as per black-box testing)
- End-to-end processing of a real user-directed calculation pipeline

This achieves complete functional coverage for the subsystem under our ownership.

## Test Case Summary

| **Test Case** | **Title** | **Pre-conditions** | **Test Steps** | **Expected Result** |
| ----- | ----- | ----- | ----- | ----- |
| 01 | testFullFormulaWorkflow() | PCGen builds successfully; formula subsystem is available; `./gradlew test` runs properly. | Create base input value; apply add; apply subtract; apply divide; capture final computed result | Final output = `6` |
| 02 | testMultipleBooks() | PCGen builds successfully; formula subsystem is available; `./gradlew test ` runs properly. | Create two spell books, add two spells to one, and one to the other, confirm that both spellbooks are differentiated and accurate. | Final output = `true` |
| 03 | testBasicCharacter | PCGen build successfully, character generation works, `./gradlew test ` runs properly.| Create a base character and add basic information to it to test the character generation is running properly. | Final output = `true` |

## Detailed Test Case Descriptions

**Test Case 01**

Title:
End-to-End Arithmetic Workflow Using Formula Subsystem

Pre-conditions:
- The PCGen project compiles (./gradlew build)
- Formula subsystem classes are available from pcgen.base.format and pcgen.base.formula
- No internal classes or states are accessed; only public APIs are used

Test Steps:
1.	Initialize the FormatManager NumberManager to handle numeric input
2.	Define a base numeric value (10)
3.	Create an AddingFormula(5) to represent a bonus
4.	Create a SubtractingFormula(3) to represent a penalty
5.	Create a MultiplyingFormula(2) to represent a strength multiplier
6.	Create a DividingFormula(4) to represent a final reduction factor
7.	Apply all formulas in order, each taking the result of the previous step
8.	Store the final computed result
9.	Assert that the result equals 6

Expected Result:
- Final output: `6`
This confirms the entire multi-step workflow resolves correctly

## Execution and Results

Execution method:
`./gradlew test`

**Observed Output**
- Test passed successfully with no compilation issues or runtime failures
- The printed/logged result matched the expected value of 6

## Group Contributions

| **Team Member**| **Contribution Summary** | **Notes** |
|-------------|----------------------|-------|
| **Shahmir Khan** | Implemented the full system-level test case (01). Ensured the test compiled with black-box requirements. Performed Gradle configuration corrections to ensure the subsystem compiled. Troubleshot missing PCGen-base/PCGen-formula module dependencies. Authored documentation. | N/A |
| **Tyler Jaafari** | Implemented minor system-level test case (02). Ensured the test compiled with black-box requirements. Performed Gradle build to ensure system compiled. Updated documentation. | N/A |
| **JoJo Kaler** | Implemented system level test case for character generation (03). Ensured test compiled with black blox requirements. Successfully built project and updated documentation |