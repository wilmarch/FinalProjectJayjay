package DummyAPI.stepdef;

import DummyAPI.configs.ApiConfig;
import DummyAPI.context.TestContext;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SecurityStepDef {

    private final TestContext context;

    public SecurityStepDef(TestContext context) {
        this.context = context;
    }

    @When("I send request to {string} without app-id")
    public void iSendRequestToEndpointWithoutAppId(String endpoint) {
        Response response = RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .when()
                .get(endpoint);

        context.setResponse(response);
    }

    @When("I send request to {string} with invalid app-id {string}")
    public void iSendRequestToEndpointWithInvalidAppId(String endpoint, String invalidAppId) {
        Response response = RestAssured.given()
                .baseUri(ApiConfig.BASE_URL)
                .header("app-id", invalidAppId)
                .when()
                .get(endpoint);

        context.setResponse(response);
    }
}