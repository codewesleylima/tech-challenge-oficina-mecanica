---
name: development
description: Implements focused Java/Spring changes using repository conventions, type safety, explicit error handling, and minimal scope.
version: 1.0.0
---

# Development Skill

## Preconditions

- Read the affected code and its callers.
- Check for existing helpers, patterns, and tests before adding new logic.
- Preserve public API behavior unless the task explicitly changes it.

## Execution

1. Trace the complete request-to-persistence path.
2. Make the smallest coherent change that fixes the root cause.
3. Keep validation and errors consistent with existing application behavior.
4. Avoid broad catches, silent fallbacks, unnecessary casts, and speculative refactors.
5. Add comments only when they clarify non-obvious logic; use JavaDoc for public contracts.
6. Run the smallest existing build and test commands that prove the change.
