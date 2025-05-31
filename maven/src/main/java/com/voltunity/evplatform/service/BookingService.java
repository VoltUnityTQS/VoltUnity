package com.voltunity.evplatform.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltunity.evplatform.model.Booking;
import com.voltunity.evplatform.model.ChargerPoint;
import com.voltunity.evplatform.model.Station;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.BookingRepository;
import com.voltunity.evplatform.repository.ChargerPointRepository;
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
    private ChargerPointRepository chargerPointRepository;
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

        List<ChargerPoint> chargerPoints = chargerPointRepository.findByStationId(stationId);
        for (ChargerPoint cp : chargerPoints) {
            List<Booking> overlappingBookings = bookingRepository.findByChargerPointAndTimeOverlap(cp, start, end);
            if (overlappingBookings.isEmpty() && "available".equals(cp.getStatus())) {
                Booking booking = new Booking();
                booking.setUser(user);
                booking.setChargerPoint(cp);
                booking.setStart(start);
                booking.setEnd(end);
                booking.setBookingStatus("confirmed");
                booking.setPriceAtBooking(0.0f); // Placeholder, pode ser calculado com base em tarifas
                cp.setStatus("occupied"); // Atualizar status do slot
                chargerPointRepository.save(cp);
                return bookingRepository.save(booking);
            }
        }

        throw new RuntimeException("Nenhum slot disponível na estação para o período selecionado");
    }

    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }
}