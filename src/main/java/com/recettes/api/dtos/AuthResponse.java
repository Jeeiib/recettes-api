package com.recettes.api.dtos;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}
