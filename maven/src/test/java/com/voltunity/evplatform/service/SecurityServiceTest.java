
package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SecurityServiceTest {

    private SecurityService securityService;

    @BeforeEach
    void setup() {
        securityService = new SecurityService();
    }

    @Test
    void testIsAdmin_shouldReturnTrue() {
        User admin = new User();
        admin.setRole("ADMIN");

        assertTrue(securityService.isAdmin(admin));
    }

    @Test
    void testIsAdmin_shouldReturnFalse() {
        User user = new User();
        user.setRole("USER");

        assertFalse(securityService.isAdmin(user));
    }

    @Test
    void testIsSameUser_shouldReturnTrue() {
        User user = new User();
        user.setId(5L);

        assertTrue(securityService.isSameUser(user, 5L));
    }

    @Test
    void testIsSameUser_shouldReturnFalse() {
        User user = new User();
        user.setId(3L);

        assertFalse(securityService.isSameUser(user, 7L));
    }
}