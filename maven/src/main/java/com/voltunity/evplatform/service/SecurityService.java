package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.User;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public boolean isAdmin(User user) {
        return user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN");
    }

    public boolean isSameUser(User currentUser, Long targetUserId) {
        return currentUser != null && currentUser.getId().equals(targetUserId);
    }
}