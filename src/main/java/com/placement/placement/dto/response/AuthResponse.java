package com.placement.placement.dto.response;

public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String email;
    private String role;
    private String message;

    public AuthResponse() {}

    // ✅ Constructor with refresh token (Login/Register success)
    public AuthResponse(String accessToken, String refreshToken,
                        String email, String role, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
        this.role = role;
        this.message = message;
    }

    // ✅ Constructor for errors (message only)
    public AuthResponse(String message) {
        this.message = message;
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
