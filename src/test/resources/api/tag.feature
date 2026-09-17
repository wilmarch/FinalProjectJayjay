@api
Feature: Tag API DummyAPI

  Scenario: Successfully get list of tags
    When I send request to get list of tags
    Then the response status code should be 200
    And the tags list should not be empty

  Scenario: Fail to get tags with invalid endpoint path
    When I send request to an invalid tags endpoint "/tags"
    Then the response status code should be 404