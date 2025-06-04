package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {

    @Autowired
    private SlotRepository slotRepository;

    public Slot saveSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    public List<Slot> getAllSlots() {
        return slotRepository.findAll();
    }

    public Slot updateSlotStatus(Long slotId, String newStatus) {
        Slot slot = getSlotById(slotId);
        slot.setSlotStatus(newStatus);
        return slotRepository.save(slot);
    }

    public Slot getSlotById(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slot not found with id: " + id));
    }

    public Slot updateSlot(Slot slot) {
        return slotRepository.save(slot);
    }

    public List<Slot> getSlotsByStation(Station station) {
        return slotRepository.findByStation(station);
    }

    
}