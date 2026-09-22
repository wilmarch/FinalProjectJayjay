package DemoBlaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        List<WebElement> oldLinks = driver.findElements(productNameLinks);
        WebElement anchor = oldLinks.isEmpty() ? null : oldLinks.get(0);

        wait.until(ExpectedConditions.elementToBeClickable(categoryLocator)).click();

        if (anchor != null) {
            try {
                wait.until(ExpectedConditions.stalenessOf(anchor));
            } catch (TimeoutException ignored) {
            }
        }

        waitForProductGridToLoad();
    }

    public List<String> getAllProductNames() {
        waitForProductGridToLoad();
        StaleElementReferenceException lastError = null;
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                List<WebElement> links = driver.findElements(productNameLinks);
                List<String> names = new ArrayList<>();
                for (WebElement link : links) {
                    names.add(link.getText());
                }
                return names;
            } catch (StaleElementReferenceException e) {
                lastError = e;
            }
        }
        throw new StaleElementReferenceException(
                "Gagal membaca daftar produk setelah beberapa percobaan (DOM terus berubah)", lastError);
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