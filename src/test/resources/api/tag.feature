@api @tag
Feature: Tag API DummyAPI

  @tag-get-list-success
  Scenario: Successfully get list of tags
    When the list of tags is requested
    Then the response status code should be 200
    And the tags list should not be empty

  @tag-get-invalid-endpoint
  Scenario: Fail to get tags with invalid endpoint path
    When a request is sent to an invalid tags endpoint "/tags"
    Then the response status code should be 404
