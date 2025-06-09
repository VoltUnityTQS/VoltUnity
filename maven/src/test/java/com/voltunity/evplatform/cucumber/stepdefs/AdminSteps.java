package com.voltunity.evplatform.cucumber.stepdefs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.service.StationService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest
public class AdminSteps {

    @Autowired
    private StationService stationService;

    @Given("a station {string} exists with status {string}")
    public void aStationExistsWithStatus(String name, String status) {
        Station station = new Station();
        station.setName(name);
        station.setStationStatus(status);
        station.setLat(0.0f);
        station.setLng(0.0f);
        station.setChargerTypes(List.of("CCS"));
        stationService.saveStation(station);
    }

    @When("I mark the station {string} as {string}")
    public void iMarkTheStationAs(String name, String newStatus) {
        List<Station> all = stationService.getAllStations();
        Station target = all.stream()
            .filter(s -> s.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Station not found"));
        stationService.updateStationStatus(target.getId(), newStatus);
    }

    @Then("the station {string} should have status {string}")
    public void theStationShouldHaveStatus(String name, String expectedStatus) {
        List<Station> all = stationService.getAllStations();
        Station target = all.stream()
            .filter(s -> s.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Station not found"));
        assertEquals(expectedStatus, target.getStationStatus());
    }
}