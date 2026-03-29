package com.example.tests;

import com.example.pages.TablePage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TablePageTest extends BaseTest {

    private static final int TOTAL_COURSES = 9;

    @Test(description = "TC1: Language filter = Java shows only Java courses")
    public void languageFilterJavaTest() {
        TablePage page = new TablePage(driver()).open();
        page.selectLanguage("Java");

        List<String> languages = page.getVisibleLanguages();
        Assert.assertFalse(languages.isEmpty(), "At least one Java course should be visible");
        languages.forEach(lang ->
                Assert.assertEquals(lang, "Java", "Every visible row should have Language = Java"));
    }

    @Test(description = "TC2: Level filter = Beginner only shows Beginner courses")
    public void levelFilterBeginnerTest() {
        TablePage page = new TablePage(driver()).open();
        page.setLevelChecked("Intermediate", false);
        page.setLevelChecked("Advanced", false);

        List<String> levels = page.getVisibleLevels();
        Assert.assertFalse(levels.isEmpty(), "At least one Beginner course should be visible");
        levels.forEach(level ->
                Assert.assertEquals(level, "Beginner", "Every visible row should have Level = Beginner"));
    }

    @Test(description = "TC3: Min enrollments = 10,000+ shows only courses with ≥ 10,000 enrollments")
    public void minEnrollmentsFilterTest() {
        TablePage page = new TablePage(driver()).open();
        page.selectMinEnrollments("10000");

        List<Integer> enrollments = page.getVisibleEnrollments();
        Assert.assertFalse(enrollments.isEmpty(), "At least one course should meet the 10,000+ threshold");
        enrollments.forEach(enroll ->
                Assert.assertTrue(enroll >= 10_000,
                        "Enrollments should be ≥ 10,000 but found: " + enroll));
    }

    @Test(description = "TC4: Combined Python + Beginner + 10,000+ filter")
    public void combinedFiltersTest() {
        TablePage page = new TablePage(driver()).open();
        page.selectLanguage("Python");
        page.setLevelChecked("Intermediate", false);
        page.setLevelChecked("Advanced", false);
        page.selectMinEnrollments("10000");

        List<String>  languages = page.getVisibleLanguages();
        List<String>  levels    = page.getVisibleLevels();
        List<Integer> enrolls   = page.getVisibleEnrollments();

        Assert.assertFalse(languages.isEmpty(), "At least one course should match all three filters");
        languages.forEach(lang ->
                Assert.assertEquals(lang, "Python", "Language should be Python"));
        levels.forEach(level ->
                Assert.assertEquals(level, "Beginner", "Level should be Beginner"));
        enrolls.forEach(enroll ->
                Assert.assertTrue(enroll >= 10_000,
                        "Enrollments should be ≥ 10,000 but found: " + enroll));
    }

    @Test(description = "TC5: No-results state shown for impossible filter combination")
    public void noResultsStateTest() {
        // Python courses are all Beginner; unchecking Beginner leaves nothing to show
        TablePage page = new TablePage(driver()).open();
        page.selectLanguage("Python");
        page.setLevelChecked("Beginner", false);

        Assert.assertTrue(page.isNoDataMessageVisible(),
                "'No matching courses.' message should be displayed");
        Assert.assertEquals(page.getVisibleRowCount(), 0,
                "No table rows should be visible");
    }

    @Test(description = "TC6: Reset button visibility and behavior restores all defaults")
    public void resetButtonTest() {
        TablePage page = new TablePage(driver()).open();
        Assert.assertFalse(page.isResetButtonVisible(),
                "Reset button should be hidden on initial page load");

        page.selectLanguage("Java");
        Assert.assertTrue(page.isResetButtonVisible(),
                "Reset button should appear after changing a filter");

        page.clickReset();
        Assert.assertFalse(page.isResetButtonVisible(),
                "Reset button should be hidden again after reset");
        Assert.assertEquals(page.getSelectedLanguage(), "Any",
                "Language should reset to Any");
        Assert.assertTrue(page.isLevelChecked("Beginner"),
                "Beginner level should be checked after reset");
        Assert.assertTrue(page.isLevelChecked("Intermediate"),
                "Intermediate level should be checked after reset");
        Assert.assertTrue(page.isLevelChecked("Advanced"),
                "Advanced level should be checked after reset");
        Assert.assertEquals(page.getSelectedEnrollmentLabel(), "Any",
                "Enrollments dropdown should reset to Any");
        Assert.assertEquals(page.getVisibleRowCount(), TOTAL_COURSES,
                "All " + TOTAL_COURSES + " courses should be visible after reset");
    }

    @Test(description = "TC7: Sort by Enrollments orders rows numerically ascending")
    public void sortByEnrollmentsTest() {
        TablePage page = new TablePage(driver()).open();
        page.selectSortBy("col_enroll");

        List<Integer> enrollments = page.getVisibleEnrollments();
        Assert.assertEquals(enrollments.size(), TOTAL_COURSES, "All courses should be visible");
        for (int i = 0; i < enrollments.size() - 1; i++) {
            Assert.assertTrue(enrollments.get(i) <= enrollments.get(i + 1),
                    "Enrollments should be sorted ascending: "
                    + enrollments.get(i) + " appears before " + enrollments.get(i + 1));
        }
    }

    @Test(description = "TC8: Sort by Course Name orders rows alphabetically A→Z")
    public void sortByCourseNameTest() {
        TablePage page = new TablePage(driver()).open();
        page.selectSortBy("col_course");

        List<String> names = page.getVisibleCourseNames();
        Assert.assertEquals(names.size(), TOTAL_COURSES, "All courses should be visible");
        for (int i = 0; i < names.size() - 1; i++) {
            Assert.assertTrue(names.get(i).compareToIgnoreCase(names.get(i + 1)) <= 0,
                    "Course names should be sorted A→Z: '"
                    + names.get(i) + "' appears before '" + names.get(i + 1) + "'");
        }
    }
}
