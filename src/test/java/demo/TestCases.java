package demo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestCases {
    WebDriver driver;

    @BeforeTest
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    @Test
    public void testCase01() {
        System.out.println("Executing testCase01"); // step 1
        Reporter.log("Executing testCase01", true);

        driver.get("https://www.flipkart.com/");
        try { driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z")).click(); } catch (Exception ignored) {}

        WebElement box = driver.findElement(By.xpath("//input[@name='q']"));
        box.clear();
        box.sendKeys("Washing Machine");
        Assert.assertEquals(box.getAttribute("value"), "Washing Machine");
        box.sendKeys(Keys.ENTER);

        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Popularity']"))).click();

        List<WebElement> ratings = driver.findElements(By.xpath("//div[@class='_3LWZlK']"));
        int count = 0;
        for (WebElement rating : ratings) {
            String text = rating.getText().replace("★","");
            if (text.isEmpty()) continue;
            double value = Double.parseDouble(text.trim());
            String title = rating.findElement(By.xpath("./ancestor::div[contains(@class,'_1xHGtK')]//div[@class='_4rR01T']")).getText();
            System.out.println(title + " rating is : " + value); // step 2
            Reporter.log(title + " rating is : " + value, true);
            if (value <= 4.0) count++;
        }
        System.out.println("total count of items with rating <= 4 stars : " + count); // step 3
        Reporter.log("total count of items with rating <= 4 stars : " + count, true);
    }

    @Test
    public void testCase02() {
        System.out.println("Executing testCase02"); // step 4
        Reporter.log("Executing testCase02", true);

        driver.get("https://www.flipkart.com/");
        try { driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z")).click(); 
    } 
    catch (Exception ignored)
     {}

        WebElement box = driver.findElement(By.xpath("//input[@name='q']"));
        box.clear();
        box.sendKeys("iPhone");
        Assert.assertEquals(box.getAttribute("value"), "iPhone");
        box.sendKeys(Keys.ENTER);

        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'10% or more')]"))).click();

        List<WebElement> products = driver.findElements(By.xpath("//div[contains(@class,'_1xHGtK')]"));
        int count = 0;
        for (WebElement product : products) {
            try {
                String discount = product.findElement(By.xpath(".//span[contains(text(),'% off')]")).getText();
                String title = product.findElement(By.xpath(".//div[@class='_4rR01T']")).getText();
                System.out.println(title + " discount is : " + discount); // step 5
                Reporter.log(title + " discount is : " + discount, true);
                int value = Integer.parseInt(discount.replace("% off","").trim());
                if (value >= 17) count++;
            } catch (Exception ignored) {}
        }
        System.out.println("total items with discount >= 17% : " + count); // step 6
        Reporter.log("total items with discount >= 17% : " + count, true);
    }

    @Test
    public void testCase03() {
        System.out.println("Executing testCase03"); // step 7
        Reporter.log("Executing testCase03", true);

        driver.get("https://www.flipkart.com/");
        try { driver.findElement(By.cssSelector("button._2KpZ6l._2doB4z")).click(); } catch (Exception ignored) {}

        WebElement box = driver.findElement(By.xpath("//input[@name='q']"));
        box.clear();
        box.sendKeys("Coffee Mug");
        Assert.assertEquals(box.getAttribute("value"), "Coffee Mug");
        box.sendKeys(Keys.ENTER);

        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Popularity']"))).click();

        try {
            WebElement filter = driver.findElement(By.xpath("//div[contains(text(),'above')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", filter);
        } catch (Exception e) {
            System.out.println("Filter not found: " + e.getMessage()); // step 8
            Reporter.log("Filter not found: " + e.getMessage(), true);
        }

        List<WebElement> mugs = driver.findElements(By.xpath("//div[contains(@class,'_1xHGtK')]"));
        int count = 0;
        for (WebElement mug : mugs) {
            try {
                String rating = mug.findElement(By.xpath(".//div[@class='_3LWZlK']")).getText().replace("★","");
                if (rating.isEmpty()) continue;
                double ratingVal = Double.parseDouble(rating);
                String reviews = mug.findElement(By.xpath(".//span[contains(text(),'Ratings') or contains(text(),'Reviews')]")).getText();
                String title = mug.findElement(By.xpath(".//div[@class='_4rR01T']")).getText();
                String src = mug.findElement(By.xpath(".//img")).getAttribute("src");

                System.out.println(title + " review value is : " + reviews); // step 9
                Reporter.log(title +
                    "\n  src value is : " + src +
                    "\n  review value is : " + reviews +
                    "\n  rating value is : " + ratingVal, true);

                count++;
                if (count >= 5) break;
            } catch (Exception ignored) {}
        }
        System.out.println("total mugs fetched with reviews and ratings : " + count); // step 10
        Reporter.log("total mugs fetched with reviews and ratings : " + count, true);
    }

    @AfterTest
    public void tearDown() {
        if (driver != null){
        driver.quit();
        }
    }
}