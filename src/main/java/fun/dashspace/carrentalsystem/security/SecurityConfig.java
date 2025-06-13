package fun.dashspace.carrentalsystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Value("${security.cors.allowed-origins}")
    private String allowedOrigins;

    private final String[] PUBLIC_PATHS = {
            "/locations/**",
            "/auth/**",
            "/cars/search",
            "/swagger-ui/**",
            "/v3/api-docs/**",
    };

    private final String[] USER_PATHS = {
            "/users/account/**",
            "/users/bookings/**",
            "/profile/**",
    };

    private final String[] HOST_PATHS = {
            "/portal/cars/**",
            "/portal/bookings/**",
            "/portal/datacenter/**",
    };

    private final String[] ADMIN_PATHS = {
            "/admin/**",
            "/users/all",
            "/reports/**",
            "/datacenter/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_PATHS).permitAll()
                        .requestMatchers(USER_PATHS).hasAnyRole("RENTER", "HOST", "ADMIN")
                        .requestMatchers(HOST_PATHS).hasRole("HOST")
                        .requestMatchers(ADMIN_PATHS).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(this::handleAuthenticationError)
                        .accessDeniedHandler(this::handleAccessDeniedError)
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    private void handleAuthenticationError(
            HttpServletRequest req,
            HttpServletResponse res,
            AuthenticationException ex) throws IOException {
        String message = switch (ex) {
            case DisabledException ignored -> "Account is disabled";
            case BadCredentialsException ignored -> "Invalid email or password";
            case UsernameNotFoundException ignored -> "User not found";
            default -> "Authentication failed";
        };

        sendErrorResponse(res, HttpStatus.UNAUTHORIZED, message);
    }

    public void handleAccessDeniedError(
            HttpServletRequest req,
            HttpServletResponse res,
            AccessDeniedException ex) throws IOException {
        sendErrorResponse(res, HttpStatus.FORBIDDEN, "Access denied");
    }

    private void sendErrorResponse(
            HttpServletResponse res,
            HttpStatus status,
            String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var errorBody = Map.of(
                "success", false,
                "status", status.value(),
                "message", message,
                "error", status.getReasonPhrase(),
                "timestamp", Instant.now().toString()
        );
        new ObjectMapper().writeValue(res.getOutputStream(), errorBody);
    }
}
