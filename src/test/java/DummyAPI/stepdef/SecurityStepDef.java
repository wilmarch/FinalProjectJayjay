package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.requests.SecurityRequest;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class SecurityStepDef {

    private final TestContext context;

    public SecurityStepDef(TestContext context) {
        this.context = context;
    }

    @When("a request is sent to {string} without app-id")
    public void aRequestIsSentToEndpointWithoutAppId(String endpoint) {
        Response response = SecurityRequest.getWithoutAppId(endpoint);
        context.setResponse(response);
    }

    @When("a request is sent to {string} with invalid app-id {string}")
    public void aRequestIsSentToEndpointWithInvalidAppId(String endpoint, String invalidAppId) {
        Response response = SecurityRequest.getWithCustomAppId(endpoint, invalidAppId);
        context.setResponse(response);
    }
}