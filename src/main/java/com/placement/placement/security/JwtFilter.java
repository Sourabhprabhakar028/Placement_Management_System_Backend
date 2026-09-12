package com.placement.placement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            log.debug("Auth Header: {}", authHeader);

            if (authHeader != null
                    && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7).trim();
                log.debug("Token received for processing");

                if (jwtUtil.validateToken(token)
                        && !jwtUtil.isTokenExpired(token)) {
                    String email = jwtUtil.getEmailFromToken(token);
                    String role = jwtUtil.getRoleFromToken(token);
                    log.debug("Authenticated user: {} with role: {}",
                            email, role);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    List.of(new SimpleGrantedAuthority(
                                            "ROLE_" + role))
                            );
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("JWT Error: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
