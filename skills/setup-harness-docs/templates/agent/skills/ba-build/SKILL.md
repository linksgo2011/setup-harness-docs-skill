---
name: ba-build
description: Create or update business analysis documents from code and requirements. Use when asked to document features, business facts, or domain knowledge.
---

# BA Build

Write business analysis documents based on the codebase.

## Sources

| Doc | Source |
|-----|--------|
| `docs/ba/feature/<name>.md` | Feature requests, user stories, domain aggregates, controllers |
| `docs/ba/business-fact/booking-flow.md` | Domain services, state machine, business rules, seed data |
| `docs/ba/business-fact/domain-model.md` | Aggregates, VOs, repositories, domain services |
| `docs/ba/chore/` | Meeting transcripts (manual input, cannot reverse-engineer) |
| `docs/ba/prototype/` | Frontend views, routes, UI flow (manual input or screenshots) |

## Conventions

- Write in Chinese for business-facing docs
- Include tables for entity fields, enum values, and state transitions
- Reference concrete code paths to keep docs traceable
