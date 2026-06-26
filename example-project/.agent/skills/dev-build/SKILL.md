---
name: dev-build
description: Implement features following the four-layer DDD architecture. Use when writing backend or frontend code for the consultation booking platform.
---

# Dev Build

Write code following the architecture conventions in AGENTS.md.

## Backend

- **adapter/rest/** — Controllers: protocol conversion only, no business logic
- **application/service/** — AppServices: use case orchestration, `@Transactional`
- **domain/** — Aggregates, VOs, domain services: business invariants
- **infrastructure/** — Persistence, security, config

Layers dependency: adapter → application → domain ← infrastructure

## Frontend

- Vue 3 Composition API + `<script setup>`
- Pinia store for auth state in `src/stores/auth.ts`
- Views under `src/views/user/` or `src/views/admin/`
- Axios interceptors handle JWT and 401 redirect

## Verify

```bash
cd backend && mvn compile
cd frontend && npm run build
```
