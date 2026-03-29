package com.example.tests;

import com.example.pages.LoggedInPage;
import com.example.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginPageTest extends BaseTest {

    @Test(description = "Verify successful login with valid credentials")
    public void successfulLoginTest() {
        new LoginPage(driver()).open()
                             .enterUsername("student")
                             .enterPassword("Password123")
                             .clickSubmit();

        LoggedInPage loggedInPage = new LoggedInPage(driver());
        Assert.assertEquals(loggedInPage.getHeading(), "Logged In Successfully",
                "Heading should confirm successful login");
        Assert.assertTrue(loggedInPage.isLogoutButtonDisplayed(),
                "Log out button should be visible after login");
    }

    @Test(description = "Verify error message for invalid username")
    public void invalidUsernameTest() {
        LoginPage loginPage = new LoginPage(driver());
        loginPage.open()
                 .enterUsername("invalidUser")
                 .enterPassword("Password123")
                 .clickSubmit();

        Assert.assertEquals(loginPage.getErrorMessage(), "Your username is invalid!",
                "Error message should indicate invalid username");
    }

    @Test(description = "Verify error message for invalid password")
    public void invalidPasswordTest() {
        LoginPage loginPage = new LoginPage(driver());
        loginPage.open()
                 .enterUsername("student")
                 .enterPassword("wrongPassword")
                 .clickSubmit();

        Assert.assertEquals(loginPage.getErrorMessage(), "Your password is invalid!",
                "Error message should indicate invalid password");
    }
}
