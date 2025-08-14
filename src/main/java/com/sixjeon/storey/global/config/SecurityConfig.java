package com.sixjeon.storey.global.config;

import com.sixjeon.storey.global.security.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->  session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/owner/**").hasRole("OWNER")
                                .requestMatchers("/store/**").hasRole("OWNER")
                                .anyRequest().permitAll()
                )

                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
