package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoggedInPage extends BasePage {

    private static final By HEADING       = By.cssSelector("h1");
    private static final By LOGOUT_BUTTON = By.linkText("Log out");

    public LoggedInPage(WebDriver driver) {
        super(driver);
    }

    @Step("Get page heading")
    public String getHeading() {
        return getText(HEADING);
    }

    @Step("Check Log out button is displayed")
    public boolean isLogoutButtonDisplayed() {
        return waitForVisible(LOGOUT_BUTTON).isDisplayed();
    }
}
