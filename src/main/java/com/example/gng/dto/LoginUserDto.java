package com.example.gng.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class LoginUserDto {
    @NotEmpty(message = "empty_username")
    private String username;

    @NotEmpty(message = "empty_password")
    private String password;
}
