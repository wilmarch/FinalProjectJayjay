@web @cart
Feature: Cart Page

  Background:
    Given user is on the DemoBlaze homepage

  @cart-verify-item @positive
  Scenario: Verify cart item matches selected product
    Given user has added "Samsung galaxy s6" to the cart
    When user navigates to the cart page
    Then the cart item list should display "Samsung galaxy s6" with the matching price

  @cart-duplicate-items @positive
  Scenario: Verify duplicate items appear as separate rows
    Given user has added "Samsung galaxy s6" to the cart
    And user has added "Samsung galaxy s6" to the cart
    When user navigates to the cart page
    Then the cart should display 2 rows of items

  @cart-total-price @positive
  Scenario: Verify total price equals sum of individual items
    Given user has added "Samsung galaxy s6" to the cart
    And user has added "Nokia lumia 1520" to the cart
    When user navigates to the cart page
    Then the total price should equal the sum of all individual item prices

  @cart-remove-single @positive
  Scenario: Remove single item from cart and recalculate total
    Given user has added "Samsung galaxy s6" to the cart
    And user has added "Nokia lumia 1520" to the cart
    When user navigates to the cart page
    And user deletes item "Samsung galaxy s6" from cart
    Then "Samsung galaxy s6" should not be visible in cart
    And the total price should equal the sum of all individual item prices

  @cart-remove-all @positive
  Scenario: Remove all items from cart
    Given user has added "Samsung galaxy s6" to the cart
    And user has added "Nokia lumia 1520" to the cart
    When user navigates to the cart page
    And user deletes all items from cart
    Then the cart should be completely empty

  @cart-persistence @exploratory
  Scenario: Cart persistence after page refresh
    Given user has added "Samsung galaxy s6" to the cart
    When user navigates to the cart page
    And user refreshes the browser page
    Then the cart should still retain "Samsung galaxy s6"

  @cart-checkout-empty @bug-documentation
  Scenario: Document checkout modal behavior on empty cart
    Given the cart is empty
    When user proceeds to place order
    Then document if checkout modal is allowed to open without items