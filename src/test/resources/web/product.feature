@web @products
Feature: Catalog Navigation and Product Detail Page

  Background:
    Given user is on the DemoBlaze homepage

  @category-filter @positive
  Scenario Outline: Filter products by category
    When user selects category "<category>"
    Then only products belonging to category "<category>" should be displayed

    Examples:
      | category |
      | Phones   |
      | Laptops  |
      | Monitors |

  @pagination @positive
  Scenario: Navigate products using pagination
    When user clicks the "Next" pagination button
    Then the product list should update with page 2 items

  @product-title-navigation @positive
  Scenario: Navigate to product detail via product name
    When user clicks on a product "Samsung galaxy s6"
    Then the product detail page should display title "Samsung galaxy s6" and corresponding price

  @thumbnail-click @bug-documentation
  Scenario: Clicking product image card navigates to detail
    When user clicks on the image thumbnail for product "Samsung galaxy s6"
    Then the page should redirect to product detail page or document failure if unlinked

  @product-attributes @positive
  Scenario: Product detail displays complete attributes
    When user clicks on a product "Nokia lumia 1520"
    Then the product detail page should display name, price, and description

  @add-to-cart-alert @positive
  Scenario: Add product to cart triggers confirmation
    Given user is on the detail page for product "Nokia lumia 1520"
    When user adds the product to cart
    Then an alert should appear with message "Product added"