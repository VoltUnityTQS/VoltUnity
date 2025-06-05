
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Subscription;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubscription_shouldCreateSubscription() {
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.createSubscription(
                1L, "PREMIUM",
                LocalDateTime.now(), LocalDateTime.now().plusMonths(1),
                15.0, 10, 0.10
        );

        assertEquals("PREMIUM", result.getSubscriptionType());
        assertEquals(user, result.getUser());
        assertEquals(15.0, result.getPricePerMonth());
        assertEquals(10, result.getSessionsIncluded());
    }
}