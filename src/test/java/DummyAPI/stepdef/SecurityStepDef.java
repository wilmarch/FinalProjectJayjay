package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.models.UserPayload;
import DummyAPI.requests.SecurityRequest;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

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

    @When("a request is sent to create a user without app-id")
    public void aRequestIsSentToCreateAUserWithoutAppId() {
        Map<String, Object> payload = UserPayload.createValidUser(
                "Security", "Check", "security.write.check@mail.com");
        Response response = SecurityRequest.postWithoutAppId("/user/create", payload);
        context.setResponse(response);
    }
}