package com.example.tests;

import com.example.config.ConfigReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

public abstract class BaseTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    protected WebDriver driver() {
        return driverThreadLocal.get();
    }

    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.getBrowser();
        boolean headless = ConfigReader.isHeadless();

        WebDriver driver = switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("--headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless");
                yield new EdgeDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) options.addArguments("--headless");
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        driverThreadLocal.set(driver);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot on failure", "image/png",
                        new ByteArrayInputStream(screenshot), "png");
            }
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
