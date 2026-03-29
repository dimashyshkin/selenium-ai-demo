package com.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class TablePage extends BasePage {

    private static final String URL = "https://practicetestautomation.com/practice-test-table/";

    private static final By RESET_BUTTON        = By.id("resetFilters");
    private static final By SORT_BY_SELECT       = By.id("sortBy");
    private static final By ENROLL_DROPDOWN_BTN  = By.cssSelector("#enrollDropdown .dropdown-button");
    private static final By ENROLL_DROPDOWN_LABEL = By.cssSelector("#enrollDropdown .dropdown-label");
    private static final By NO_DATA_MESSAGE      = By.id("noData");
    private static final By TABLE_ROWS           = By.cssSelector("#courses_table tbody tr");

    public TablePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open table page")
    public TablePage open() {
        driver.get(URL);
        return this;
    }

    @Step("Select language filter: {lang}")
    public TablePage selectLanguage(String lang) {
        click(By.cssSelector("input[name='lang'][value='" + lang + "']"));
        return this;
    }

    @Step("Set level '{level}' checked={checked}")
    public TablePage setLevelChecked(String level, boolean checked) {
        WebElement cb = waitForVisible(By.cssSelector("input[name='level'][value='" + level + "']"));
        if (cb.isSelected() != checked) {
            cb.click();
        }
        return this;
    }

    @Step("Select min enrollments: {dataValue}")
    public TablePage selectMinEnrollments(String dataValue) {
        click(ENROLL_DROPDOWN_BTN);
        click(By.cssSelector("#enrollDropdown li[data-value='" + dataValue + "']"));
        return this;
    }

    @Step("Select sort by value: {value}")
    public TablePage selectSortBy(String value) {
        new Select(waitForVisible(SORT_BY_SELECT)).selectByValue(value);
        return this;
    }

    @Step("Click Reset button")
    public TablePage clickReset() {
        click(RESET_BUTTON);
        return this;
    }

    @Step("Get Reset button visibility")
    public boolean isResetButtonVisible() {
        return isDisplayed(RESET_BUTTON);
    }

    @Step("Get 'No matching courses' message visibility")
    public boolean isNoDataMessageVisible() {
        return isDisplayed(NO_DATA_MESSAGE);
    }

    @Step("Get level '{level}' checked state")
    public boolean isLevelChecked(String level) {
        return waitForVisible(By.cssSelector("input[name='level'][value='" + level + "']")).isSelected();
    }

    @Step("Get selected language")
    public String getSelectedLanguage() {
        return driver.findElements(By.cssSelector("input[name='lang']")).stream()
                     .filter(WebElement::isSelected)
                     .map(el -> el.getAttribute("value"))
                     .findFirst().orElse("");
    }

    @Step("Get selected enrollment dropdown label")
    public String getSelectedEnrollmentLabel() {
        return getText(ENROLL_DROPDOWN_LABEL);
    }

    @Step("Get visible row count")
    public int getVisibleRowCount() {
        return (int) driver.findElements(TABLE_ROWS).stream()
                           .filter(WebElement::isDisplayed)
                           .count();
    }

    @Step("Get visible language values")
    public List<String> getVisibleLanguages() {
        return getVisibleRows().stream()
                .map(row -> row.findElement(By.cssSelector("td[data-col='language']")).getText())
                .toList();
    }

    @Step("Get visible level values")
    public List<String> getVisibleLevels() {
        return getVisibleRows().stream()
                .map(row -> row.findElement(By.cssSelector("td[data-col='level']")).getText())
                .toList();
    }

    @Step("Get visible enrollments as integers")
    public List<Integer> getVisibleEnrollments() {
        return getVisibleRows().stream()
                .map(row -> row.findElement(By.cssSelector("td[data-col='enrollments']")).getText())
                .map(text -> Integer.parseInt(text.replace(",", "").trim()))
                .toList();
    }

    @Step("Get visible course names")
    public List<String> getVisibleCourseNames() {
        return getVisibleRows().stream()
                .map(row -> row.findElement(By.cssSelector("td[data-col='course']")).getText())
                .toList();
    }

    private List<WebElement> getVisibleRows() {
        return driver.findElements(TABLE_ROWS).stream()
                     .filter(WebElement::isDisplayed)
                     .toList();
    }
}
