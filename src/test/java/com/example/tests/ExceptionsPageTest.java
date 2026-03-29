package com.example.tests;

import com.example.pages.ExceptionsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExceptionsPageTest extends BaseTest {

    @Test(description = "TC1: Verify Row 2 input is displayed after clicking Add (NoSuchElementException)")
    public void noSuchElementTest() {
        ExceptionsPage page = new ExceptionsPage(driver()).open();
        page.clickAdd();
        Assert.assertTrue(page.isRow2InputDisplayed(),
                "Row 2 input should be displayed after clicking Add");
    }

    @Test(description = "TC2: Verify text can be typed and saved in Row 2 (ElementNotInteractableException)")
    public void elementNotInteractableTest() {
        ExceptionsPage page = new ExceptionsPage(driver()).open();
        page.clickAdd();
        page.typeInRow2("Sushi");
        page.clickSaveRow2();
        Assert.assertEquals(page.getConfirmationMessage(), "Row 2 was saved",
                "Confirmation message should appear after saving row 2");
    }

    @Test(description = "TC3: Verify Row 1 input can be edited after clicking Edit (InvalidElementStateException)")
    public void invalidElementStateTest() {
        ExceptionsPage page = new ExceptionsPage(driver()).open();
        page.clickEdit();
        page.typeInRow1("Burger");
        page.clickSaveRow1();
        Assert.assertEquals(page.getRow1InputValue(), "Burger",
                "Row 1 input should contain the updated text after saving");
    }

    @Test(description = "TC4: Verify instructions element is removed after clicking Add (StaleElementReferenceException)")
    public void staleElementReferenceTest() {
        ExceptionsPage page = new ExceptionsPage(driver()).open();
        page.clickAdd();
        Assert.assertTrue(page.isInstructionsGone(),
                "Instructions text should be removed from the page after Row 2 is added");
    }

    @Test(description = "TC5: Verify Row 2 input appears within timeout after clicking Add (TimeoutException)")
    public void timeoutExceptionTest() {
        ExceptionsPage page = new ExceptionsPage(driver()).open();
        page.clickAdd();
        Assert.assertTrue(page.isRow2InputDisplayed(),
                "Row 2 input should appear within the configured wait timeout");
    }
}
