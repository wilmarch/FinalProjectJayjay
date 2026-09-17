package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.requests.TagRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class TagStepDef {

    private final TestContext context;

    public TagStepDef(TestContext context) {
        this.context = context;
    }

    @When("I send request to get list of tags")
    public void iSendRequestToGetListOfTags() {
        Response response = TagRequest.getTags();
        context.setResponse(response);
    }

    @When("I send request to get list of tags without app-id")
    public void iSendRequestToGetListOfTagsWithoutAppId() {
        Response response = TagRequest.getTagsWithoutAuth();
        context.setResponse(response);
    }

    @When("I send request to get list of tags with app-id {string}")
    public void iSendRequestToGetListOfTagsWithAppId(String customAppId) {
        Response response = TagRequest.getTagsWithCustomAppId(customAppId);
        context.setResponse(response);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        context.getResponse().then().log().ifValidationFails();
        context.getResponse().then().statusCode(expectedStatusCode);
    }

    @And("the tags list should not be empty")
    public void theTagsListShouldNotEmpty() {
        context.getResponse().then().body("data", not(empty()));
    }

    @And("the response error should be {string}")
    public void theResponseErrorShouldBe(String expectedError) {
        context.getResponse().then().body("error", equalTo(expectedError));
    }

    @When("I send request to an invalid tags endpoint {string}")
    public void iSendRequestToAnInvalidTagsEndpoint(String endpointPath) {
        Response response = TagRequest.getTagsWithCustomPath(endpointPath);
        context.setResponse(response);
    }
}