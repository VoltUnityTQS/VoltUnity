Feature: Station maintenance management

  Scenario: Mark a station as under maintenance
    Given a station "Maintenance Test Station" exists with status "ACTIVE"
    When I mark the station "Maintenance Test Station" as "IN_MAINTENANCE"
    Then the station "Maintenance Test Station" should have status "IN_MAINTENANCE"