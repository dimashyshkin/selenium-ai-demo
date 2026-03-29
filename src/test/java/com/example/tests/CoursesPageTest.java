package com.example.tests;

import com.example.pages.CoursesPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class CoursesPageTest extends BaseTest {

    private static final int    TOTAL_COURSES = 9;
    private static final List<String> EXPECTED_COURSE_NAMES = Arrays.asList(
            "Selenium WebDriver: Selenium Automation Testing with Java",
            "Selenium WebDriver: Selenium Automation Testing with Python",
            "Java for Testers",
            "Python: The Complete Guide for Software Testers",
            "Advanced Selenium WebDriver with Java and TestNG",
            "XPath locators for Selenium",
            "REST Assured. API test automation for beginners",
            "Advanced Selenium Grid and Cloud",
            "Advanced Selenium testing framework with Java"
    );

    @Test(description = "TC1: Page heading displays 'Courses'")
    public void pageHeadingTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        Assert.assertEquals(page.getPageHeading(), "Courses",
                "Page heading should be 'Courses'");
    }

    @Test(description = "TC2: Exactly 9 courses are listed on the page")
    public void courseCountTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        Assert.assertEquals(page.getCourseCount(), TOTAL_COURSES,
                "There should be exactly " + TOTAL_COURSES + " courses listed");
    }

    @Test(description = "TC3: All expected course titles are present in order")
    public void courseNamesTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        Assert.assertEquals(page.getCourseNames(), EXPECTED_COURSE_NAMES,
                "Course titles should match the expected list in order");
    }

    @Test(description = "TC4: Every course has an 'Enroll in this course on Udemy' button")
    public void enrollButtonCountTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        Assert.assertEquals(page.getEnrollButtonCount(), TOTAL_COURSES,
                "Each of the " + TOTAL_COURSES + " courses should have an enroll button");
    }

    @Test(description = "TC5: Every enroll button links to a non-empty URL")
    public void enrollButtonUrlsNotEmptyTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        List<String> urls = page.getEnrollButtonUrls();
        Assert.assertEquals(urls.size(), TOTAL_COURSES,
                "Should have " + TOTAL_COURSES + " enroll button URLs");
        for (int i = 0; i < urls.size(); i++) {
            Assert.assertFalse(urls.get(i) == null || urls.get(i).isBlank(),
                    "Enroll button " + (i + 1) + " should have a non-empty href");
        }
    }

    @Test(description = "TC6: Every course has a learning outcomes bullet list")
    public void learningOutcomesListsTest() {
        CoursesPage page = new CoursesPage(driver()).open();
        Assert.assertEquals(page.getLearningOutcomesListCount(), TOTAL_COURSES,
                "Each of the " + TOTAL_COURSES + " courses should have a learning outcomes list");
    }

    @Test(description = "TC7: Every enroll button redirects to a Udemy page")
    public void enrollButtonsRedirectToUdemyTest() {
        for (int i = 0; i < TOTAL_COURSES; i++) {
            CoursesPage page = new CoursesPage(driver()).open();
            String finalUrl = page.followEnrollLinkByIndex(i);
            Assert.assertTrue(finalUrl.contains("udemy.com"),
                    "Course " + (i + 1) + " enroll button should redirect to udemy.com, but landed on: " + finalUrl);
        }
    }
}
