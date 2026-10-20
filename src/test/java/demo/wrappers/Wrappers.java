package demo.wrappers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wrappers {

    private static final int DEFAULT_IPHONE_DISCOUNT = 17;

    private Wrappers() {
    }

    public static void enterTextWrapper(WebDriver driver, By locator, String textToEnter) {
        System.out.println("Sending keys");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            inputBox.clear();
            inputBox.sendKeys(textToEnter);
            inputBox.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }

    public static void ClickOnElementWrapper(WebDriver driver, By locator) {
        System.out.println("Clicking");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
            clickableElement.click();
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
        }
    }

    public static void clickOnElementWrapper(WebDriver driver, By locator) {
        ClickOnElementWrapper(driver, locator);
    }

    public static Boolean searchStarRatingAndPrintCount(
            WebDriver driver, By locator, double starRating) {
        int washingMachineCount = 0;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            int ratingCount = driver.findElements(locator).size();
            for (int i = 0; i < ratingCount; i++) {
                String ratingText = getElementTextWithRetry(driver, locator, i);
                if (Double.parseDouble(ratingText) <= starRating) {
                    washingMachineCount++;
                }
            }
            System.out.println("Count of washing machine which has star rating less than or equal to "
                    + starRating + ": " + washingMachineCount);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occured! " + e.getMessage());
            return false;
        }
    }

    private static String getElementTextWithRetry(WebDriver driver, By locator, int index) {
        StaleElementReferenceException lastStaleException = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (index >= elements.size()) {
                    throw new IllegalStateException("Rating list changed while it was being read");
                }
                return elements.get(index).getText();
            } catch (StaleElementReferenceException e) {
                lastStaleException = e;
            }
        }
        throw lastStaleException;
    }

    public static Boolean printTitleAndDiscountIphone(
            WebDriver driver, By locator, int discount) {
        try {
            List<WebElement> productRows =
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions
                        .visibilityOfAllElementsLocatedBy(locator));
            for (WebElement productRow : productRows) {
                List<WebElement> discountElements =
                        productRow.findElements(By.xpath(".//*[contains(normalize-space(.), '% off')]"));
                List<WebElement> titleElements =
                        productRow.findElements(By.xpath(".//*[contains(@class, 'KzDlHZ')]"));

                if (discountElements.isEmpty() || titleElements.isEmpty()) {
                    continue;
                }

                String discountText = discountElements.get(0).getText();
                Matcher discountMatcher = Pattern.compile("(\\d+)\\s*%").matcher(discountText);
                if (discountMatcher.find()
                        && Integer.parseInt(discountMatcher.group(1)) >= discount) {
                    System.out.println("iPhone title: " + titleElements.get(0).getText()
                            + " and discount: " + discountText);
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
            return false;
        }
    }

    public static Boolean printTitleAndDiscountIphone(WebDriver driver, By locator) {
        return printTitleAndDiscountIphone(driver, locator, DEFAULT_IPHONE_DISCOUNT);
    }

    public static Boolean printTitleAndImageUrlOfCoffeeMug(WebDriver driver, By locator) {
        try {
            List<CoffeeMugDetails> products = new ArrayList<>();
            List<WebElement> reviewElements = new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            System.out.println("Coffee mug review elements found: " + reviewElements.size());
            for (WebElement reviewElement : reviewElements) {
                Integer reviewCount = parseFirstNumber(reviewElement.getText());
                if (reviewCount == null) {
                    continue;
                }

                List<WebElement> productCards = reviewElement.findElements(
                        By.xpath("./ancestor::a[1]"));
                if (productCards.isEmpty()) {
                    productCards = reviewElement.findElements(
                            By.xpath("./ancestor::div[contains(@class, 'jsfZz_')][1]"));
                }
                if (productCards.isEmpty()) {
                    productCards = reviewElement.findElements(
                            By.xpath("./ancestor::div[contains(@class, 'slAVV4')][1]"));
                }
                if (productCards.isEmpty()) {
                    productCards = reviewElement.findElements(
                            By.xpath("./ancestor::div[.//img[contains(@src, '/image/')]][1]"));
                }
                if (productCards.isEmpty()) {
                    continue;
                }

                WebElement productCard = productCards.get(0);
                String[] titleLines = productCard.getText().split("\\R");
                String productTitle = titleLines.length == 0
                        ? "" : titleLines[0].replaceFirst("^\\d+\\.\\s*", "").trim();
                List<WebElement> images = productCard.findElements(
                        By.xpath(".//img[contains(@src, '/image/')]"));
                if (!productTitle.isEmpty() && !images.isEmpty()) {
                    products.add(new CoffeeMugDetails(
                            reviewCount, productTitle, images.get(0).getAttribute("src")));
                }
            }

            if (products.isEmpty()) {
                System.out.println("No coffee mug review counts with product details were found.");
                return false;
            }

            products.sort(Comparator.comparingInt(CoffeeMugDetails::getReviewCount).reversed());
            int resultCount = Math.min(5, products.size());
            for (int i = 0; i < resultCount; i++) {
                CoffeeMugDetails product = products.get(i);
                System.out.println((i + 1) + " highest review count: " + product.getReviewCount()
                        + ", title: " + product.getTitle()
                        + " and product image URL: " + product.getImageUrl());
            }
            return true;
        } catch (Exception e) {
            System.out.println("Exception Occured! " + e.getMessage());
            return false;
        }
    }

    private static Integer parseFirstNumber(String text) {
        Matcher matcher = Pattern.compile("[\\d,]+").matcher(text);
        if (!matcher.find()) {
            return null;
        }
        return Integer.parseInt(matcher.group().replace(",", ""));
    }

    private static class CoffeeMugDetails {
        private final int reviewCount;
        private final String title;
        private final String imageUrl;

        CoffeeMugDetails(int reviewCount, String title, String imageUrl) {
            this.reviewCount = reviewCount;
            this.title = title;
            this.imageUrl = imageUrl;
        }

        int getReviewCount() {
            return reviewCount;
        }

        String getTitle() {
            return title;
        }

        String getImageUrl() {
            return imageUrl;
        }
    }
}
