package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.model.Subscription;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.service.SubscriptionService;
import com.voltunity.evplatform.service.SecurityService;
import com.voltunity.evplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    public static class SubscriptionRequest {
        public Long userId;
        public String subscriptionType;
        public LocalDateTime startDate;
        public LocalDateTime endDate;
        public double pricePerMonth;
        public int sessionsIncluded;
        public double discountPerKWh;
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody SubscriptionRequest request) {
        Subscription subscription = subscriptionService.createSubscription(
                request.userId,
                request.subscriptionType,
                request.startDate,
                request.endDate,
                request.pricePerMonth,
                request.sessionsIncluded,
                request.discountPerKWh
        );

        return new ResponseEntity<>(subscription, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions());
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(
            @PathVariable Long userId,
            @RequestHeader("X-User-Id") Long currentUserId
    ) {
        User currentUser = userService.getUserById(currentUserId);

        if (securityService.isAdmin(currentUser) || securityService.isSameUser(currentUser, userId)) {
            return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(userId));
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}