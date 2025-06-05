package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Payment;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.service.PaymentService;
import com.voltunity.evplatform.service.SecurityService;
import com.voltunity.evplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    public static class PaymentRequest {
        public Long userId;
        public double amount;
        public String currency;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody PaymentRequest request) {
        Payment payment = paymentService.createPayment(request.userId, request.amount, request.currency);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUser(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);

        if (securityService.isAdmin(currentUser) || securityService.isSameUser(currentUser, userId)) {
            return ResponseEntity.ok(paymentService.getPaymentsByUser(userId));
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}