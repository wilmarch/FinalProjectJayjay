@api @security
Feature: Global Authentication Security API

  @security-missing-app-id
  Scenario Outline: Fail to access endpoint without app-id header
    When a request is sent to "<endpoint>" without app-id
    Then the response status code should be 403
    And the response error should be "APP_ID_MISSING"

    Examples:
      | endpoint |
      | /tag     |
      | /user    |

  @security-invalid-app-id
  Scenario Outline: Fail to access endpoint with invalid app-id
    When a request is sent to "<endpoint>" with invalid app-id "invalid_token_123"
    Then the response status code should be 403
    And the response error should be "APP_ID_NOT_EXIST"

    Examples:
      | endpoint |
      | /tag     |
      | /user    |

  @security-create-without-app-id
  Scenario: Fail to create user without app-id header
    When a request is sent to create a user without app-id
    Then the response status code should be 403
    And the response error should be "APP_ID_MISSING"
