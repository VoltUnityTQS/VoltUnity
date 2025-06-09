package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Payment;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserService userService;

    public Payment createPayment(Long userId, double amount, String currency) {
        User user = userService.getUserById(userId);

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setTimestamp(LocalDateTime.now());
        payment.setPaymentStatus("COMPLETED"); // mock → depois podes integrar com gateway real

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUser_Id(userId);
    }
}