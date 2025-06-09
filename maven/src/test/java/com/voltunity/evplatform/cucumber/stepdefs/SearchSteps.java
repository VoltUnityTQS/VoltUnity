package com.voltunity.evplatform.cucumber.stepdefs;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.service.StationService;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SearchSteps {

    @Autowired private StationService stationService;
    @Autowired private SlotRepository slotRepository;

    private List<Station> searchResults;

    @Given("a station {string} with type {string} and 1 available slot exists")
    public void aStationAvailable(String name, String type) {
        Station station = new Station(name, "ACTIVE", 0, 0, "Rua A", 1, 22, List.of(type));
        station = stationService.saveStation(station);

        Slot slot = new Slot();
        slot.setStation(station);
        slot.setSlotStatus("AVAILABLE");
        slotRepository.save(slot);
    }

    @Given("a station {string} with type {string} and no available slots exists")
    public void aStationUnavailable(String name, String type) {
        Station station = new Station(name, "ACTIVE", 0, 0, "Rua B", 1, 22, List.of(type));
        station = stationService.saveStation(station);

        Slot slot = new Slot();
        slot.setStation(station);
        slot.setSlotStatus("IN_USE");
        slotRepository.save(slot);
    }

    @When("I search for stations with type {string} and availability {string}")
    public void iSearch(String type, String availability) {
        boolean disp = Boolean.parseBoolean(availability);
        searchResults = stationService.searchStations(0, 0, 1000, type, disp, null);
    }

    @Then("I should find {int} station named {string}")
    public void iShouldFindStation(int count, String name) {
        assertEquals(count, searchResults.size());
        assertTrue(searchResults.stream().anyMatch(s -> s.getName().equals(name)));
    }
}


