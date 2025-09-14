package net.mysterria.archive.config;

import lombok.RequiredArgsConstructor;
import net.mysterria.archive.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/archive/items/**",
                                "/archive/pathways/**",
                                "/archive/types/**",
                                "/archive/researchers/**",
                                "/archive/comments/**",
                                "/archive/mvc/**",
                                "/archive/mvc/",
                                "/archive/mvc"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/archive/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/archive/v3/api-docs/**",
                                "/actuator/health"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/archive/**").hasAuthority("PERM_ARCHIVE:WRITE")
                        .requestMatchers(HttpMethod.PUT, "/archive/**").hasAuthority("PERM_ARCHIVE:WRITE")
                        .requestMatchers(HttpMethod.DELETE, "/archive/**").hasAuthority("PERM_ARCHIVE:MODERATE")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}