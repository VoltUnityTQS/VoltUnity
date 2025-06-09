Feature: Station search by filters

  Scenario: Search stations by charger type and availability
    Given a station "Fast CCS Station" with type "CCS" and 1 available slot exists
    And a station "Slow Type2 Station" with type "TYPE2" and no available slots exists
    When I search for stations with type "CCS" and availability "true"
    Then I should find 1 station named "Fast CCS Station"