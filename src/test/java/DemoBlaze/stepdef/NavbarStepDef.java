package DemoBlaze.stepdef;

import DemoBlaze.context.TestContext;
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

    TestContext context;
    HomePage homePage;
    NavigationHeader navHeader;
    WebDriverWait wait;

    public NavbarStepDef(TestContext context) {
        this.context = context;
        this.homePage = context.getHomePage();
        this.navHeader = context.getNavigationHeader();
        this.wait = new WebDriverWait(context.getDriver(), Duration.ofSeconds(10));
    }

    @When("user clicks {string} navbar link")
    public void userClicksNavbarLink(String linkText) {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText(linkText))).click();
    }

    @When("user submits contact message with all blank fields")
    public void userSubmitsContactMessageWithAllBlankFields() {
        navHeader.clickSendMessage();
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
        navHeader.waitForAboutUsModal();
    }

    @Then("the video player should be present inside the modal")
    public void theVideoPlayerShouldBePresentInsideTheModal() {
        Assert.assertTrue("Video player tidak ditemukan di modal", navHeader.isVideoPlayerPresent());
        navHeader.closeAboutUsModal();
    }

    @When("user clicks the site brand logo")
    public void userClicksTheSiteBrandLogo() {
        navHeader.clickBrandLogo();
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
        Assert.assertTrue(context.getDriver().getCurrentUrl().contains("index.html"));
    }

    @Then("an alert should appear with message {string}")
    public void anAlertShouldAppearWithMessage(String expectedMsg) {
        String alertText = navHeader.getAlertTextAndAccept();
        Assert.assertEquals("Pesan alert tidak cocok", expectedMsg, alertText);
    }
}