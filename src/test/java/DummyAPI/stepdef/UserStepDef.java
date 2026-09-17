package DummyAPI.stepdef;

import DummyAPI.context.TestContext;
import DummyAPI.models.UserPayload;
import DummyAPI.requests.UserRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.*;

public class UserStepDef {

    private final TestContext context;

    public UserStepDef(TestContext context) {
        this.context = context;
    }

    @Given("the list of users is requested")
    public void theListOfUsersIsRequested() {
        Response response = UserRequest.getUserList();
        context.setResponse(response);
    }

    @When("the list of users is requested with page {int} and limit {int}")
    public void theListOfUsersIsRequestedWithPageAndLimit(int page, int limit) {
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

    @And("the first user ID is extracted from the list")
    public void theFirstUserIdIsExtractedFromTheList() {
        String firstUserId = context.getResponse().jsonPath().getString("data[0].id");
        context.setUserId(firstUserId);
    }

    @When("user details are requested by that ID")
    public void userDetailsAreRequestedByThatId() {
        Response response = UserRequest.getUserById(context.getUserId());
        context.setResponse(response);
    }

    @And("the retrieved user details should match the ID")
    public void theRetrievedUserDetailsShouldMatchTheId() {
        context.getResponse().then().body("id", equalTo(context.getUserId()));
    }

    @And("the user mandatory fields should not be empty")
    public void theUserMandatoryFieldsShouldNotBeEmpty() {
        context.getResponse().then()
                .body("firstName", notNullValue())
                .body("lastName", notNullValue())
                .body("email", notNullValue());
    }

    @When("user details are requested with non-existent ID {string}")
    public void userDetailsAreRequestedWithNonExistentId(String nonExistentId) {
        Response response = UserRequest.getUserById(nonExistentId);
        context.setResponse(response);
    }

    @When("user details are requested with invalid format ID {string}")
    public void userDetailsAreRequestedWithInvalidFormatId(String invalidFormatId) {
        Response response = UserRequest.getUserById(invalidFormatId);
        context.setResponse(response);
    }

    @When("a request is sent to create a user with firstName {string}, lastName {string}, and email {string}")
    public void aRequestIsSentToCreateAUserWithEmail(String firstName, String lastName, String email) {
        context.setUserEmail(email);

        Map<String, Object> payload = UserPayload.createValidUser(firstName, lastName, email);
        context.setRequestPayload(payload);

        Response response = UserRequest.createUser(payload);
        context.setResponse(response);
    }

    @Given("a request is sent to create a user with unique email for duplicate check")
    public void aRequestIsSentToCreateAUserWithUniqueEmailForDuplicateCheck() {
        String testEmail = "duplicate_check_" + System.currentTimeMillis() + "@mail.com";
        context.setUserEmail(testEmail);

        Map<String, Object> payload = UserPayload.createValidUser("FirstUser", "Test", testEmail);
        context.setRequestPayload(payload);

        Response response = UserRequest.createUser(payload);
        context.setResponse(response);

        if (response.getStatusCode() == 200) {
            String createdId = response.jsonPath().getString("id");
            context.setUserId(createdId);
        }
    }

    @And("the created user should have valid id")
    public void theCreatedUserShouldHaveValidId() {
        context.getResponse().then()
                .body("id", notNullValue())
                .body("id", not(emptyOrNullString()));

        String createdId = context.getResponse().jsonPath().getString("id");
        context.setUserId(createdId);
    }

    @And("the created user details should match the request payload")
    public void theCreatedUserDetailsShouldMatchTheRequestPayload() {
        Map<String, Object> payload = context.getRequestPayload();
        context.getResponse().then()
                .body("firstName", equalTo(payload.get("firstName")))
                .body("lastName", equalTo(payload.get("lastName")))
                .body("email", equalTo(payload.get("email")));
    }

    @When("a request is sent to create a user with the same email")
    public void aRequestIsSentToCreateAUserWithTheSameEmail() {
        Map<String, Object> payload = UserPayload.createValidUser("Duplicate", "User", context.getUserEmail());
        Response response = UserRequest.createUser(payload);
        context.setResponse(response);
    }

    @When("a request is sent to create a user missing {string}")
    public void aRequestIsSentToCreateAUserMissing(String mandatoryField) {
        String staticEmail = "missing_field_check@mail.com";
        Map<String, Object> payload = UserPayload.createMissingFieldUser(mandatoryField, staticEmail);

        Response response = UserRequest.createUser(payload);
        context.setResponse(response);
    }

    @When("a request is sent to update the user with the following fields:")
    public void aRequestIsSentToUpdateTheUserWithTheFollowingFields(DataTable dataTable) {
        Map<String, String> fields = dataTable.asMap(String.class, String.class);
        Map<String, Object> payload = UserPayload.createUpdatePayload(fields);
        context.setRequestPayload(payload);

        Response response = UserRequest.updateUser(context.getUserId(), payload);
        context.setResponse(response);
    }

    @When("a request is sent to update the user location with street {string}, city {string}, state {string}, country {string}, and timezone {string}")
    public void aRequestIsSentToUpdateTheUserLocation(String street, String city, String state,
                                                      String country, String timezone) {
        Map<String, Object> payload = UserPayload.createLocationUpdatePayload(street, city, state, country, timezone);
        context.setRequestPayload(payload);

        Response response = UserRequest.updateUser(context.getUserId(), payload);
        context.setResponse(response);
    }

    @When("a request is sent to update the user email to {string}")
    public void aRequestIsSentToUpdateTheUserEmailTo(String newEmail) {
        Map<String, Object> payload = UserPayload.createSingleFieldUpdatePayload("email", newEmail);
        context.setRequestPayload(payload);

        Response response = UserRequest.updateUser(context.getUserId(), payload);
        context.setResponse(response);
    }

    @When("a request is sent to update user with non-existent ID {string}")
    public void aRequestIsSentToUpdateUserWithNonExistentId(String nonExistentId) {
        Map<String, Object> payload = UserPayload.createSingleFieldUpdatePayload("firstName", "Ghost");
        Response response = UserRequest.updateUser(nonExistentId, payload);
        context.setResponse(response);
    }

    @When("a request is sent to update user with invalid format ID {string}")
    public void aRequestIsSentToUpdateUserWithInvalidFormatId(String invalidFormatId) {
        Map<String, Object> payload = UserPayload.createSingleFieldUpdatePayload("firstName", "Ghost");
        Response response = UserRequest.updateUser(invalidFormatId, payload);
        context.setResponse(response);
    }

    @When("a request is sent to delete the user by that ID")
    public void aRequestIsSentToDeleteTheUserByThatId() {
        Response response = UserRequest.deleteUser(context.getUserId());
        context.setResponse(response);
    }

    @When("a request is sent to delete user with non-existent ID {string}")
    public void aRequestIsSentToDeleteUserWithNonExistentId(String nonExistentId) {
        Response response = UserRequest.deleteUser(nonExistentId);
        context.setResponse(response);
    }

    @When("a request is sent to delete user with invalid format ID {string}")
    public void aRequestIsSentToDeleteUserWithInvalidFormatId(String invalidFormatId) {
        Response response = UserRequest.deleteUser(invalidFormatId);
        context.setResponse(response);
    }

    @And("the response body path {string} should match the created user id")
    public void theResponseBodyPathShouldMatchTheCreatedUserId(String jsonPath) {
        context.getResponse().then()
                .body(jsonPath, equalTo(context.getUserId()));
    }

    @And("the response body path {string} should be {string}")
    public void theResponseBodyPathShouldBe(String jsonPath, String expectedValue) {
        context.getResponse().then()
                .body(jsonPath, equalTo(expectedValue));
    }
}