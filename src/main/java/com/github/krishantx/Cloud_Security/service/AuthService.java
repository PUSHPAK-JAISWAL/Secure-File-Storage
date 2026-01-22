package com.github.krishantx.Cloud_Security.service;

import com.github.krishantx.Cloud_Security.dto.RegisterRequest;
import com.github.krishantx.Cloud_Security.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService uds;
    private final JwtUtil jwtUtil;

    @Autowired
    private AuthService(AuthenticationManager authManager, CustomUserDetailsService uds, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.uds = uds;
        this.jwtUtil = jwtUtil;
    }

    public String login(RegisterRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails ud = uds.loadUserByUsername(request.getUsername());
        return jwtUtil.generateToken(ud.getUsername());
    }
}