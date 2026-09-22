package DemoBlaze.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductDetailPage {
     WebDriver driver;
     WebDriverWait wait;

     By productName = By.cssSelector(".name");
     By productPrice = By.cssSelector(".price-container");
     By productDescription = By.cssSelector("#more-information p");
     By addToCartButton = By.xpath("//*[@id='tbodyid']/div[2]/div/a");

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartButton));
    }

    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productName)).getText().trim();
    }

    public String getProductPrice() {
        String rawPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(productPrice)).getText();
        return rawPrice.split(" ")[0].replace("$", "").trim();
    }

    public String getProductDescription() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productDescription)).getText().trim();
    }

    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
    }

    public String getAlertTextAndAccept() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }
}