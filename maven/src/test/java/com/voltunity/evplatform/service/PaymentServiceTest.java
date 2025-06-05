
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.Payment;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment_shouldCreatePayment() {
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.createPayment(1L, 50.0, "EUR");

        assertEquals("COMPLETED", result.getPaymentStatus());
        assertEquals(50.0, result.getAmount());
        assertEquals("EUR", result.getCurrency());
        assertEquals(user, result.getUser());
    }
}