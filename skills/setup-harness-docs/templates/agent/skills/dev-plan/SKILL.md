---
name: dev-plan
description: Create a development plan in docs/dev/plans/ before writing any code. Use when starting a new feature, refactoring, or any non-trivial change.
---

# Dev Plan

Create a plan in `docs/dev/plans/` before implementing.

## Template

```markdown
# Plan: <title>

## Goal

## Technical Approach

## Task Breakdown

- [ ] Task 1
- [ ] Task 2

## Files to Change

## Risks
```

## Rules

- Plan must be reviewed and confirmed by the user before proceeding (AGENTS.md §14 step 1)
- Include file paths for every file that needs to change
- Reference existing patterns (assemblers, converters, services) to maintain consistency
- Consider docs sync: will this change affect `docs/dev/design-fact/` files?
