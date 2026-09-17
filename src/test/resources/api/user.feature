@api @user
Feature: User API DummyAPI

  @get
  Scenario: Successfully get list of users with pagination
    When the list of users is requested with page 1 and limit 5
    Then the response status code should be 200
    And the user list page should be 1
    And the user list limit should be 5
    And the user list data size should be 5

  @get
  Scenario: Successfully get user details by valid ID
    Given the list of users is requested
    And the first user ID is extracted from the list
    When user details are requested by that ID
    Then the response status code should be 200
    And the retrieved user details should match the ID
    And the user mandatory fields should not be empty

  @get @negative
  Scenario: Fail to get user with non-existent ID
    When user details are requested with non-existent ID "60d0fe4f5311236168a109ca"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  @get @negative
  Scenario: Fail to get user with invalid ID format
    When user details are requested with invalid format ID "invalid_user_id_123"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

  @get @create @cleanup_user
  Scenario: Successfully get user details of the newly created user
    Given a request is sent to create a user with firstName "Created", lastName "Directly", and email "newly.created.user@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When user details are requested by that ID
    Then the response status code should be 200
    And the retrieved user details should match the ID
    And the response body path "firstName" should be "Created"
    And the response body path "lastName" should be "Directly"
    And the response body path "email" should be "newly.created.user@mail.com"

  @create @cleanup_user
  Scenario: Successfully create user with valid mandatory fields
    When a request is sent to create a user with firstName "QA", lastName "Engineer", and email "qa.valid.tester@mail.com"
    Then the response status code should be 200
    And the created user should have valid id
    And the response body path "email" should be "qa.valid.tester@mail.com"

  @create @negative @cleanup_user
  Scenario: Fail to create user with existing email
    Given a request is sent to create a user with unique email for duplicate check
    And the response status code should be 200
    When a request is sent to create a user with the same email
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

  @create @negative
  Scenario Outline: Fail to create user missing mandatory fields
    When a request is sent to create a user missing "<mandatory_field>"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

    Examples:
      | mandatory_field |
      | firstName       |
      | lastName        |
      | email           |

  @create @email @negative
  Scenario Outline: Fail to create user with invalid email format
    When a request is sent to create a user with firstName "QA", lastName "Engineer", and email "<invalid_email>"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

    Examples:
      | invalid_email             |
      | plainaddress              |
      | @missingusername.com      |
      | username@.com             |
      | username@domain..com      |
      | username space@domain.com |

  @update @cleanup_user
  Scenario: Successfully update a single field
    Given a request is sent to create a user with firstName "Original", lastName "Name", and email "update.single.field@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to update the user with the following fields:
      | firstName | Updated |
    Then the response status code should be 200
    And the response body path "firstName" should be "Updated"
    And the response body path "lastName" should be "Name"

  @update @cleanup_user
  Scenario: Successfully update multiple fields at once
    Given a request is sent to create a user with firstName "Multi", lastName "Field", and email "update.multi.field@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to update the user with the following fields:
      | firstName | UpdatedFirst   |
      | lastName  | UpdatedLast    |
      | phone     | +628111222333  |
    Then the response status code should be 200
    And the response body path "firstName" should be "UpdatedFirst"
    And the response body path "lastName" should be "UpdatedLast"
    And the response body path "phone" should be "+628111222333"

  @update @cleanup_user
  Scenario: Successfully update nested location object
    Given a request is sent to create a user with firstName "Location", lastName "Test", and email "update.location.field@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to update the user location with street "Jl. Merdeka No. 10", city "Jakarta", state "DKI Jakarta", country "Indonesia", and timezone "+7:00"
    Then the response status code should be 200
    And the response body path "location.street" should be "Jl. Merdeka No. 10"
    And the response body path "location.city" should be "Jakarta"
    And the response body path "location.timezone" should be "+7:00"

  @update @negative @exploratory @cleanup_user
  Scenario: Attempt to update the forbidden email field is ignored
    Given a request is sent to create a user with firstName "Locked", lastName "Email", and email "locked.email.original@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to update the user email to "locked.email.changed@mail.com"
    Then the response status code should be 200
    When user details are requested by that ID
    Then the response body path "email" should be "locked.email.original@mail.com"

  @update @negative
  Scenario: Fail to update user with non-existent ID
    When a request is sent to update user with non-existent ID "60d0fe4f5311236168a109ca"
    Then the response status code should be 400
    And the response error should be "BODY_NOT_VALID"

  @update @negative
  Scenario: Fail to update user with invalid ID format
    When a request is sent to update user with invalid format ID "invalid_user_id_123"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

  @update @exploratory @cleanup_user
  Scenario: Update accepts firstName below documented minimum length
    Given a request is sent to create a user with firstName "Boundary", lastName "Test", and email "update.boundary.negative@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to update the user with the following fields:
      | firstName | A |
    Then the response status code should be 200
    And the response body path "firstName" should be "A"

  @delete
  Scenario: Successfully delete an existing user and verify it is gone
    Given a request is sent to create a user with firstName "ToDelete", lastName "User", and email "to.delete.verify@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to delete the user by that ID
    Then the response status code should be 200
    And the response body path "id" should match the created user id
    When user details are requested by that ID
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  @delete @negative
  Scenario: Fail to delete non-existent user
    When a request is sent to delete user with non-existent ID "60d0fe4f5311236168a109ca"
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"

  @delete @negative
  Scenario: Fail to delete user with invalid ID format
    When a request is sent to delete user with invalid format ID "invalid_user_id_123"
    Then the response status code should be 400
    And the response error should be "PARAMS_NOT_VALID"

  @delete @negative
  Scenario: Fail to delete the same user twice
    Given a request is sent to create a user with firstName "DoubleDelete", lastName "User", and email "double.delete.check@mail.com"
    And the response status code should be 200
    And the created user should have valid id
    When a request is sent to delete the user by that ID
    Then the response status code should be 200
    When a request is sent to delete the user by that ID
    Then the response status code should be 404
    And the response error should be "RESOURCE_NOT_FOUND"