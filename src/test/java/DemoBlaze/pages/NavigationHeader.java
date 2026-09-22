package DemoBlaze.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class NavigationHeader {

    WebDriver driver;
    WebDriverWait wait;

    // Navbar links
    By homeLink = By.xpath("//*[@id='navbarExample']/ul/li[1]/a");
    By loginNavLink = By.id("login2");
    By signupNavLink = By.id("signin2");
    By logoutNavLink = By.id("logout2");
    By welcomeUserText = By.id("nameofuser");

    // Login modal
    By loginUsernameInput = By.id("loginusername");
    By loginPasswordInput = By.id("loginpassword");
    By loginSubmitButton = By.xpath("//button[normalize-space()='Log in']");

    // Sign Up modal
    By signupUsernameInput = By.id("sign-username");
    By signupPasswordInput = By.id("sign-password");
    By signupSubmitButton = By.xpath("//button[normalize-space()='Sign up']");

    By contactEmailInput = By.id("recipient-email");
    By contactNameInput = By.id("recipient-name");
    By contactMessageInput = By.id("message-text");
    By sendMessageButton = By.xpath("//button[normalize-space()='Send message']");

    public NavigationHeader(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void goToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
    }

    public void goToCart() {
        if (driver.getCurrentUrl().contains("cart.html")) {
            return;
        }
        By cartLocator = By.xpath("//a[@id='cartur' or contains(@href, 'cart.html')]");
        try {
            WebElement cartElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartLocator));
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", cartElement);
        } catch (Exception e) {
            driver.get("https://www.demoblaze.com/cart.html");
        }
    }

    public void openLoginModal() {
        wait.until(ExpectedConditions.elementToBeClickable(loginNavLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsernameInput));
    }

    public void enterLoginUsername(String username) {
        driver.findElement(loginUsernameInput).sendKeys(username);
    }

    public void enterLoginPassword(String password) {
        driver.findElement(loginPasswordInput).sendKeys(password);
    }

    public void clickLoginSubmit() {
        driver.findElement(loginSubmitButton).click();
    }

    public void loginWithValidCredentials(String username, String password) {
        openLoginModal();
        enterLoginUsername(username);
        enterLoginPassword(password);
        clickLoginSubmit();
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutNavLink));
    }

    public String getWelcomeUserText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeUserText)).getText();
    }

    public boolean isLogoutVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutNavLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutNavLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginNavLink));
    }

    public void openSignupModal() {
        wait.until(ExpectedConditions.elementToBeClickable(signupNavLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(signupUsernameInput));
    }

    public void enterSignupUsername(String username) {
        driver.findElement(signupUsernameInput).sendKeys(username);
    }

    public void enterSignupPassword(String password) {
        driver.findElement(signupPasswordInput).sendKeys(password);
    }

    public void clickSignupSubmit() {
        driver.findElement(signupSubmitButton).click();
    }

    public String getAlertTextAndAccept() {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public void fillContactForm(String email, String name, String message) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactEmailInput)).sendKeys(email);
        driver.findElement(contactNameInput).sendKeys(name);
        driver.findElement(contactMessageInput).sendKeys(message);
    }

    public void clickSendMessage() {
        wait.until(ExpectedConditions.elementToBeClickable(sendMessageButton)).click();
    }
}