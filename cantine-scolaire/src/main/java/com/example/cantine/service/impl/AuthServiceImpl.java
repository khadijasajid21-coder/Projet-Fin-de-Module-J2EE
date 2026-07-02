package com.example.cantine.service.impl;

import com.example.cantine.dto.*;
import com.example.cantine.security.*;
import com.example.cantine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        UserDetailsImpl ud = (UserDetailsImpl) auth.getPrincipal();
        String token = jwtUtils.generateToken(auth);
        String role = ud.getAuthorities().stream().findFirst().map(a -> a.getAuthority()).orElse("");
        return LoginResponse.builder()
            .token(token).type("Bearer")
            .id(ud.getId()).nom(ud.getNom()).prenom(ud.getPrenom())
            .email(ud.getEmail()).role(role).build();
    }
}
