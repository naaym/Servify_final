# Servify Project Onboarding & Working Agreement

This guide summarizes how we build and operate the Servify monolith (Spring Boot backend + Angular frontend) and how we collaborate as a Scrum team. It is meant to be a concise reference for a new developer’s first project.

## Product context

- **Users**: clients (service consumers), providers (service owners), admins, and super admins.
- **Platform goal**: let clients request services, while providers manage their offerings and admins govern quality and compliance.
- **Architecture**: single deployable Spring Boot monolith with a modular codebase (feature-first packages) and Angular SPA for the UI.

## Engineering principles

- Prefer **feature-first structure**: group controllers/services/repositories/DTOs by domain (e.g., `client`, `provider`, `admin`).
- **API-first mindset**: design REST endpoints around resources; use DTOs to avoid leaking entities and to control payloads.
- **Guard rails by default**: enable validation, null checks, and clear error responses before coding happy paths.
- **Automate** repeatable tasks (testing, formatters, checks) and document any manual steps that remain.

## Backend (Spring Boot) guidelines

- **Package layout**: keep controllers, services, repositories, models, DTOs, mappers, and exceptions inside each feature package (see existing `client`, `provider`, and `admin` modules).
- **Controller best practices**: return DTOs; never expose JPA entities directly. Annotate endpoints with method-level validation (`@Valid`), clear status codes, and consistent response bodies.
- **Service layer**: enforce business rules, idempotency, and transactional safety (`@Transactional` where multiple writes happen). Keep controllers thin.
- **Persistence**: prefer Spring Data repositories; avoid cascade pitfalls by specifying `fetch` and cascade rules explicitly. Seed data via migrations (Flyway/Liquibase) when possible rather than manual SQL.
- **Security**: use JWT for stateless auth; apply role-based authorization (client/provider/admin/super-admin) at controller or method level. Hash passwords with BCrypt, validate file uploads (type/size), and never log sensitive data or tokens.
- **Validation & errors**: validate input DTOs and use global exception handlers for consistent error shapes. Avoid throwing generic exceptions.
- **Mapping**: keep mappers in each feature; consider MapStruct or explicit mapping methods to avoid leaking domain objects.
- **Observability**: log with structured messages and correlation IDs per request; capture key business events (registrations, approvals) at INFO level.

## Frontend (Angular) guidelines

- **Project layout**: structure by feature modules (e.g., `auth`, `client`, `provider`, `admin`) with shared UI components and guards in `shared/`.
- **State & services**: keep HTTP calls in services; use interceptors for JWT injection and error handling. Prefer reactive forms with strong typing.
- **Change detection**: use `OnPush` where possible and favor pure pipes/components to keep the UI fast.
- **Styling**: follow design tokens and utility-first CSS (Tailwind is available). Keep component CSS scoped and avoid deep selectors.
- **Routing & guards**: protect routes per role (client/provider/admin/super-admin) and handle pending/disabled provider states at navigation time.
- **Accessibility**: ensure form labels, focus management, and keyboard navigation are handled for all critical flows.
- **Testing**: write component tests with Karma/Jasmine; mock HTTP services; keep fixtures in feature folders.

## API and contract considerations

- **Versioning**: prefer URI versioning (`/api/v1/...`) when introducing breaking changes.
- **Consistency**: standardize pagination, sorting, and filtering query params across controllers; document required/optional fields.
- **Files & uploads**: set explicit size/type limits; scan or validate MIME types; store references, not binaries, in the database.
- **DTOs**: create separate request/response DTOs when fields differ; avoid over-posting by whitelisting fields.

## Security checklist

- Enforce HTTPS in production; terminate TLS at the ingress/reverse proxy.
- Store secrets in environment variables or a secrets manager; never commit them.
- Use CSRF protection for browser-facing sessions (JWT flows should use `Authorization` headers, not cookies).
- Implement rate limiting and account lockout/backoff on authentication endpoints.
- Audit trails: record who did what (admin approvals, provider updates) with timestamps and IDs.

## Documentation expectations

- Keep **README** and feature-level `README`/ADR notes up to date when adding endpoints or flows.
- Document API contracts (request/response examples) and authorization rules close to the code (e.g., `controller` package README or Swagger/OpenAPI docs).
- Add in-code comments sparingly for non-obvious business rules; prefer self-describing names and tests.

## Testing strategy

- **Unit tests**: services and mappers with happy-path and edge cases; use builders/fixtures for readability.
- **Integration tests**: controller tests with mocked slices or Testcontainers-backed database to verify wiring and security.
- **Frontend tests**: component and service tests with mocks; add accessibility checks for key pages.
- **Pipelines**: aim for fast checks (<10 minutes). Include lint/format, unit tests, and minimal integration tests per PR.

## Collaboration & workflow (Scrum/Kanban)

- **Ceremonies**: participate in daily standups with concise updates (yesterday/today/blocks). Use refinement to clarify acceptance criteria and definition of done (DoD). Retros are for actionable improvements.
- **Board hygiene**: keep tickets small, with clear acceptance criteria and test notes. Limit work in progress; move cards only when the state truly changes.
- **Branching**: use short-lived feature branches (`feature/<topic>`); rebase on main frequently. Small PRs (<300 lines) speed up reviews.
- **Commits & PRs**: write meaningful commit messages, include context and rationale in PR descriptions, and list test evidence. Request reviews early and respond to feedback with humility and clarity.
- **Pairing & reviews**: pair or mob on risky changes; review for correctness, security, and readability. Ask questions instead of assuming intent.

## Working with roles & stakeholders

- **Clients**: prioritize reliability and clarity; communicate outages or breaking changes early.
- **Providers**: ensure onboarding (document uploads, verification) is reliable and well-validated; provide clear status messaging (pending/approved/rejected).
- **Admins/Super admins**: expose governance tools (approvals, user suspension, audit logs) with strong authorization checks and clear UI affordances.
- **Team members**: over-communicate when blocked; create concise tickets for bugs or tech debt you uncover; respect code ownership but avoid silos through pairing and rotation.

## Delivery checklist for new features

1. Confirm acceptance criteria and security requirements (roles, rate limits, data retention).
2. Design API contract and UI states (loading, empty, error) before coding; share mocks if needed.
3. Implement backend with validation, DTOs, authorization, and logging; add tests.
4. Implement frontend with services, guards, and accessible UI; add tests and error handling.
5. Update documentation (README, API docs) and record test evidence in the PR.
6. Demo the feature during review and capture follow-up tasks in the board.

Use this document as a living guide—update it when practices change or when new patterns emerge.
