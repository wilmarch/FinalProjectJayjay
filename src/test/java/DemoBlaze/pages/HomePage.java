package DemoBlaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object untuk homepage demoblaze (index.html) - kategori, grid produk,
 * navigasi ke detail produk, dan pagination.
 */
public class HomePage {

    WebDriver driver;
    WebDriverWait wait;

    By productGrid = By.id("tbodyid");
    By productNameLinks = By.cssSelector(".card-title a");
    By nextPageButton = By.id("next2");
    By previousPageButton = By.id("previous2");

    By phonesCategory = By.linkText("Phones");
    By laptopsCategory = By.linkText("Laptops");
    By monitorsCategory = By.linkText("Monitors");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openHomePage() {
        driver.get("https://www.demoblaze.com/index.html");
        waitForProductGridToLoad();
    }

    public void waitForProductGridToLoad() {
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("cart.html")));
        wait.until(ExpectedConditions.presenceOfElementLocated(productGrid));
        wait.until(ExpectedConditions.visibilityOfElementLocated(productNameLinks));
    }

    public void selectCategory(String categoryName) {
        By categoryLocator;
        switch (categoryName.toLowerCase()) {
            case "phones":
                categoryLocator = phonesCategory;
                break;
            case "laptops":
                categoryLocator = laptopsCategory;
                break;
            case "monitors":
                categoryLocator = monitorsCategory;
                break;
            default:
                throw new IllegalArgumentException("Kategori tidak dikenal: " + categoryName);
        }
        wait.until(ExpectedConditions.elementToBeClickable(categoryLocator)).click();
        waitForProductGridToLoad();
    }

    public List<String> getAllProductNames() {
        waitForProductGridToLoad();
        List<WebElement> links = driver.findElements(productNameLinks);
        List<String> names = new ArrayList<>();
        for (WebElement link : links) {
            names.add(link.getText());
        }
        return names;
    }

    public String getPriceByProductName(String productName) {
        String xpath = String.format(
                "//a[text()='%s']/ancestor::div[contains(@class,'card-block')]//h5[@class='card-title']",
                productName);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).getText();
    }

    public void clickProductByName(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(productName))).click();
    }

    public void clickNextPage() {
        wait.until(ExpectedConditions.elementToBeClickable(nextPageButton)).click();
        waitForProductGridToLoad();
    }

    public void clickPreviousPage() {
        wait.until(ExpectedConditions.elementToBeClickable(previousPageButton)).click();
        waitForProductGridToLoad();
    }
}