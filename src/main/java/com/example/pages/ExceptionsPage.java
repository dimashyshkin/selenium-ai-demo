package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ExceptionsPage extends BasePage {

    private static final String URL = "https://practicetestautomation.com/practice-test-exceptions/";

    private static final By ADD_BUTTON       = By.id("add_btn");
    private static final By EDIT_BUTTON      = By.id("edit_btn");
    private static final By SAVE_BUTTON_ROW1 = By.id("save_btn");
    private static final By ROW1_INPUT       = By.cssSelector("#row1 input");
    private static final By ROW2_INPUT       = By.cssSelector("#row2 input");
    private static final By SAVE_BUTTON_ROW2 = By.cssSelector("#row2 [name='Save']");
    private static final By INSTRUCTIONS     = By.id("instructions");
    private static final By CONFIRMATION     = By.id("confirmation");

    public ExceptionsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open exceptions page")
    public ExceptionsPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Click Add button")
    public ExceptionsPage clickAdd() {
        click(ADD_BUTTON);
        return this;
    }

    @Step("Click Edit button")
    public ExceptionsPage clickEdit() {
        click(EDIT_BUTTON);
        return this;
    }

    @Step("Click Save button for row 1")
    public ExceptionsPage clickSaveRow1() {
        click(SAVE_BUTTON_ROW1);
        return this;
    }

    @Step("Click Save button for row 2")
    public ExceptionsPage clickSaveRow2() {
        click(SAVE_BUTTON_ROW2);
        return this;
    }

    @Step("Type '{text}' into row 1 input")
    public ExceptionsPage typeInRow1(String text) {
        type(ROW1_INPUT, text);
        return this;
    }

    @Step("Type '{text}' into row 2 input")
    public ExceptionsPage typeInRow2(String text) {
        type(ROW2_INPUT, text);
        return this;
    }

    @Step("Get row 1 input value")
    public String getRow1InputValue() {
        return waitForVisible(ROW1_INPUT).getAttribute("value");
    }

    @Step("Verify row 2 input is displayed")
    public boolean isRow2InputDisplayed() {
        return waitForVisible(ROW2_INPUT).isDisplayed();
    }

    @Step("Verify instructions text is no longer displayed")
    public boolean isInstructionsGone() {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(INSTRUCTIONS));
    }

    @Step("Get confirmation message")
    public String getConfirmationMessage() {
        return getText(CONFIRMATION);
    }
}
