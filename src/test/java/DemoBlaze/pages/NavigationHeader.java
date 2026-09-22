package DemoBlaze.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavigationHeader {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Navbar links
    private final By homeLink = By.xpath("//*[@id='navbarExample']/ul/li[1]/a");
    private final By brandLogo = By.id("nava");
    private final By loginNavLink = By.id("login2");
    private final By signupNavLink = By.id("signin2");
    private final By logoutNavLink = By.id("logout2");
    private final By welcomeUserText = By.id("nameofuser");

    // Login modal
    private final By loginUsernameInput = By.id("loginusername");
    private final By loginPasswordInput = By.id("loginpassword");
    private final By loginSubmitButton = By.xpath("//div[@id='logInModal']//button[normalize-space()='Log in']");

    // Sign Up modal
    private final By signUpModal = By.id("signInModal");
    private final By signupUsernameInput = By.id("sign-username");
    private final By signupPasswordInput = By.id("sign-password");
    private final By signupSubmitButton = By.xpath("//div[@id='signInModal']//button[normalize-space()='Sign up']");
    private final By signupCloseButton = By.xpath("//div[@id='signInModal']//button[normalize-space()='Close']");

    // Contact modal
    private final By contactEmailInput = By.id("recipient-email");
    private final By contactNameInput = By.id("recipient-name");
    private final By contactMessageInput = By.id("message-text");
    private final By sendMessageButton = By.xpath("//button[normalize-space()='Send message']");

    // About Us modal
    private final By videoModal = By.id("videoModal");
    private final By videoPlayer = By.id("example-video");
    private final By videoModalCloseButton = By.xpath("//div[@id='videoModal']//button[normalize-space()='Close']");

    public NavigationHeader(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void safeClick(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    public void goToHome() {
        safeClick(homeLink);
    }

    public void clickBrandLogo() {
        safeClick(brandLogo);
    }

    public void goToCart() {
        if (driver.getCurrentUrl().contains("cart.html")) {
            return;
        }
        By cartLocator = By.xpath("//a[@id='cartur' or contains(@href, 'cart.html')]");
        try {
            WebElement cartElement = wait.until(ExpectedConditions.presenceOfElementLocated(cartLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartElement);
        } catch (Exception e) {
            driver.get("https://www.demoblaze.com/cart.html");
        }
    }

    public void openLoginModal() {
        safeClick(loginNavLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsernameInput));
    }

    public void enterLoginUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(loginUsernameInput));
        input.clear();
        input.sendKeys(username);
    }

    public void enterLoginPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(loginPasswordInput));
        input.clear();
        input.sendKeys(password);
    }

    public void clickLoginSubmit() {
        safeClick(loginSubmitButton);
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

    public boolean isLoginLinkVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginNavLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSignupLinkVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(signupNavLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        safeClick(logoutNavLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginNavLink));
    }

    public void openSignupModal() {
        safeClick(signupNavLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(signupUsernameInput));
    }

    public void enterSignupUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(signupUsernameInput));
        input.clear();
        input.sendKeys(username);
    }

    public void enterSignupPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(signupPasswordInput));
        input.clear();
        input.sendKeys(password);
    }

    public void clickSignupSubmit() {
        safeClick(signupSubmitButton);
    }

    public void closeSignupModal() {
        safeClick(signupCloseButton);
    }

    public boolean isSignupModalClosed() {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(signUpModal));
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
        safeClick(sendMessageButton);
    }

    public void waitForAboutUsModal() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(videoModal));
    }

    public boolean isVideoPlayerPresent() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(videoPlayer)).isDisplayed();
    }

    public void closeAboutUsModal() {
        safeClick(videoModalCloseButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(videoModal));
    }
}