package com.example.RM_solution.auth.configuration;

import com.example.RM_solution.auth.util.HashUtil;
import com.example.RM_solution.auth.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfiguration {
    @Bean
    public HashUtil hashUtil(){
        return new HashUtil();
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

}