package com.example.gng.controller;

import com.example.gng.dto.UserDto;
import com.example.gng.model.User;
import com.example.gng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity <Object> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("error", "user_not_found");
            return ResponseEntity.internalServerError().body(response);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping
    public ResponseEntity <Object> createUserWithRole(@Valid @RequestBody UserDto userDto) {
        try {
            User user = userService.createUserWithRole(userDto);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", errorMessage);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
