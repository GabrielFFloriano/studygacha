package com.gacha.studygacha.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest (
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 50, message = "Username must contain between 3 and 50 characters")
        String username
) {
}
