# CLAUDE.md — selenium-ai-demo

## Project Overview

A Java Selenium test automation project targeting [practicetestautomation.com](https://practicetestautomation.com) — a public practice site for QA automation learning. It covers three pages: a login form, an exceptions exercise page, and a filterable/sortable course table. The project is structured as a Maven project using the Page Object Model pattern, TestNG for test execution, and Allure for reporting.

---

## Tech Stack (exact versions)

- **Java** 24 (`maven.compiler.release=24`)
- **Selenium** 4.33.0
- **TestNG** 7.11.0
- **Allure TestNG** 2.29.1
- **AspectJ Weaver** 1.9.24 (required by Allure agent)
- **Maven Surefire** 3.5.5
- **Allure Maven plugin** 2.15.2

---

## Project Structure

```
src/
  main/java/com/example/
    config/         # ConfigReader — reads config.properties
    pages/          # Page object classes (BasePage + one class per page)
  test/java/com/example/
    tests/          # Test classes (BaseTest + one class per page)
  test/resources/
    config.properties
    testng.xml      # Suite definition — all test classes registered here
```

- Page objects go in `src/main/java/com/example/pages/`
- Test classes go in `src/test/java/com/example/tests/`
- Page object naming: `<PageName>Page.java` (e.g. `LoginPage`, `TablePage`)
- Test class naming: `<PageName>PageTest.java` (e.g. `LoginPageTest`, `TablePageTest`)

---

## Code Patterns to Follow

### Page Objects

- Extend `BasePage`. Always call `super(driver)` in the constructor.
- Declare all **top-level** locators as `private static final By` constants at the top of the class, before any methods. Align the `=` signs across the locator block with padding spaces (see `TablePage`, `ExceptionsPage`).
- Locators used only inside stream lambdas as nested finders (e.g. `row.findElement(By.cssSelector("td[data-col='language']"))`) stay inline — do not promote them to class-level constants.
- Prefer `By.id()` first, then `By.cssSelector()`, then `By.linkText()`. Avoid `By.xpath()`.
- Hardcode the page URL as a `private static final String URL` constant and navigate to it in `open()`.
- `open()` returns `this` (the page object itself) to enable method chaining.
- Action methods (`click*`, `type*`, `select*`) return `this` to enable fluent chaining.
- Getter/query methods (`get*`, `is*`) return the value type (`String`, `boolean`, `List<>`, etc.).
- All public methods must be annotated with `@Step("...")` from `io.qameta.allure`. Use `{paramName}` interpolation in step descriptions for dynamic values.
- Use `BasePage` helper methods for all interactions: `click(By)`, `type(By, text)`, `getText(By)`, `waitForVisible(By)`, `waitForClickable(By)`. Do not call `driver.findElement()` directly unless `BasePage` has no suitable method (e.g. `isDisplayed()` checks on already-found elements, multi-element queries).
- For `invisibilityOfElementLocated` waits, use `wait.until(ExpectedConditions.invisibilityOfElementLocated(...))` directly (as in `ExceptionsPage.isInstructionsGone()`).
- For `<select>` dropdowns use `new Select(waitForVisible(locator)).selectByValue(...)` (as in `TablePage.selectSortBy()`).
- For multi-element queries (lists, filtered rows), use `driver.findElements(...)` with `.stream().filter(WebElement::isDisplayed)`.
- To count visible elements, use `.stream().filter(WebElement::isDisplayed).count()` — not `.collect(Collectors.toList()).size()` (see `TablePage.getVisibleRowCount()`).
- When 2 or more public methods need the same filtered element collection, extract a private `getVisible<Entity>()` helper that returns `List<WebElement>` and have the public methods stream over it (see `TablePage.getVisibleRows()`). Private helpers do not need `@Step`.

### Test Classes

- Extend `BaseTest`. Never manage `WebDriver` directly in test classes — always use `driver()`.
- Call `new PageObject(driver()).open()` at the start of each test. Never reuse page objects across tests.
- Chain page object calls where the same page is returned, e.g. `new LoginPage(driver()).open().enterUsername(...).enterPassword(...).clickSubmit()`.
- All assertions use `org.testng.Assert` (not JUnit). Use `Assert.assertEquals`, `Assert.assertTrue`, `Assert.assertFalse`.
- Always pass a failure message as the third argument to every assertion.
- Each `@Test` method must have a `description` attribute: `@Test(description = "TC1: ...")`.
- Use `TC<n>:` prefixes in descriptions when there are multiple tests on the same page (see `ExceptionsPageTest`, `TablePageTest`).
- Test methods are `public void`, no parameters.
- Constants shared across test methods in a class (e.g. `TOTAL_COURSES`) are `private static final`.

### Configuration

- Read all config via `ConfigReader` static methods (`getBaseUrl()`, `getBrowser()`, `isHeadless()`, `get(key)`). Never read `System.getProperty` or hardcode config in tests.

### Parallel Execution

- `BaseTest` uses `ThreadLocal<WebDriver>` — driver is per-thread. Tests within a `<test>` block in `testng.xml` run in parallel across 3 threads. This means page objects and test methods must be stateless (no shared mutable fields).

---

## Code Patterns to NEVER Use

- **No `Thread.sleep()`** — use `BasePage.waitForVisible()`, `waitForClickable()`, or `wait.until(ExpectedConditions.*)` instead.
- **No JUnit** — the project uses TestNG exclusively. Never import `org.junit.*`.
- **No assertions in page objects** — page objects return values; assertions belong only in test classes.
- **No `@FindBy` / `PageFactory`** — locators are `private static final By` constants, not `PageFactory`-injected fields.
- **No `driver.manage().timeouts()`** — implicit waits are not used. All waits go through `WebDriverWait` in `BasePage`.
- **No direct `driver.findElement()` in test classes** — all element interaction goes through page object methods.
- **No static `WebDriver` fields** — driver is stored in `ThreadLocal` in `BaseTest` to support parallel execution.
- **No `@BeforeClass` / `@AfterClass`** — driver lifecycle is per-method (`@BeforeMethod` / `@AfterMethod`) in `BaseTest`.

---

## Running Tests

```bash
# Run all tests
mvn test

# Run headless
mvn test -Dheadless=true

# Run with a different browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge

# Generate and open Allure report after running tests
mvn allure:serve

# Generate static Allure report to target/site/allure-maven-plugin/
mvn allure:report
```

Test results (raw JSON) land in `target/allure-results/`. The pre-built HTML report is at `target/selenium-ai-demo-report.html`.

---

## How to Add a New Page Object

1. Create `src/main/java/com/example/pages/<Name>Page.java` in package `com.example.pages`.
2. Extend `BasePage` and call `super(driver)` in the constructor.
3. Add a `private static final String URL` if the page is directly navigable.
4. Declare all locators as `private static final By` constants before the constructor.
5. Add an `open()` method that calls `driver.get(URL)` and returns `this`.
6. Add action methods returning `this` and getter/query methods returning values. Annotate every public method with `@Step("...")`.
7. Use only `BasePage` helpers (`click`, `type`, `getText`, `waitForVisible`, `waitForClickable`) for element interactions.

Example skeleton:
```java
package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MyPage extends BasePage {

    private static final String URL = "https://practicetestautomation.com/...";

    private static final By SOME_ELEMENT = By.id("someId");

    public MyPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open my page")
    public MyPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Get some text")
    public String getSomeText() {
        return getText(SOME_ELEMENT);
    }
}
```

---

## How to Add a New Test Class

1. Create `src/test/java/com/example/tests/<Name>PageTest.java` in package `com.example.tests`.
2. Extend `BaseTest`. Do not add `@BeforeMethod` or `@AfterMethod` — driver lifecycle is handled by `BaseTest`.
3. Write `@Test(description = "TC<n>: ...")` methods. Instantiate the page object with `driver()` inside each test.
4. Use `org.testng.Assert` for all assertions. Always include a failure message.
5. Register the new class in `src/test/resources/testng.xml`:

```xml
<test name="My Page Tests">
    <classes>
        <class name="com.example.tests.MyPageTest"/>
    </classes>
</test>
```

Example skeleton:
```java
package com.example.tests;

import com.example.pages.MyPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MyPageTest extends BaseTest {

    @Test(description = "TC1: Verify something on my page")
    public void mySomethingTest() {
        MyPage page = new MyPage(driver()).open();
        Assert.assertEquals(page.getSomeText(), "Expected text",
                "Some text should match expected value");
    }
}
```

---

## Workflows

### When asked to study a page, add tests, or automate a page

1. **Inspect the page** — use Selenium MCP if available (navigate, wait for full load, extract real `id`/CSS selectors from the DOM); fall back to WebFetch and clearly flag any speculative locators.
2. **Check for an existing page object** — look in `src/main/java/com/example/pages/`. If one exists, read it; if not, plan a new one.
3. **Read the existing test class** (if one exists) — determine the current highest `TC<n>` number before generating anything.
4. **If creating a new page** — generate `<Name>Page.java`, `<Name>PageTest.java`, and the `testng.xml` snippet following all conventions in this file.
5. **If adding to an existing page** — output only the new `@Test` methods to paste into the existing class. If the page object is missing a needed method, generate that first and label the two outputs clearly.
6. **After generating tests** — remind the user to register the class in `testng.xml` if it is new.
7. **Flag speculative locators** — if any locator was guessed (WebFetch fallback or ambiguous DOM), list them explicitly so the user knows what to verify.

### When planning the page object API

Before writing code, decide:
- Which elements need **action methods** (buttons, inputs, dropdowns) → return `this`
- Which elements need **getter/query methods** (text, visibility, counts, lists) → return the value type
- Whether 2+ public methods will share the same filtered element collection → if so, plan a private `getVisible<Entity>()` helper

Aim for 3–6 focused tests that cover meaningful behaviours — not just "page loads".

### When asked to "come up with test cases" or "suggest tests"

Propose the test cases first and wait for approval before generating code.

---

## Configuration Reference (`config.properties`)

| Key | Default | Controls |
|-----|---------|----------|
| `base.url` | `https://example.com` | Base URL (used via `ConfigReader.getBaseUrl()`) |
| `browser` | `chrome` | Browser to launch: `chrome`, `firefox`, `edge` |
| `headless` | `false` | Run browser without UI when `true` |

- Override any property at the command line with `-D<key>=<value>` (e.g. `mvn test -Dheadless=true`).
- `ConfigReader` is a static utility — call `ConfigReader.get("key")` for arbitrary keys not covered by the named methods.
