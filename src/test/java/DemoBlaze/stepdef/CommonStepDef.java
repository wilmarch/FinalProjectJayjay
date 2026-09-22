package DemoBlaze.stepdef;

import DemoBlaze.base.BaseTest;
import DemoBlaze.pages.NavigationHeader;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class CommonStepDef {

    private final NavigationHeader navHeader = new NavigationHeader(BaseTest.driver);

    @Then("an alert should appear with message {string}")
    public void anAlertShouldAppearWithMessage(String expectedMsg) {
        String alertText = navHeader.getAlertTextAndAccept();
        Assert.assertEquals("Pesan alert tidak cocok", expectedMsg, alertText);
    }

    @Then("user accepts the alert")
    public void userAcceptsTheAlert() {
    }
}