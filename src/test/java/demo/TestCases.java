package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import demo.wrappers.Wrappers;

public class TestCases {
    WebDriver driver;

    @BeforeTest
    public void setUp() {
    // Save browser commands for the platform assessment.
    File buildDirectory = new File("build");
    if (!buildDirectory.isDirectory() && !buildDirectory.mkdirs()) {
        throw new IllegalStateException("Could not create build directory");
    }

    ChromeDriverService service = new ChromeDriverService.Builder()
        .withVerbose(true)
        .withLogFile(new File(buildDirectory, "chromedriver.log"))
        .build();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");

    driver = new ChromeDriver(service, options);
    driver.manage().window().maximize();
}

    @Test(enabled = true)
    public void testCase01() throws InterruptedException {
        System.out.println("Beginning Test Case 01");

        double starRating = 4.0;
        driver.get("https://www.flipkart.com/");
        String currentUrl = driver.getCurrentUrl();
        Wrappers.enterTextWrapper (driver, By.xpath("//input[@name='q']"), "Washing Machine");

        //ExplicitWait
        Wrappers.ClickOnElementWrapper(driver, By.xpath("//div[contains(text(), 'Popularity')]"));
        Boolean status = Wrappers.searchStarRatingAndPrintCount(driver, By.xpath("//span[contains(@id,'productRating')]"), starRating);
        // Assert.assertTrue(status, "Could not read washing-machine ratings");

        System.out.println("Ending Test Case 01");
    }

    @Test(enabled = true)
    public void testCase02() throws InterruptedException {
        System.out.println("Beginning Test Case 02"); // step 7 
        
        int discount = 17;
        driver.get("https://www.flipkart.com/");
        Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "iPhone");
        // Thread.sleep(5000);
        Boolean status = Wrappers.printTitleAndDiscountIphone(driver, By.cssSelector("div[data-tkid]"), discount);
        // Assert.assertTrue(status, "Could not process iPhone discounts");

        System.out.println("Ending Test Case 02");
    }

    @Test(enabled = true)
    public void testCase03() throws InterruptedException {
        System.out.println("Beginning Test Case 03");

        driver.get("https://www.flipkart.com/");
        Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "Coffee Mug");
        // Thread.sleep(3000);
        Wrappers.clickOnElementWrapper(driver, By.xpath("//div[normalize-space(.)='4\u2605 & above']"));
        // Thread.sleep(3000);
        Boolean status = Wrappers.printTitleAndImageUrlOfCoffeeMug(driver, 
            By.xpath("//span[contains(concat(' ', normalize-space(@class), ' '), ' PvbNMB ')]")
            );

        // Assert.assertTrue(status, "Could not read coffee-mug product details");

        System.out.println("Ending Test Case 03");
    }
    
    
    @AfterTest(alwaysRun = true)
    public void endTest()
    {
        driver.close();
        driver.quit();
        
    }
}