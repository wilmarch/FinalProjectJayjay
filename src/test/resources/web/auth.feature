@web @auth
Feature: Authentication and User Management

  Background:
    Given user is on the DemoBlaze homepage

  @signup-unique-user @positive
  Scenario: Sign up with a unique dynamic username
    When user opens the sign up modal
    And user registers with a dynamically generated username and password "Secret123!"
    Then an alert should appear with message "Sign up successful."
    And user accepts the alert

  @signup-existing-user @negative
  Scenario: Sign up with an existing username
    When user opens the sign up modal
    And user registers with username "existing_user_test" and password "Secret123!"
    Then an alert should appear with message "This user already exist."
    And user accepts the alert

  @signup-blank-credentials @negative
  Scenario Outline: Sign up with blank credentials
    When user opens the sign up modal
    And user registers with username "<username>" and password "<password>"
    Then an alert should appear with message "Please fill out Username and Password."
    And user accepts the alert

    Examples:
      | username    | password   |
      |             | Secret123! |
      | testuser999 |            |
      |             |            |

  @signup-weak-password @exploratory @known-weakness
  Scenario: Sign up with weak single-character password
    When user opens the sign up modal
    And user registers with a dynamically generated username and password "1"
    Then an alert should appear with message "Sign up successful."
    And user accepts the alert

  @signup-close-modal @positive
  Scenario: Close sign up modal without changes
    When user opens the sign up modal
    And user closes the sign up modal
    Then the sign up modal should no longer be visible

  @login-success @positive
  Scenario: Login with valid credentials
    When user opens the login modal
    And user logs in with username "test" and password "test"
    Then the navbar should display "Welcome test"
    And the logout button should be visible

  @login-wrong-password @negative
  Scenario: Login with incorrect password
    When user opens the login modal
    And user logs in with username "valid_registered_user" and password "WrongPassword!"
    Then an alert should appear with message "User does not exist."
    And user accepts the alert

  @login-non-existent @negative
  Scenario: Login with non-existent username
    When user opens the login modal
    And user logs in with non-existent random username and password "RandomPass123!"
    Then an alert should appear with message "User does not exist."
    And user accepts the alert

  @login-blank-credentials @negative
  Scenario Outline: Login with blank credentials
    When user opens the login modal
    And user logs in with username "<username>" and password "<password>"
    Then an alert should appear with message "Please fill out Username and Password."
    And user accepts the alert

    Examples:
      | username        | password       |
      |                 | ValidPassword! |
      | registered_user |                |
      |                 |                |

  @logout-session @positive
  Scenario: Logout from authenticated session
    Given user is logged in with username "test" and password "test"
    When user clicks the logout button
    Then the navbar should display the "Log in" link
    And the navbar should display the "Sign up" link
