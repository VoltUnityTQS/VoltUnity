package com.voltunity.evplatform.service;

import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voltunity.evplatform.dto.UserDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
            .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole()))
            .collect(Collectors.toList());
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}