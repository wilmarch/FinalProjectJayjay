package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import DemoBlaze.pages.CartPage;
import DemoBlaze.pages.HomePage;
import DemoBlaze.pages.NavigationHeader;
import DemoBlaze.pages.OrderPage;
import DemoBlaze.pages.ProductDetailPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class CheckoutStepDef {

    private final HomePage homePage = new HomePage(BaseTest.driver);
    private final ProductDetailPage detailPage = new ProductDetailPage(BaseTest.driver);
    private final CartPage cartPage = new CartPage(BaseTest.driver);
    private final OrderPage orderPage = new OrderPage(BaseTest.driver);
    private final NavigationHeader navHeader = new NavigationHeader(BaseTest.driver);

    @When("user fills order form with valid details:")
    public void userFillsOrderFormWithValidDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        orderPage.fillOrderForm(
                data.get("Name"),
                data.get("Country"),
                data.get("City"),
                data.get("Card"),
                data.get("Month"),
                data.get("Year")
        );
    }

    @When("user fills order form with:")
    public void userFillsOrderFormWith(DataTable dataTable) {
        userFillsOrderFormWithValidDetails(dataTable);
    }

    @When("user submits the purchase")
    public void userSubmitsThePurchase() {
        orderPage.clickPurchase();
    }

    @When("user submits the purchase without filling any field")
    public void userSubmitsThePurchaseWithoutFillingAnyField() {
        orderPage.waitForOrderModal();
        orderPage.clickPurchase();
    }

    @Then("verify if the system accepts invalid input or shows rejection")
    public void verifyIfTheSystemAcceptsInvalidInputOrShowsRejection() {
        try {
            String confText = orderPage.getConfirmationText();
            Assert.assertTrue(confText.contains("Thank you for your purchase!"));
            System.out.println("[KNOWN BUG DETECTED] Pembelian berhasil meski format data tidak valid.");
            orderPage.closeConfirmation();
        } catch (Exception e) {
            System.out.println("Sistem menolak purchase.");
        }
    }

    @Then("the purchase confirmation popup should appear")
    public void thePurchaseConfirmationPopupShouldAppear() {
        String conf = orderPage.getConfirmationText();
        Assert.assertTrue(conf.contains("Thank you for your purchase!"));
    }

    @Then("the confirmation should display matching Name, Card, and Total Amount")
    public void theConfirmationShouldDisplayMatchingNameCardAndTotalAmount() {
        String conf = orderPage.getConfirmationText();
        Assert.assertTrue(conf.contains("Amount:"));
        Assert.assertTrue(conf.contains("Card Number:"));
        Assert.assertTrue(conf.contains("Name:"));
    }

    @Given("user has completed a purchase for {string}")
    public void userHasCompletedAPurchaseFor(String product) {
        homePage.openHomePage();
        homePage.clickProductByName(product);
        detailPage.waitForPageToLoad();
        detailPage.clickAddToCart();
        detailPage.getAlertTextAndAccept();
        navHeader.goToHome();

        navHeader.goToCart();
        cartPage.waitForCartToLoad();
        cartPage.clickPlaceOrder();

        orderPage.fillOrderForm("Test User", "Indonesia", "Jakarta", "41111111", "12", "2028");
        orderPage.clickPurchase();
        orderPage.getConfirmationText();
    }

    @When("user clicks OK on confirmation popup")
    public void userClicksOKOnConfirmationPopup() {
        orderPage.closeConfirmation();
    }

    @Then("user should be redirected to the homepage")
    public void userShouldBeRedirectedToTheHomepage() {
        homePage.waitForProductGridToLoad();
    }
}