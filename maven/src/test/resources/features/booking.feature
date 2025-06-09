Feature: Booking Slot

  Scenario: Create a valid booking
    Given a station with available slots exists
    And a registered user exists
    When I book a slot from "2025-06-10T10:00" to "2025-06-10T11:00"
    Then the booking should be confirmed

  Scenario: Attempt to book with invalid time range
    Given a station with available slots exists
    And a registered user exists
    When I book a slot from "2025-06-10T12:00" to "2025-06-10T10:00"
    Then an error should occur with message "Intervalo de tempo inválido"

  Scenario: Attempt to book when no slots are available
    Given a station without available slots exists
    And a registered user exists
    When I book a slot from "2025-06-10T10:00" to "2025-06-10T11:00"
    Then an error should occur with message "Nenhum slot disponível na estação para o período selecionado"

  Scenario: Cancel a confirmed booking
    Given a confirmed booking exists
    When I cancel the booking
    Then the booking status should be "cancelled"
    And the associated slot should be "AVAILABLE"