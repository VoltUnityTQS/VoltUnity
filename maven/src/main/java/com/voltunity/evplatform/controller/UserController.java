package com.voltunity.evplatform.controller;

import com.voltunity.evplatform.dto.UserDTO;
import com.voltunity.evplatform.model.User;
import com.voltunity.evplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        UserDTO userDTO = new UserDTO(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
        return ResponseEntity.ok(userDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}