---
name: project-guardrails
description: Reviews agent plans, code changes, commands, and final outputs for quality, security, architecture, academic, and repository compliance.
version: 1.0.0
---

# Project Guard Rails

## Mandatory review

Review every agent output before it is presented or applied. Reject or return for correction
anything that:

- exposes credentials, tokens, personal data, or secrets;
- proposes destructive commands without explicit confirmation;
- violates the dependency direction documented by the architecture skill;
- changes behavior without targeted validation;
- invents test results, deployment status, or academic requirements;
- leaves temporary files, debug output, copied prompt artifacts, or accidental comments;
- adds comments that are not required clarification or JavaDoc/documentation;
- ignores existing repository conventions or changes unrelated files.

## Quality gates

- Target at least 95% line coverage for production code where the project rubric requires it.
- Treat 95% as a measurable acceptance criterion, never as an unverified claim.
- Require coverage evidence from configured test and reporting tools before claiming compliance.
- Require targeted tests to pass; document unrelated pre-existing failures separately.
- Check that CI/CD names, image references, namespaces, and Kubernetes resource names agree.
- Ensure PDFs and other third-party academic materials remain untracked unless distribution
  rights are explicit.

## Output format

Return: decision, findings, evidence, required corrections, and residual risks.
