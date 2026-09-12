package com.placement.placement.service.impl;

import com.placement.placement.entity.RefreshToken;
import com.placement.placement.entity.User;
import com.placement.placement.repository.RefreshTokenRepository;
import com.placement.placement.repository.UserRepository;
import com.placement.placement.security.JwtUtil;
import com.placement.placement.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository,
                                   JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        log.info("Creating refresh token for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        // ✅ Delete existing refresh token if any
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush(); // ✅ Force delete before insert

        // Create new refresh token
        RefreshToken refreshToken = new RefreshToken(
                jwtUtil.generateRefreshToken(),
                Instant.now().plusMillis(jwtUtil.getRefreshExpiration()),
                user
        );

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for email: {}", email);
        return saved;
    }

    @Override
    public RefreshToken verifyRefreshToken(String token) {
        log.info("Verifying refresh token");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.error("Refresh token not found");
                    return new RuntimeException("Refresh token not found!");
                });

        // Check if expired
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            log.error("Refresh token expired for user: {}",
                    refreshToken.getUser().getEmail());
            throw new RuntimeException("Refresh token expired! Please login again.");
        }

        log.info("Refresh token valid for user: {}",
                refreshToken.getUser().getEmail());
        return refreshToken;
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String email) {
        log.info("Deleting refresh token for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        refreshTokenRepository.deleteByUser(user);
        log.info("Refresh token deleted for email: {}", email);
    }
}
