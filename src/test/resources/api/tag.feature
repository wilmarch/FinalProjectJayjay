@api
Feature: Tag API DummyAPI

  Scenario: Successfully get list of tags
    When I send request to get list of tags
    Then the response status code should be 200
    And the tags list should not be empty

  Scenario: Get tags without app-id header
    When I send request to get list of tags without app-id
    Then the response status code should be 403
    And the response error should be "APP_ID_MISSING"

  Scenario: Fail to get tags with invalid or different app-id
    When I send request to get list of tags with app-id "63a804408eb0cb069b57e9990"
    Then the response status code should be 403
    And the response error should be "APP_ID_NOT_EXIST"

  Scenario: Fail to get tags with invalid endpoint path
    When I send request to an invalid tags endpoint "/tags"
    Then the response status code should be 404