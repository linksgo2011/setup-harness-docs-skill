---
name: ba-review
description: Review business analysis documents for completeness, consistency, and traceability to the codebase. Use when asked to review features, business facts, or requirements.
---

# BA Review

Review business analysis documents in `docs/ba/`.

## Checklist

- [ ] Feature specs (`docs/ba/feature/`) clearly describe the user-facing behavior
- [ ] Business facts (`docs/ba/business-fact/`) align with the actual domain model and domain services
- [ ] All states and transitions in the domain are documented
- [ ] Business rules referenced in docs match `domain/businessrule/` enums and YAML config
- [ ] Seed data is consistent with what's documented

## Cross-reference

| Docs | Code |
|------|------|
| `docs/ba/feature/` | Controllers, application services |
| `docs/ba/business-fact/` | Domain aggregates, business rules, state machine |
| `docs/ba/prototype/` | Frontend views and routes |
