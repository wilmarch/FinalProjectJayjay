package DemoBlaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.Duration;

public class OrderPage {
    WebDriver driver;
    WebDriverWait wait;

     By orderModal = By.id("orderModal");
     By nameInput = By.id("name");
     By countryInput = By.id("country");
     By cityInput = By.id("city");
     By cardInput = By.id("card");
     By monthInput = By.id("month");
     By yearInput = By.id("year");
     By purchaseButton = By.xpath("//button[normalize-space()='Purchase']");

     By sweetAlertBox = By.cssSelector(".sweet-alert.showSweetAlert.visible");
     By sweetAlertText = By.cssSelector(".sweet-alert.showSweetAlert.visible p");
     By sweetAlertTitle = By.cssSelector(".sweet-alert.showSweetAlert.visible h2");
     By confirmOkButton = By.cssSelector(".confirm.btn.btn-lg.btn-primary");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void waitForOrderModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderModal));
        wait.until(ExpectedConditions.elementToBeClickable(nameInput));
    }

    public void fillOrderForm(String name, String country, String city, String card, String month, String year) {
        waitForOrderModal();

        typeText(nameInput, name);
        typeText(countryInput, country);
        typeText(cityInput, city);
        typeText(cardInput, card);
        typeText(monthInput, month);
        typeText(yearInput, year);
    }

    private void typeText(By locator, String text) {
        if (text != null) {
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
        }
    }

    public void clickPurchase() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(purchaseButton));
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    public String getConfirmationText() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sweetAlertBox));
        String title = driver.findElement(sweetAlertTitle).getText();
        String details = driver.findElement(sweetAlertText).getText();
        return title + "\n" + details;
    }

    public void closeConfirmation() {
        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(confirmOkButton));
        try {
            okButton.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", okButton);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(sweetAlertBox));
    }

    public int getConfirmedAmount() {
        String text = getConfirmationText();
        Matcher matcher = Pattern.compile("Amount:\\s*(\\d+)").matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        throw new IllegalStateException("Label 'Amount: <angka>' tidak ditemukan di dalam struk konfirmasi:\n" + text);
    }
}