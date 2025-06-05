
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}