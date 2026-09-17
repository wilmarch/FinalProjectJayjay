@api @user
Feature: User API DummyAPI

  # CREATE USER (POST)
  @create @cleanup_user
  Scenario: Successfully create user with valid mandatory fields
    When I send request to create a user with firstName "QA", lastName "Engineer", and email "qa.valid.tester@mail.com"
    Then the response status code should be 200
    And the created user should have valid id
    And the response body path "email" should be "qa.valid.tester@mail.com"

  @create @negative @cleanup_user
  Scenario: Fail to create user with existing email
    Given I send request to create a user with unique email for duplicate check
    And the response status code should be 200
    When I send request to create a user with the same email
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

  @create @negative
  Scenario Outline: Fail to create user missing mandatory fields
    When I send request to create a user missing "<mandatory_field>"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

    Examples:
      | mandatory_field |
      | firstName       |
      | lastName        |
      | email           |

  @create @email @negative
  Scenario Outline: Fail to create user with invalid email format
    When I send request to create a user with firstName "QA", lastName "Engineer", and email "<invalid_email>"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

    Examples:
      | invalid_email             |
      | plainaddress              |
      | @missingusername.com      |
      | username@.com             |
      | username@domain..com      |
      | username space@domain.com |

  # GET USER LIST & DETAIL
  @get
  Scenario: Successfully get list of users with pagination
    When I request the list of users with page 1 and limit 5
    Then the response status code should be 200
    And the user list page should be 1
    And the user list limit should be 5
    And the user list data size should be 5

  @get
  Scenario: Successfully get user details by valid ID
    Given I request the list of users
    And I extract the first user ID from the list
    When I request the user details by that ID
    Then the response status code should be 200
    And the retrieved user details should match the ID
    And the user mandatory fields should not be empty

  @get @negative
  Scenario: Fail to get user with non-existent ID
    When I request user details with non-existent ID "60d0fe4f5311236168a109ca"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  @get @negative
  Scenario: Fail to get user with invalid ID format
    When I request user details with invalid format ID "invalid_user_id_123"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

  @get @create @cleanup_user
  Scenario: Successfully get user details of the newly created user
    Given I send request to create a user with firstName "Created", lastName "Directly", and email "newly.created.user@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When I request the user details by that ID
    Then the response status code should be 200
    And the retrieved user details should match the ID
    And the response body path "firstName" should be "Created"
    And the response body path "lastName" should be "Directly"
    And the response body path "email" should be "newly.created.user@mail.com"