package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SlotService slotService;

    public Station saveStation(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station updateStationStatus(Long stationId, String newStatus) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Station not found with id: " + stationId));

        station.setStationStatus(newStatus);
        return stationRepository.save(station);
    }

    public Station getStationById(Long id) {
    return stationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
}

    public Station updateStation(Long id, Station updatedStation) {
        Station station = getStationById(id);

        // Atualizar campos (podes personalizar quais queres que sejam editáveis)
        station.setName(updatedStation.getName());
        station.setStationStatus(updatedStation.getStationStatus());
        station.setLat(updatedStation.getLat());
        station.setLng(updatedStation.getLng());
        station.setAddress(updatedStation.getAddress());
        station.setTotalSlots(updatedStation.getTotalSlots());
        station.setMaxPower(updatedStation.getMaxPower());
        station.setChargerTypes(updatedStation.getChargerTypes());

        return stationRepository.save(station);
    }

    public List<Station> searchStations(
            double latitude,
            double longitude,
            double radius,
            String tipoDeCarregador,
            Boolean disponibilidade,
            String name
    ) {
        List<Station> stations = stationRepository.findStationsByLocationAndName(
                latitude, longitude, radius, name
        );

        if (tipoDeCarregador != null && !tipoDeCarregador.isEmpty()) {
            stations = stations.stream()
                    .filter(s -> s.getChargerTypes() != null &&
                            s.getChargerTypes().stream()
                                    .anyMatch(type -> type.equalsIgnoreCase(tipoDeCarregador)))
                    .collect(Collectors.toList());
        }

        if (disponibilidade != null && disponibilidade) {
            stations = stations.stream()
                    .filter(s -> slotService.getSlotsByStation(s).stream()
                            .anyMatch(slot -> slot.getSlotStatus().equalsIgnoreCase("AVAILABLE")))
                    .collect(Collectors.toList());
        }

        return stations;
    }

    public Station updateStationPricing(Long stationId, double newPricePerKWh) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Estação não encontrada"));

        station.setPricePerKWh(newPricePerKWh);
        return stationRepository.save(station);
    }
    
}