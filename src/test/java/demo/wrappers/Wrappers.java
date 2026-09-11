package demo.wrappers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import java.time.Duration;
import java.util.List;

public class Wrappers {
    WebDriver driver;
    WebDriverWait wait;

    public Wrappers(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigate_to_url(String url) {
        driver.get(url);
        Reporter.log("Navigated to: " + url, true);
    }

    public void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        Reporter.log("Clicked element: " + locator.toString(), true);
    }

    public void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
        Reporter.log("Typed '" + text + "' into: " + locator.toString(), true);
    }

    public String getAttributeValue(By locator, String attribute) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String value = element.getAttribute(attribute);
        Reporter.log("Attribute " + attribute + " value: " + value, true);
        return value;
    }

    public List<WebElement> get_search_items_list(By locator) {
        return driver.findElements(locator);
    }

    public String getTitleofResult(WebElement element, By locator) {
        try {
            return element.findElement(locator).getText();
        } catch (Exception e) {
            return null;
        }
    }
}