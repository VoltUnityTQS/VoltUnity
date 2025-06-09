package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.repository.BookingRepository;
//import com.voltunity.evplatform.service.SlotService;
//import com.voltunity.evplatform.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChargingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SlotService slotService;

    @Autowired
    private StationService stationService;

    public boolean unlockSlot(Long bookingId, Long stationId, Long slotId) {
        Station station = stationService.getStationById(stationId);
        Slot slot = slotService.getSlotById(slotId);

        if (!slot.getStation().getId().equals(station.getId())) {
            throw new RuntimeException("Slot não pertence à estação especificada.");
        }

        Optional<Booking> validBooking = bookingRepository.findValidBookingForSlot(slot, LocalDateTime.now());

        if (validBooking.isPresent()) {
            if (!validBooking.get().getId().equals(bookingId)) {
                throw new RuntimeException("Reserva inválida para este slot.");
            }

            slot.setSlotStatus("IN_USE");
            slotService.updateSlot(slot);
            return true;
        }

        if (slot.getSlotStatus().equalsIgnoreCase("AVAILABLE")) {
            slot.setSlotStatus("IN_USE");
            slotService.updateSlot(slot);
            return true;
        }

        throw new RuntimeException("Slot não pode ser desbloqueado no momento.");
    }
}