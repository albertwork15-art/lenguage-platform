package com.SafariTalk.lenguage_platform.dto.response;

public record AuthResponse(
        String accessToken, String tokenType, long expiresInSeconds, Long userId, String email, String role) {

    public static AuthResponse of(String accessToken, long expiresInSeconds, Long userId, String email, String role) {
        return new AuthResponse(accessToken, "Bearer", expiresInSeconds, userId, email, role);
    }
}
