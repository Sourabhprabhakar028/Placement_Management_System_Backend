package com.placement.placement.service;

import com.placement.placement.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyRefreshToken(String token);
    void deleteRefreshToken(String email);
}
