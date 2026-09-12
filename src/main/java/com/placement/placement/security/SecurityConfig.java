package com.placement.placement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // ✅ AuthenticationManager Bean for AuthController
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ✅ Public Endpoints
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        // ✅ ADMIN-only write operations
                        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/companies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/companies/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/companies/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/placements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/placements/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/placements/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/applications/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/applications/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/interviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/interviews/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/interviews/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/offers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/offers/**").hasRole("ADMIN")

                        // ✅ ADMIN + STUDENT — Read & General access
                        .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/companies/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/placements/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/applications/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/applications/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/interviews/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.GET, "/offers/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/resumes/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/profiles/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/reports/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/export/**").hasAnyRole("ADMIN", "STUDENT")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔥 IMPROVED CORS CONFIGURATION — Fixes Backend Connection Error
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed Origin Patterns All (*) to allow Live Server (5500), React (3000), Vite (5173), etc.
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept", "X-Requested-With"
        ));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}