# selenium-ai-demo

A Selenium test automation demo project built with AI assistance using [Claude Code](https://claude.ai/code). This project serves as a practical example of how to use AI to write, improve, and optimize test automation code — suitable for presentations, blog posts, and meetups.

## What This Project Demonstrates

- Using **Claude Code** to generate and iterate on Selenium page objects and test classes
- Applying the **Page Object Model** pattern in a real project
- Integrating **Allure** for rich test reporting
- Running tests in **parallel** with thread-safe driver management
- Using AI to inspect live pages and extract real locators

## Target Application

Tests run against [practicetestautomation.com](https://practicetestautomation.com) — a free public site designed for QA automation practice. Three pages are covered:

| Page | What's Tested |
|------|--------------|
| Login form | Valid login, invalid credentials |
| Exceptions exercise | Dynamic elements, visibility waits |
| Course table | Filtering, sorting, row counts |

## Project Structure

```
src/
  main/java/com/example/
    config/         # ConfigReader — reads config.properties
    pages/          # Page object classes (BasePage + one per page)
  test/java/com/example/
    tests/          # Test classes (BaseTest + one per page)
  test/resources/
    config.properties
    testng.xml
```

## Running Tests

```bash
# Run all tests
mvn test

# Run headless (no browser window)
mvn test -Dheadless=true

# Run with a different browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge

# Generate and open Allure report
mvn allure:serve
```

## Key Design Decisions

- **ThreadLocal WebDriver** — driver is per-thread, enabling safe parallel execution
- **No implicit waits** — all waiting goes through explicit conditions
- **Fluent API** — action methods return `this` for readable test chains
- **`@Step` annotations** — every page object method appears as a named step in the Allure report

## AI Workflow

This project was built iteratively with Claude Code:

1. Claude inspects the live page and extracts real locators from the DOM
2. Claude generates the page object and test class following project conventions
3. Tests run, output is reviewed, and Claude refines as needed

The goal is to show how AI can accelerate test automation without sacrificing code quality or maintainability.
