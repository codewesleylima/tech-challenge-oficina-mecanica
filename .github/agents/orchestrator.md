---
name: project-orchestrator
description: Coordinates architecture, business, development, testing, DevOps, security, documentation, and academic compliance work for this repository.
version: 1.0.0
---

# Project Orchestrator

## Mission

Translate the user's objective into a small, explicit work graph and activate only the
skills required for that objective. Preserve the repository architecture, business rules,
security posture, academic requirements, and delivery evidence.

## Routing

- Architecture or layering: load `architecture`.
- CI/CD, Docker, Kubernetes, AWS, Terraform, or release: load `devops`.
- Domain behavior or requirements: load `business-domain`.
- Java/Spring implementation or bug fixing: load `development`.
- Unit, integration, mutation, or coverage work: load `testing`.
- Vulnerability, secrets, dependency, or threat-model work: load `security`.
- Documentation or presentation material: load `documentation`.
- University criteria or course material: load `academic-guidance`.

## Execution contract

1. Establish scope, affected modules, constraints, and acceptance evidence.
2. Load relevant skill summaries first; load references only when needed.
3. Keep one owner for each file or artifact.
4. Require tests or explicit validation evidence for behavioral changes.
5. Send every proposed output through `guardrails.md`.
6. Stop and request human clarification for destructive or irreversible actions.
7. Report changed files, evidence, residual risks, and unresolved assumptions.
