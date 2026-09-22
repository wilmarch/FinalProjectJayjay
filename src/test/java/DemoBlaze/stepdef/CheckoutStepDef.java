package DemoBlaze.stepdef;

import DemoBlaze.context.TestContext;
import DemoBlaze.pages.OrderPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.Map;

public class CheckoutStepDef {

    OrderPage orderPage;
    private String lastSubmittedName;
    private String lastSubmittedCard;

    public CheckoutStepDef(TestContext context) {
        this.orderPage = context.getOrderPage();
    }

    @When("user fills order form with valid details:")
    public void userFillsOrderFormWithValidDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        lastSubmittedName = data.get("Name");
        lastSubmittedCard = data.get("Card");

        orderPage.fillOrderForm(
                lastSubmittedName,
                data.get("Country"),
                data.get("City"),
                lastSubmittedCard,
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
            Assert.assertTrue("Pop-up konfirmasi tidak memuat teks sukses", confText.contains("Thank you for your purchase!"));
            System.out.println("[KNOWN BUG DETECTED] Pembelian berhasil meski format data kartu/tanggal tidak valid.");
            orderPage.closeConfirmation();
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("[EXPECTED BEHAVIOR] Sistem berhasil menolak pembelian data invalid (pop-up sukses tidak muncul).");
        }
    }

    @Then("the purchase confirmation popup should appear")
    public void thePurchaseConfirmationPopupShouldAppear() {
        String conf = orderPage.getConfirmationText();
        Assert.assertTrue("Pop-up konfirmasi pembelian tidak tampil", conf.contains("Thank you for your purchase!"));
    }

    @Then("the confirmation should display matching Name, Card, and Total Amount")
    public void theConfirmationShouldDisplayMatchingNameCardAndTotalAmount() {
        String conf = orderPage.getConfirmationText();

        Assert.assertTrue("Label Amount tidak ditemukan di struk konfirmasi", conf.contains("Amount:"));

        if (lastSubmittedName != null) {
            Assert.assertTrue("Nama pada struk tidak sesuai dengan yang diinput: " + lastSubmittedName,
                    conf.contains(lastSubmittedName));
        }
        if (lastSubmittedCard != null) {
            Assert.assertTrue("Nomor kartu pada struk tidak sesuai dengan yang diinput: " + lastSubmittedCard,
                    conf.contains(lastSubmittedCard));
        }
    }

    @When("user clicks OK on confirmation popup")
    public void userClicksOKOnConfirmationPopup() {
        orderPage.closeConfirmation();
    }
}