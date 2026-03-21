# Repository Guidelines

## Project Structure & Module Organization
This repository is a Maven Java 17 project with one tracked module: `yiidii-framework`. Shared domain models, annotations, enums, and utility classes live under `yiidii-framework/src/main/java/cn/yiidii/framework`, alongside Spring Boot configuration, web support, interceptors, aspects, and integration code. Framework resources live in `yiidii-framework/src/main/resources`, including `META-INF/spring.factories` and `i18n/`. `yiidii-demo/` remains a local example app, but it is ignored by Git and is not part of the reactor build.

## Build, Test, and Development Commands
Run commands from the repository root.

- `mvn -q -DskipTests compile`: compile the framework module quickly.
- `mvn -q test`: run the full reactor test phase.
- `mvn -pl yiidii-framework compile`: rebuild the framework module explicitly.
- `mvn clean`: remove generated output and flattened POM artifacts.

Do not commit `target/` output or `.flattened-pom.xml`.

## Coding Style & Naming Conventions
Use 4-space indentation and standard Java naming: `PascalCase` for classes, `camelCase` for methods and fields, and `UPPER_SNAKE_CASE` for constants. Keep packages under `cn.yiidii.*`. Match the existing style: concise Javadoc, one top-level class per file, and Lombok for boilerplate where already established. Name Spring components by role, for example `*Config`, `*Configuration`, `*Interceptor`, `*Aspect`, and `*Handler`.

## Testing Guidelines
`spring-boot-starter-test` is available through the framework module. Add tests under `yiidii-framework/src/test/java` using the same package layout as production code. Name unit tests `*Test`. Cover new framework behavior, especially serialization, configuration, interceptors, and exception handling. Run `mvn -q test` before opening a pull request.

## Commit & Pull Request Guidelines
Recent commits use short, focused subjects such as `actuator`, `数据脱敏`, and `satoken ex处理`. Keep commit messages brief and scoped to one change. Pull requests should summarize behavioral changes, note any dependency updates, and list verification steps. Screenshots are unnecessary unless a change affects an example UI.
