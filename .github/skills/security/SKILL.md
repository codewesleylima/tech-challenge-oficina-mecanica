---
name: security
description: Reviews secrets, authentication, authorization, dependencies, containers, Kubernetes, and OWASP risks with evidence-based findings.
version: 1.0.0
---

# Security Skill

## Execution

1. Search for hardcoded credentials, tokens, unsafe defaults, and secret leakage.
2. Review JWT, authorization, input validation, and error handling.
3. Review Docker and Kubernetes privilege, image, and secret handling.
4. Use the existing dependency and vulnerability reports as references.
5. Report severity, confidence, file, lines, impact, and remediation.

## Rules

- Do not paste secret values into output.
- Do not weaken controls to make CI green.
- Do not treat a report as proof that no vulnerabilities exist.
