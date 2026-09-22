package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import DemoBlaze.pages.HomePage;
import DemoBlaze.pages.NavigationHeader;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Map;

public class NavbarStepDef {

    private final HomePage homePage = new HomePage(BaseTest.driver);
    private final NavigationHeader navHeader = new NavigationHeader(BaseTest.driver);
    private final WebDriverWait wait = new WebDriverWait(BaseTest.driver, Duration.ofSeconds(10));

    @When("user clicks {string} navbar link")
    public void userClicksNavbarLink(String linkText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
    }

    @When("user submits contact message with all blank fields")
    public void userSubmitsContactMessageWithAllBlankFields() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Send message']"))).click();
    }

    @Then("document if system alerts {string} despite empty inputs")
    public void documentIfSystemAlertsDespiteEmptyInputs(String expectedMsg) {
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        System.out.println("[KNOWN BUG DETECTED] Contact form menerima input kosong dengan notifikasi: " + text);
        Assert.assertEquals(expectedMsg, text);
    }

    @Then("the About Us modal should appear")
    public void theAboutUsModalShouldAppear() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("videoModal")));
    }

    @Then("the video player should be present inside the modal")
    public void theVideoPlayerShouldBePresentInsideTheModal() {
        Assert.assertTrue(BaseTest.driver.findElement(By.id("example-video")).isDisplayed());
        BaseTest.driver.findElement(By.xpath("//div[@id='videoModal']//button[normalize-space()='Close']")).click();
    }

    @When("user clicks the site brand logo")
    public void userClicksTheSiteBrandLogo() {
        BaseTest.driver.findElement(By.id("nava")).click();
    }

    @When("user fills contact form with valid details:")
    public void userFillsContactFormWithValidDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        navHeader.fillContactForm(
                data.get("Email"),
                data.get("Name"),
                data.get("Message")
        );
    }

    @When("user submits the contact message")
    public void userSubmitsTheContactMessage() {
        navHeader.clickSendMessage();
    }

    @When("user clicks the Home link in navbar")
    public void userClicksTheHomeLinkInNavbar() {
        navHeader.goToHome();
    }

    @Then("user should be redirected to the homepage product grid")
    public void userShouldBeRedirectedToTheHomepageProductGrid() {
        homePage.waitForProductGridToLoad();
        Assert.assertTrue(BaseTest.driver.getCurrentUrl().contains("index.html"));
    }
}