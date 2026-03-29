---
name: add-tests
description: Add new @Test methods to an existing test class for a page already covered in this project. Reads the existing page object to understand available methods, then generates only the new test methods to paste in.
---

You are adding new test methods to an existing test class in this Selenium project. Follow every rule in CLAUDE.md exactly.

## Input

The user will provide one or both of:
- **Page name** — e.g. `Login`, `Table`, `Exceptions`
- **Scenario description** — what behaviour to test

If either is missing, ask before proceeding.

Arguments received: $ARGUMENTS

## Step 1 — Read the existing code

Read both files for the named page:
- `src/main/java/com/example/pages/<Name>Page.java` — to know what methods are available
- `src/test/java/com/example/tests/<Name>PageTest.java` — to know what is already tested and what `TC<n>` number to continue from

Do not guess — read the actual files.

## Step 2 — Identify gaps

Based on the existing tests and the scenario description, determine:
- What is not yet covered
- Whether the existing page object has all the methods needed, or whether a new method must be added to the page object first

If a new page object method is needed, generate it following page object rules before generating the test.

## Step 3 — Generate new @Test methods only

Output only the new `@Test` methods to be added inside the existing test class — not the full class.

Rules (from CLAUDE.md):
- Continue the `TC<n>:` numbering from where the existing tests leave off
- `@Test(description = "TC<n>: ...")`
- Instantiate the page fresh: `new <Name>Page(driver()).open()`
- All assertions via `org.testng.Assert` with a failure message as third argument
- Chain when one page is returned; assign to `page` variable when calling multiple methods
- `public void`, no parameters
- New `private static final` constants if needed (add near the top of the class)

## Step 4 — If a new page object method is needed

Output the new method(s) to add to `<Name>Page.java`, following page object rules:
- Annotate with `@Step("...")`
- Action methods return `this`; getter/query methods return the value type
- Use BasePage helpers; no assertions; no `Thread.sleep`

Clearly label which code goes in the page object and which goes in the test class.
