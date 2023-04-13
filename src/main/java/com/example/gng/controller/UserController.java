package com.example.gng.controller;

import com.example.gng.dto.CreateUserDto;
import com.example.gng.dto.LoginUserDto;
import com.example.gng.model.User;
import com.example.gng.security.JwtUtils;
import com.example.gng.service.BCryptService;
import com.example.gng.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptService bCryptService;

    @GetMapping("/{id}")
    public ResponseEntity <Object> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("ok", false,"error", "user_not_found"));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("ok", true, "user", user));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("ok", false, "error", e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity <Object> createUserWithRole(@Valid @RequestBody CreateUserDto createUserDto) {
        try {
            User user = userService.createUserWithRole(createUserDto);
            String token = jwtUtils.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("ok", true,"user", user, "token", token));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("ok", false, "error", "user_exists"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("ok", false, "error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity <Object> login(@Valid @RequestBody LoginUserDto loginUserDto) {
        try {
            User user = userService.findByEmail(loginUserDto.getUsername());

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("ok", false,"error", "user_not_exists"));
            }

            Boolean isValid = bCryptService.decode(loginUserDto.getPassword(), user.getPassword());

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("ok", false, "error", "user_password_not_match"));
            }

            String token = jwtUtils.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("ok", true, "token", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("ok", false, "error", e.getMessage()));
        }
    }
}
