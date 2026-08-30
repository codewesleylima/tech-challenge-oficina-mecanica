---
name: architecture
description: Analyzes and evolves the DDD modular monolith using Clean Architecture and Ports and Adapters principles without violating dependency direction.
version: 1.0.0
---

# Architecture Skill

## Preconditions

- Read the relevant module structure under `src/main/java`.
- Identify domain, application, infrastructure, and presentation boundaries.
- Check existing tests before changing architecture.

## Capability

Assess whether a change belongs to the domain, use cases, adapters, persistence, or HTTP
boundary. Prefer business rules in domain entities/value objects, orchestration in application
use cases, and technical details behind ports and adapters.

## Execution

1. Map the affected bounded context and dependency direction.
2. Preserve domain independence from Spring, JPA, HTTP, and infrastructure.
3. Keep repository contracts in domain/application and implementations in infrastructure.
4. Compare alternatives and identify coupling introduced by the change.
5. Validate with compilation, targeted tests, and architecture evidence.

## References

Use the project `README.md` for the current module map. Do not claim a fully pure Clean
Architecture implementation; describe it accurately as DDD modular monolith with Clean
Architecture and Ports and Adapters influences.
