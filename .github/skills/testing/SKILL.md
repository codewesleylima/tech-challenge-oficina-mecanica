---
name: testing
description: Designs and runs unit, integration, regression, mutation, and coverage validation for the Java/Spring project and its CI pipeline.
version: 1.0.0
---

# Testing Skill

## Preconditions

- Identify existing Gradle tasks and test conventions.
- Inspect affected production code and adjacent tests.
- Do not add a new testing framework unless required.

## Execution

1. Start with the smallest targeted test.
2. Include integration tests when persistence, security, events, or HTTP wiring changes.
3. Use existing coverage or mutation tooling when available.
4. Verify the configured threshold with an actual report.
5. Never claim 95% coverage without measured evidence.
