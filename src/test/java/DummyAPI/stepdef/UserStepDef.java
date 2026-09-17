package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.requests.UserRequest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

public class UserStepDef {

    private final TestContext context;

    public UserStepDef(TestContext context) {
        this.context = context;
    }

    @Given("I request the list of users")
    public void iRequestTheListOfUsers() {
        Response response = UserRequest.getUserList();
        context.setResponse(response);
    }

    @When("I request the list of users with page {int} and limit {int}")
    public void iRequestTheListOfUsersWithPageAndLimit(int page, int limit) {
        Response response = UserRequest.getUserListWithPagination(page, limit);
        context.setResponse(response);
    }

    @And("the user list page should be {int}")
    public void theUserListPageShouldBe(int expectedPage) {
        context.getResponse().then().body("page", equalTo(expectedPage));
    }

    @And("the user list limit should be {int}")
    public void theUserListLimitShouldBe(int expectedLimit) {
        context.getResponse().then().body("limit", equalTo(expectedLimit));
    }

    @And("the user list data size should be {int}")
    public void theUserListDataSizeShouldBe(int expectedSize) {
        context.getResponse().then().body("data.size()", equalTo(expectedSize));
    }

    @And("I extract the first user ID from the list")
    public void iExtractTheFirstUserIDFromTheList() {
        String firstUserId = context.getResponse().jsonPath().getString("data[0].id");
        context.setUserId(firstUserId);
    }

    @When("I request the user details by that ID")
    public void iRequestTheUserDetailsByThatID() {
        Response response = UserRequest.getUserById(context.getUserId());
        context.setResponse(response);
    }

    @And("the retrieved user details should match the ID")
    public void theRetrievedUserDetailsShouldMatchTheID() {
        context.getResponse().then().body("id", equalTo(context.getUserId()));
    }

    @And("the user mandatory fields should not be empty")
    public void theUserMandatoryFieldsShouldNotBeEmpty() {
        context.getResponse().then()
                .body("firstName", notNullValue())
                .body("lastName", notNullValue())
                .body("email", notNullValue());
    }

    @When("I request user details with non-existent ID {string}")
    public void iRequestUserDetailsWithNonExistentID(String nonExistentId) {
        Response response = UserRequest.getUserById(nonExistentId);
        context.setResponse(response);
    }

    @When("I request user details with invalid format ID {string}")
    public void iRequestUserDetailsWithInvalidFormatID(String invalidFormatId) {
        Response response = UserRequest.getUserById(invalidFormatId);
        context.setResponse(response);
    }
}