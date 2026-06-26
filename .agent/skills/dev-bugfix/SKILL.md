---
name: dev-bugfix
description: Fix bugs by reproducing, diagnosing, and applying minimal fixes. Use when the user reports a bug or test failure.
---

# Dev Bugfix

## Process

1. **Reproduce** — Run the failing test or reproduce manually
2. **Diagnose** — Identify root cause (check logs, console, network)
3. **Fix** — Apply minimal, targeted fix
4. **Verify** — Confirm fix resolves the issue without breaking other tests

## Common Bug Sources in This Project

| Layer | Common Issues |
|-------|---------------|
| Controller | Missing `@Valid`, wrong response type, incorrect path |
| Application | Missing `@Transactional`, wrong domain method call |
| Domain | State guard too strict/loose, missing invariant check |
| Infrastructure | SQL syntax, wrong MyBatis-Plus mapping, missing index |
| Frontend | Wrong API path, missing reactive update, incorrect route guard |
| E2E | Selector mismatch, timing issues, missing `waitFor` |

## Verify Fix

```bash
cd backend && mvn test
./e2e-run.sh tests/app.spec.ts
```
