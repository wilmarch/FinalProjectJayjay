package DemoBlaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartPage {
    WebDriver driver;
    WebDriverWait wait;

    By totalPrice = By.id("totalp");
    By placeOrderBtn = By.xpath("//button[normalize-space()='Place Order']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public static class CartItemInfo {
        public String name;
        public String price;
    }

    public void waitForCartToLoad() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tbodyid")));
    }

    public void waitForMinimumRows(int minRows) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#tbodyid tr"), minRows - 1));
    }

    public List<CartItemInfo> getCartItems() {
        waitForCartToLoad();
        List<WebElement> rows = driver.findElements(By.cssSelector("#tbodyid tr"));
        List<CartItemInfo> result = new ArrayList<>();

        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            if (cols.size() >= 3) {
                CartItemInfo info = new CartItemInfo();
                info.name = cols.get(1).getText().trim();
                info.price = cols.get(2).getText().trim();
                result.add(info);
            }
        }
        return result;
    }

    public int getTotalPrice() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(totalPrice));
        wait.until(d -> !d.findElement(totalPrice).getText().trim().isEmpty());
        String priceText = driver.findElement(totalPrice).getText().trim();
        return Integer.parseInt(priceText);
    }

    public void clickPlaceOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn)).click();
    }

    public void deleteItemByName(String productName) {
        waitForCartToLoad();
        String rowXpath = String.format("//tr[td[normalize-space()='%s']]//a[normalize-space()='Delete']", productName);
        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(rowXpath)));
        deleteBtn.click();
        wait.until(ExpectedConditions.invisibilityOf(deleteBtn));
    }

    public void deleteAllItems() {
        waitForCartToLoad();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#tbodyid tr")));
        } catch (Exception e) {
            return;
        }

        while (true) {
            List<WebElement> deleteLinks = driver.findElements(By.xpath("//tr//a[normalize-space()='Delete']"));
            if (deleteLinks.isEmpty()) {
                break;
            }
            WebElement firstDelete = deleteLinks.get(0);
            firstDelete.click();
            wait.until(ExpectedConditions.stalenessOf(firstDelete));
        }
    }

    public boolean isCartEmpty() {
        return driver.findElements(By.cssSelector("#tbodyid tr")).isEmpty();
    }
}