package com.example.gng.controller;

import com.example.gng.dto.UserDto;
import com.example.gng.model.User;
import com.example.gng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity <User> createUserWithRole(@Valid @RequestBody UserDto userDto) {
        User user = userService.createUserWithRole(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
