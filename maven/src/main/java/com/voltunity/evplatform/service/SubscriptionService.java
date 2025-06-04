package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Subscription;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;

    public Subscription createSubscription(Long userId, String subscriptionType, 
                                           java.time.LocalDateTime startDate, 
                                           java.time.LocalDateTime endDate, 
                                           double pricePerMonth, 
                                           int sessionsIncluded, 
                                           double discountPerKWh) {
        User user = userService.getUserById(userId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionType(subscriptionType);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setStatus("ACTIVE");
        subscription.setPricePerMonth(pricePerMonth);
        subscription.setSessionsIncluded(sessionsIncluded);
        subscription.setDiscountPerKWh(discountPerKWh);

        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> getSubscriptionsByUser(Long userId) {
        return subscriptionRepository.findByUser_Id(userId);
    }
}