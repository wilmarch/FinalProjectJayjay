package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import DemoBlaze.pages.NavigationHeader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

public class AuthStepDef {

    private final NavigationHeader navHeader = new NavigationHeader(BaseTest.driver);
    private final WebDriverWait wait = new WebDriverWait(BaseTest.driver, Duration.ofSeconds(10));
    private String lastGeneratedUsername;

    @Given("user is on the DemoBlaze homepage")
    public void userIsOnTheDemoBlazeHomepage() {
        BaseTest.driver.get("https://www.demoblaze.com/index.html");
    }

    @When("user opens the sign up modal")
    public void userOpensTheSignUpModal() {
        navHeader.openSignupModal();
    }

    @When("user registers with a dynamically generated username and password {string}")
    public void userRegistersWithDynamicallyGeneratedUsername(String password) {
        lastGeneratedUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);
        navHeader.enterSignupUsername(lastGeneratedUsername);
        navHeader.enterSignupPassword(password);
        navHeader.clickSignupSubmit();
    }

    @When("user registers with username {string} and password {string}")
    public void userRegistersWithUsernameAndPassword(String username, String password) {
        navHeader.enterSignupUsername(username);
        navHeader.enterSignupPassword(password);
        navHeader.clickSignupSubmit();
    }

    @When("user closes the sign up modal")
    public void userClosesTheSignUpModal() {
        BaseTest.driver.findElement(By.xpath("//div[@id='signInModal']//button[normalize-space()='Close']")).click();
    }

    @Then("the sign up modal should no longer be visible")
    public void theSignUpModalShouldNoLongerBeVisible() {
        boolean isClosed = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("signInModal")));
        Assert.assertTrue("Modal Sign Up masih terlihat", isClosed);
    }

    @When("user opens the login modal")
    public void userOpensTheLoginModal() {
        navHeader.openLoginModal();
    }

    @When("user logs in with username {string} and password {string}")
    public void userLogsInWithUsernameAndPassword(String username, String password) {
        navHeader.enterLoginUsername(username);
        navHeader.enterLoginPassword(password);
        navHeader.clickLoginSubmit();
    }

    @When("user logs in with non-existent random username and password {string}")
    public void userLogsInWithNonExistentRandomUsername(String password) {
        String randomUser = "ghost_" + UUID.randomUUID().toString().substring(0, 8);
        navHeader.enterLoginUsername(randomUser);
        navHeader.enterLoginPassword(password);
        navHeader.clickLoginSubmit();
    }

    @Given("user is logged in with username {string} and password {string}")
    public void userIsLoggedInWithUsernameAndPassword(String username, String password) {
        userIsOnTheDemoBlazeHomepage();
        navHeader.loginWithValidCredentials(username, password);
    }

    @Then("the navbar should display {string}")
    public void theNavbarShouldDisplay(String expectedText) {
        String actualWelcome = navHeader.getWelcomeUserText();
        Assert.assertEquals("Welcome text tidak sesuai", expectedText, actualWelcome);
    }

    @Then("the logout button should be visible")
    public void theLogoutButtonShouldBeVisible() {
        Assert.assertTrue("Tombol logout tidak terlihat", navHeader.isLogoutVisible());
    }

    @When("user clicks the logout button")
    public void userClicksTheLogoutButton() {
        navHeader.logout();
    }

    @Then("the navbar should display the {string} link")
    public void theNavbarShouldDisplayTheLink(String linkName) {
        if ("Log in".equalsIgnoreCase(linkName)) {
            Assert.assertTrue(BaseTest.driver.findElement(By.id("login2")).isDisplayed());
        } else if ("Sign up".equalsIgnoreCase(linkName)) {
            Assert.assertTrue(BaseTest.driver.findElement(By.id("signin2")).isDisplayed());
        }
    }
}