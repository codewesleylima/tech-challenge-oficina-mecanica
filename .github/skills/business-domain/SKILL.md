---
name: business-domain
description: Preserves the oficina mechanics business rules across customers, vehicles, inventory, budgets, service orders, statuses, and notifications.
version: 1.0.0
---

# Business Domain Skill

## Capability

Reason about the workshop workflow and its bounded contexts without moving business rules
into controllers or infrastructure.

## Execution

1. Identify the affected aggregate and state transition.
2. Check invariants, authorization, validation, and domain events.
3. Trace persistence and notification consequences.
4. Add or update focused tests for the business rule.
5. Report incompatible assumptions explicitly.

## Core flow

`RECEIVED -> IN_DIAGNOSIS -> AWAITING_APPROVAL -> IN_EXECUTION -> FINALIZED -> DELIVERED`

Budget rejection leads to `CANCELED`, a terminal state.
