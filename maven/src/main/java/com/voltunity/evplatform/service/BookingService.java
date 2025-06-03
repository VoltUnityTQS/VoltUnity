package com.voltunity.evplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.Slot;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.BookingRepository;
import com.voltunity.evplatform.repository.SlotRepository;
import com.voltunity.evplatform.repository.StationRepository;
import com.voltunity.evplatform.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(Long stationId, LocalDateTime start, LocalDateTime end, Long userId) {
        if (start.isAfter(end) || start.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Intervalo de tempo inválido");
        }

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new RuntimeException("Estação não encontrada"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));

        List<Slot> slots = slotRepository.findByStation(station);
        for (Slot slot : slots) {
            List<Booking> overlappingBookings = bookingRepository.findBySlotAndTimeOverlap(slot, start, end);
            if (overlappingBookings.isEmpty() && slot.getSlotStatus().equalsIgnoreCase("AVAILABLE")) {
                Booking booking = new Booking();
                booking.setUser(user);
                booking.setSlot(slot);
                booking.setStart(start);
                booking.setEnd(end);
                booking.setBookingStatus("confirmed");
                booking.setPriceAtBooking(0.0f); // Placeholder, pode ser calculado com base em tarifas
                slot.setSlotStatus("IN_USE"); // Atualizar status do slot
                slotRepository.save(slot);
                return bookingRepository.save(booking);
            }
        }

        throw new RuntimeException("Nenhum slot disponível na estação para o período selecionado");
    }

    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}