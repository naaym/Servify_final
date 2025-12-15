# Servify

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 20.0.3.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.

## Onboarding and delivery practices

New to the project? Start with [docs/PROJECT_ONBOARDING.md](docs/PROJECT_ONBOARDING.md) for expectations on architecture, secure coding, testing, and how we collaborate as a Scrum team.

## Backend (Spring Boot)

The `/backend` directory contains a Spring Boot monolith structured by feature (e.g., `client`, `provider`). To run it locally:

```bash
cd backend
mvn spring-boot:run
```

The application starts on port `8084` and uses a MySQL database named `servify`. Before running the backend, create the database and a user (defaults below) or export environment variables to override the connection:

```sql
CREATE DATABASE servify;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON servify.* TO 'root'@'localhost';
```

Connection defaults (override with `MYSQL_HOST`, `MYSQL_PORT`, `MYSQL_USER`, and `MYSQL_PASSWORD`):

- URL: `jdbc:mysql://localhost:3306/servify?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
- Username: `root`
- Password: `root`

Key endpoints:

- `POST /api/auth/login` – authenticate clients and providers. Returns `access_token`, `id`, `role`, `status`, and `message` for routing the Angular app.
- `POST /api/clients/register` – JSON payload to register a client.
- `POST /api/providers/register` – `multipart/form-data` payload to register a provider with CIN, CV, and diploma file uploads. Providers start in `PENDING` status.

CORS is enabled for `http://localhost:4200` by default. Update `app.cors.allowed-origins` in `backend/src/main/resources/application.properties` to allow additional front-end hosts during development.

Multipart uploads are limited to 20 MB per file (60 MB per request) by default. Adjust `spring.servlet.multipart.max-file-size` and `spring.servlet.multipart.max-request-size` in `application.properties` if your documents are larger.
