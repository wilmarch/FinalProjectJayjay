@web @checkout
Feature: Checkout Order

  Background:
    Given user is on the DemoBlaze homepage
    And user has added "Samsung galaxy s6" to the cart
    And user navigates to the cart page

  @checkout-single-item @positive
  Scenario: Complete order successfully with single item
    When user proceeds to place order
    And user fills order form with valid details:
      | Name     | John Doe     |
      | Country  | Indonesia    |
      | City     | Jakarta      |
      | Card     | 411111111111 |
      | Month    | 12           |
      | Year     | 2028         |
    And user submits the purchase
    Then the purchase confirmation popup should appear
    And the confirmation should display matching Name, Card, and Total Amount
    When user clicks OK on confirmation popup
    Then the cart should be completely empty

  @checkout-multi-item @positive
  Scenario: Complete order successfully with multiple items
    Given user has added "Nokia lumia 1520" to the cart
    When user navigates to the cart page
    And user proceeds to place order
    And user fills order form with valid details:
      | Name     | Jane Doe     |
      | Country  | Indonesia    |
      | City     | Bandung      |
      | Card     | 411122223333 |
      | Month    | 11           |
      | Year     | 2029         |
    And user submits the purchase
    Then the purchase confirmation popup should appear
    And the confirmation should display matching Name, Card, and Total Amount
    When user clicks OK on confirmation popup
    Then the cart should be completely empty

  @checkout-empty-fields @negative
  Scenario: Attempt checkout without filling any fields
    When user proceeds to place order
    And user submits the purchase without filling any field
    Then an alert should appear with message "Please fill out Name and Creditcard."

  @checkout-invalid-data @bug-documentation
  Scenario Outline: Verify system behavior with invalid payment inputs
    When user proceeds to place order
    And user fills order form with:
      | Name     | John Doe     |
      | Country  | Indonesia    |
      | City     | Jakarta      |
      | Card     | <card>       |
      | Month    | <month>      |
      | Year     | <year>       |
    And user submits the purchase
    Then verify if the system accepts invalid input or shows rejection

    Examples:
      | card        | month | year |
      | invalid_num | 12    | 2028 |
      | 41111111    | 99    | 2028 |
      | 41111111    | 05    | 2010 |