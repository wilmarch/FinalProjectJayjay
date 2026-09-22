package DemoBlaze.context;

import DemoBlaze.base.DriverFactory;
import DemoBlaze.pages.*;
import org.openqa.selenium.WebDriver;

public class TestContext {

    private WebDriver driver;
    private HomePage homePage;
    private CartPage cartPage;
    private ProductDetailPage productDetailPage;
    private OrderPage orderPage;
    private NavigationHeader navigationHeader;

    public WebDriver getDriver() {
        if (driver == null) {
            driver = DriverFactory.createDriver();
        }
        return driver;
    }

    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(getDriver());
        }
        return homePage;
    }

    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage(getDriver());
        }
        return cartPage;
    }

    public ProductDetailPage getProductDetailPage() {
        if (productDetailPage == null) {
            productDetailPage = new ProductDetailPage(getDriver());
        }
        return productDetailPage;
    }

    public OrderPage getOrderPage() {
        if (orderPage == null) {
            orderPage = new OrderPage(getDriver());
        }
        return orderPage;
    }

    public NavigationHeader getNavigationHeader() {
        if (navigationHeader == null) {
            navigationHeader = new NavigationHeader(getDriver());
        }
        return navigationHeader;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}