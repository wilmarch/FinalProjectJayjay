@api
Feature: Get User API DummyAPI

  Scenario: Successfully get list of users with pagination
    When I request the list of users with page 1 and limit 5
    Then the response status code should be 200
    And the user list page should be 1
    And the user list limit should be 5
    And the user list data size should be 5

  Scenario: Successfully get user details by valid ID
    Given I request the list of users
    And I extract the first user ID from the list
    When I request the user details by that ID
    Then the response status code should be 200
    And the retrieved user details should match the ID
    And the user mandatory fields should not be empty

  Scenario: Fail to get user with non-existent ID
    When I request user details with non-existent ID "60d0fe4f5311236168a109ca"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  Scenario: Fail to get user with invalid ID format
    When I request user details with invalid format ID "invalid_user_id_123"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

