@web @misc
Feature: Navbar Links and Miscellaneous Modals

  Background:
    Given user is on the DemoBlaze homepage

  @contact-empty-fields @bug-documentation
  Scenario: Send contact message with empty inputs
    When user clicks "Contact" navbar link
    And user submits contact message with all blank fields
    Then document if system alerts "Thanks for the message!!" despite empty inputs

  @about-us-modal @positive
  Scenario: About Us modal and video player display
    When user clicks "About us" navbar link
    Then the About Us modal should appear
    And the video player should be present inside the modal

  @brand-logo-navigation @positive
  Scenario: Click logo or Home redirects to index page
    When user navigates to the cart page
    And user clicks the site brand logo
    Then user should be redirected to the homepage product grid

  @back-to-home @positive
  Scenario: Navigate back to homepage from detail page
    Given user is on the detail page for product "Nokia lumia 1520"
    When user clicks the Home link in navbar
    Then user should be redirected to the homepage product grid

  @contact-valid @positive
  Scenario: Submit contact message with valid details
    When user clicks "Contact" navbar link
    And user fills contact form with valid details:
      | Email   | testuser@example.com           |
      | Name    | John Doe                       |
      | Message | Hello, this is a test message. |
    And user submits the contact message
    Then an alert should appear with message "Thanks for the message!!"