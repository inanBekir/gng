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
        User user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        if (user == null) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("error", "user_not_found");
            return ResponseEntity.internalServerError().body(response);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("user", user);
            return ResponseEntity.ok().body(response);
        }
    }

    @PostMapping
    public ResponseEntity <Object> createUserWithRole(@Valid @RequestBody UserDto userDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.createUserWithRole(userDto);
            response.put("status", HttpStatus.CREATED.value());
            response.put("user", user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("error", errorMessage);
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
