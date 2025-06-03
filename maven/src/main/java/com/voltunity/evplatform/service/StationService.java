package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    @Autowired
    private StationRepository stationRepository;

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
}