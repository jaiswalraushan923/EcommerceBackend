package com.ApiProject.EcommerceBackend.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {

    private JWTRequestFilter jwtRequestFilter;

    public WebSecurityConfig(JWTRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.csrf(csrf->csrf.disable()).cors(cors->cors.disable());
////        http.addFilterBefore(addFilterBefore-> addFilterBefore(jwtRequestFilter, AuthenticationFilter.class));
//        http.addFilterBefore(jwtRequestFilter, AuthenticationFilter.class);
//        http.authorizeHttpRequests(authorize->authorize.anyRequest().authenticated());
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/product").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/verify").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/forgot").permitAll()
                        .requestMatchers("/auth/reset").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

}
