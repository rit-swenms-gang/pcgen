# Security Testing Report  

## 1. Test Scope and Coverage

We selected **FindSecBugs**, a security-focused extension of SpotBugs, as our tool. This choice was appropriate because:

- PCGen is a Java desktop application (not a web service), so static analysis is the most relevant testing technique.
- FindSecBugs specifically targets Java security vulnerabilities and code patterns.
- It integrates seamlessly with Gradle and requires no infrastructure or API keys.

**Vulnerability types targeted:**
- Insecure object exposure / encapsulation leaks  
- Predictable pseudo-random number generation  
- Reflection misuse  
- Improper initialization  
- Potential null-dereference risks  
- Unicode normalization issues  
- Singleton initialization problems  
- Serialization / deserialization hazards  
- Data integrity issues through shared mutable state  

The scan covered the **entire PCGen codebase**, including:
- Core engine
- UI components
- Plugin loader
- Persistence utilities
- Data-driven rule-processing modules

No parts of the project were excluded.

## 2. Vulnerability Summary

Below is a summarized table of representative findings from the FindSecBugs report (each member contributed at least one):

| Title | Type | Severity | Recommended Fix |
|-------|------|----------|------------------|
| Exposure of Internal Representation (EI/EI2) | Encapsulation / Data Exposure | Medium | Return defensive copies instead of mutable references; use immutable wrappers. |
| Predictable Random Number Generator (SECPR) | Cryptographic Weakness (Non-security use) | Low | If used for security (not applicable here), replace with SecureRandom. |
| Partially Constructed Object (CT) | Object Initialization Risk | Medium | Add guards to constructors and avoid throwing before full initialization. |
| Lazy Singleton Initialization (SING) | Concurrency / Thread Safety | Medium | Use synchronized initialization or enum-based singletons. |
| Reflection Access (REFLC) | Reflection Misuse | Medium | Verify reflective access is limited to internal classes only (it is). |
| Unicode Normalization Issues (SECUNI) | Input Validation | Low | Apply `Normalizer` when case-folding strings that originate from user input. |
| Potential Null Pointer Dereference (NP) | Stability | Low | Add null checks or refactor flow; PCGen sometimes intentionally uses null as a signal. |
| equals()/hashCode() Inconsistency (EQ) | Integrity / Object Identity | Low | Regenerate equals/hashCode via IDE; requires structural refactor. |
| Exceptions Thrown in Constructors (OBJ-11) | Finalizer Attack Vulnerability | Medium | Attacks can be prevented by declaring relevant classes as final or by using private constructors. |
| Overridable Method Call Through clone() (MET06-J) | Injection / Override Risk | Medium | Only static, final, or private methods should be invoked by the clone() method. |
| Mutable Static Fields | Integrity / Object Identity | Medium | Storing copies of mutable objects stored in static fields is a potential fix. |
| References to Mutable Objects | Integrity / Object Identity | Medium | Storing copies of mutable objects is a better approach in most cases. |

### Team Member Contributions
| **Member** | **Task / Contribution** | **Notes** |
| ----- | ----- | ----- | 
| Shahmir Khan | Integrated FindSecBugs into the Gradle build, executed security scan, and documented the “Exposure of Internal Representation (EI/EI2)” vulnerabilities. | Verified that these issues stem from legacy design patterns; fixes would require large-scale architectural changes. |
| Tyler Jaafari | Ran FindSecBugs and documented vulnerabilities on constructor exceptions, overridable method calls, and mutable fields/objects. | N/A |

## 3. Results

### Tool Used
**FindSecBugs** (SpotBugs security extension)
- Version: `findsecbugs-plugin:1.12.0`
- Integrated via Gradle SpotBugs task

### Results

We configured SpotBugs to output a report under `pcgen/build/reports/spotbugs/main.html` and `pcgen/build/reports/spotbugs/main.txt`