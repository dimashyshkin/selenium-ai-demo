---
name: new-page
description: Create a new Selenium page object and test class for a given URL. Inspects the live DOM to extract real locators, then generates *Page.java, *PageTest.java, and the testng.xml registration snippet following project conventions.
---

You are creating a new page object and test class for this Selenium project. Follow every rule in CLAUDE.md exactly.

## Input

The URL to target is: $ARGUMENTS

If no URL was provided, ask the user for one before proceeding.

## Step 1 — Inspect the page

Derive a short PascalCase page name from the URL path (e.g. `/courses/` → `Courses`, `/practice-test-login/` → `Login`).

Attempt to inspect the live page to extract real element IDs and CSS selectors:
- **If Selenium MCP is available**: navigate to the URL, wait for the page to fully load, then inspect the DOM. Extract element `id` attributes, relevant CSS classes, tag names, and visible text for all interactive and queryable elements.
- **If only WebFetch is available**: fetch the URL and extract what you can. Note clearly which locators are speculative and will need verification.

## Step 2 — Plan the page object API

From the inspected elements, decide:
- Which elements need action methods (buttons, inputs, dropdowns) → return `this`
- Which elements need getter/query methods (text, visibility, counts, lists) → return the value type
- Whether 2+ public methods will need the same filtered element collection → if so, plan a private `getVisible<Entity>()` helper

## Step 3 — Generate the page object

File: `src/main/java/com/example/pages/<Name>Page.java`

Rules (from CLAUDE.md):
- Extend `BasePage`, call `super(driver)` in constructor
- `private static final String URL` for the page URL
- All top-level locators as `private static final By` constants, `=` signs aligned across the block
- Locators used only inside stream lambdas stay inline — do not promote to constants
- Every public method annotated with `@Step("...")`; use `{paramName}` for dynamic values
- Use BasePage helpers: `click(By)`, `type(By, text)`, `getText(By)`, `waitForVisible(By)`, `waitForClickable(By)`
- For invisible-element waits: `wait.until(ExpectedConditions.invisibilityOfElementLocated(...))`
- For `<select>` dropdowns: `new Select(waitForVisible(locator)).selectByValue(...)`
- For counts: `.stream().filter(WebElement::isDisplayed).count()` — not `.collect().size()`
- Private helpers get no `@Step`
- No assertions, no `Thread.sleep`, no `PageFactory`

## Step 4 — Generate the test class

File: `src/test/java/com/example/tests/<Name>PageTest.java`

Rules (from CLAUDE.md):
- Extend `BaseTest`, no `@BeforeMethod`/`@AfterMethod`
- Each test: `@Test(description = "TC<n>: ...")`
- Instantiate the page fresh in every test: `new <Name>Page(driver()).open()`
- Chain calls when one page is returned and no variable is needed; assign to `page` variable when calling multiple methods
- All assertions via `org.testng.Assert` with a failure message as third argument
- `private static final` for any constants shared across tests

Write tests that cover the meaningful behaviours visible on the page — not just "page loads". Aim for 3–6 focused tests.

## Step 5 — Output the testng.xml snippet

Show the `<test>` block the user must add to `src/test/resources/testng.xml`:

```xml
<test name="<Name> Page Tests">
    <classes>
        <class name="com.example.tests.<Name>PageTest"/>
    </classes>
</test>
```

Remind the user to add it inside the `<suite>` element.

## Step 6 — Flag speculative locators

If any locators were guessed (WebFetch fallback or ambiguous DOM), list them explicitly so the user knows what to verify by running the tests:

> **Locators to verify:** `COURSE_CARDS = By.cssSelector("...")` — confirm this selector matches the rendered elements.
