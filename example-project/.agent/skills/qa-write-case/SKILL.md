---
name: qa-write-case
description: Write manual and smoke test case documents. Use when asked to create test documentation for a feature.
---

# QA Write Case

Write test case documents before writing code (AGENTS.md §14 step 2).

## Locations

| Type | Path |
|------|------|
| E2E test cases (text) | `docs/qa/e2e-cases/` |
| Manual test cases | `docs/qa/manual-cases/manual-test-cases.md` |
| Smoke test cases | `docs/qa/smoking-cases/smoking-test-cases.md` |

## E2E Case Template

```markdown
### TC-<id>: <title>

| Field | Value |
|-------|-------|
| Precondition | |
| Steps | |
| Expected | |
```

## Manual Case Template

```markdown
### TC-M-<id>: <title>

| Project | Content |
|---------|---------|
| Precondition | |
| Steps | 1. ... |
| Expected | - ... |
```

## Rules

- Cover: happy path, error cases, edge cases
- Use seed data (admin@test.com, 3 consultants, 3 branches)
- Each feature spec in `docs/ba/feature/` should have corresponding test cases
