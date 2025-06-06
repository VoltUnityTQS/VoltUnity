package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.StationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StationServiceTest {

    @Mock
    private StationRepository stationRepository;

    @Mock
    private SlotService slotService;

    @InjectMocks
    private StationService stationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveStation_shouldCallRepositorySave() {
        Station station = new Station();
        when(stationRepository.save(station)).thenReturn(station);

        Station result = stationService.saveStation(station);

        assertEquals(station, result);
        verify(stationRepository).save(station);
    }

    @Test
    void testGetAllStations_shouldReturnList() {
        List<Station> stations = Arrays.asList(new Station(), new Station());
        when(stationRepository.findAll()).thenReturn(stations);

        List<Station> result = stationService.getAllStations();

        assertEquals(2, result.size());
        verify(stationRepository).findAll();
    }

    @Test
    void testUpdateStationStatus_shouldUpdateAndSave() {
        Station station = new Station();
        station.setStationStatus("OLD_STATUS");

        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(stationRepository.save(any(Station.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Station updated = stationService.updateStationStatus(1L, "NEW_STATUS");

        assertEquals("NEW_STATUS", updated.getStationStatus());
        verify(stationRepository).save(updated);
    }

    @Test
    void testUpdateStationStatus_stationNotFound_throwsException() {
        when(stationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            stationService.updateStationStatus(1L, "NEW_STATUS");
        });

        assertEquals("Station not found with id: 1", ex.getMessage());
    }

    @Test
    void testGetStationById_found() {
        Station station = new Station();
        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));

        Station result = stationService.getStationById(1L);

        assertEquals(station, result);
    }

    @Test
    void testGetStationById_notFound_throwsException() {
        when(stationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            stationService.getStationById(1L);
        });

        assertEquals("Station not found with id: 1", ex.getMessage());
    }

    @Test
    void testUpdateStation_shouldUpdateFieldsAndSave() {
        Station existing = new Station();
        existing.setName("OldName");
        existing.setStationStatus("OLD");
        existing.setLat((float)0.0);
        existing.setLng((float)0.0);
        existing.setAddress("Old Address");
        existing.setTotalSlots(5);
        existing.setMaxPower((float)100.0);
        existing.setChargerTypes(Arrays.asList("type1"));

        Station updated = new Station();
        updated.setName("NewName");
        updated.setStationStatus("NEW");
        updated.setLat((float)1.1);
        updated.setLng((float)2.2);
        updated.setAddress("New Address");
        updated.setTotalSlots(10);
        updated.setMaxPower((float)200.0);
        updated.setChargerTypes(Arrays.asList("type2"));

        when(stationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(stationRepository.save(any(Station.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Station result = stationService.updateStation(1L, updated);

        assertEquals("NewName", result.getName());
        assertEquals("NEW", result.getStationStatus());
        assertEquals((float)1.1, result.getLat());
        assertEquals((float)2.2, result.getLng());
        assertEquals("New Address", result.getAddress());
        assertEquals(10, result.getTotalSlots());
        assertEquals((float)200.0, result.getMaxPower());
        assertEquals(1, result.getChargerTypes().size());
        assertEquals("type2", result.getChargerTypes().get(0));
    }

    @Test
    void testSearchStations_filtersByChargerTypeAndAvailability() {
        Station s1 = new Station();
        s1.setChargerTypes(Arrays.asList("Fast"));

        Station s2 = new Station();
        s2.setChargerTypes(Arrays.asList("Slow"));

        List<Station> stations = Arrays.asList(s1, s2);

        when(stationRepository.findStationsByLocationAndName(anyDouble(), anyDouble(), anyDouble(), anyString()))
                .thenReturn(stations);

        // Mock slotService to simulate availability
        Slot availableSlot = new Slot();
        availableSlot.setSlotStatus("AVAILABLE");

        Slot busySlot = new Slot();
        busySlot.setSlotStatus("IN_USE");

        when(slotService.getSlotsByStation(s1)).thenReturn(Arrays.asList(busySlot, availableSlot));
        when(slotService.getSlotsByStation(s2)).thenReturn(Arrays.asList(busySlot));

        List<Station> result = stationService.searchStations(
                0.0, 0.0, 10.0, "Fast", true, "");

        // s1 has charger type "Fast" and has available slot -> included
        // s2 has "Slow" and no available slot -> excluded
        assertEquals(1, result.size());
        assertEquals(s1, result.get(0));
    }

    @Test
    void testUpdateStationPricing_shouldUpdatePrice() {
        Station station = new Station();
        station.setId(1L);
        station.setPricePerKWh(0.30);

        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(stationRepository.save(any(Station.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Station updated = stationService.updateStationPricing(1L, 0.45);

        assertEquals(0.45, updated.getPricePerKWh());
        verify(stationRepository).save(updated);
    }

    @Test
    void testUpdateStationPricing_stationNotFound_throwsException() {
        when(stationRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            stationService.updateStationPricing(1L, 0.50);
        });

        assertEquals("Estação não encontrada", ex.getMessage());
    }
}
