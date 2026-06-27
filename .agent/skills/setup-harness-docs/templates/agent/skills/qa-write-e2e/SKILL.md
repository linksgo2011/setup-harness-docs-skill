---
name: qa-write-e2e
description: Write and run automated tests — backend API integration tests (RestAssured) and frontend E2E tests (Playwright). Use when asked to write or run tests.
---

# QA Write E2E

## Backend API Tests (RestAssured)

```bash
cd backend && mvn test
```

Test classes in `backend/src/test/java/com/consultation/adapter/rest/`:
- `AuthControllerTest` — register, login, duplicate email, wrong password
- `AppointmentControllerTest` — create, list, conflict, cancel
- `AdminControllerTest` — dashboard, users, branches CRUD, data dict, business rules
- `ConsultantControllerTest` — list, slots, invalid consultant
- `UserControllerTest` — profile, update, change password

## Pattern

```java
// TDD red phase: write test first, then implement
// TestBase provides: loginAsUser(), loginAsAdmin(), authHeader()
@Test
void should_create_appointment() {
    String token = loginAsUser();
    // ... RestAssured.given().header(...).post(...)
}
```

## Playwright E2E Tests

```bash
./e2e-run.sh
# or specific file:
./e2e-run.sh tests/app.spec.ts
```

Tests in `e2e/tests/app.spec.ts` cover: public pages, auth, user lifecycle, admin flows, edge cases.

## Write New Test

1. Add test to existing describe block or create new block in `e2e/tests/app.spec.ts`
2. Follow the existing pattern (login first, interact, assert)
3. Run `./e2e-run.sh tests/app.spec.ts` to verify
