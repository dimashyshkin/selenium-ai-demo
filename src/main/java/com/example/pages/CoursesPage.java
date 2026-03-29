package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class CoursesPage extends BasePage {

    private static final String URL = "https://practicetestautomation.com/courses/";

    private static final By PAGE_HEADING   = By.cssSelector("h1.post-title");
    private static final By COURSE_TITLES  = By.cssSelector(".post-content h2.wp-block-heading");
    private static final By ENROLL_BUTTONS = By.cssSelector(".wp-block-buttons a");
    private static final By OUTCOME_LISTS  = By.cssSelector(".post-content ul.wp-block-list");

    public CoursesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Courses page")
    public CoursesPage open() {
        driver.get(URL);
        return this;
    }

    @Step("Get page heading text")
    public String getPageHeading() {
        return getText(PAGE_HEADING);
    }

    @Step("Get course count")
    public int getCourseCount() {
        return (int) driver.findElements(COURSE_TITLES).stream()
                           .filter(WebElement::isDisplayed)
                           .count();
    }

    @Step("Get course names")
    public List<String> getCourseNames() {
        return driver.findElements(COURSE_TITLES).stream()
                     .filter(WebElement::isDisplayed)
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    @Step("Get enroll button count")
    public int getEnrollButtonCount() {
        return (int) driver.findElements(ENROLL_BUTTONS).stream()
                           .filter(WebElement::isDisplayed)
                           .count();
    }

    @Step("Get enroll button URLs")
    public List<String> getEnrollButtonUrls() {
        return driver.findElements(ENROLL_BUTTONS).stream()
                     .filter(WebElement::isDisplayed)
                     .map(el -> el.getAttribute("href"))
                     .collect(Collectors.toList());
    }

    @Step("Get learning outcomes list count")
    public int getLearningOutcomesListCount() {
        return (int) driver.findElements(OUTCOME_LISTS).stream()
                           .filter(WebElement::isDisplayed)
                           .count();
    }

    @Step("Follow enroll link at index {index} and return final URL")
    public String followEnrollLinkByIndex(int index) {
        String href = getEnrollButtonUrls().get(index);
        driver.get(href);
        wait.until(ExpectedConditions.urlContains("udemy.com"));
        return driver.getCurrentUrl();
    }
}
