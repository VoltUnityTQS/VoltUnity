Feature: Booking with payment

  Scenario: Create a booking with payment
    Given a station with available slots exists
    And a registered user exists
    When the user pays "7.50" EUR
    And the user books a slot from "2025-06-12T14:00" to "2025-06-12T15:00"
    Then the booking should be confirmed
    And the booking price should be 7.50