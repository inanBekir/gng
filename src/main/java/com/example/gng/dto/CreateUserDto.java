package com.example.gng.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CreateUserDto {
    @Email(message = "invalid_email")
    private String email;

    @Size(min = 8, max = 20, message = "invalid_password_size")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "invalid_password_pattern")
    private String password;

    @NotEmpty(message = "empty_type")
    private String type;

    @NotEmpty(message = "empty_roles")
    private Set<String> roles;
}
