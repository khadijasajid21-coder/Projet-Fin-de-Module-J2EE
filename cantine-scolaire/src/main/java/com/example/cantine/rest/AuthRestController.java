package com.example.cantine.rest;

import com.example.cantine.dto.*;
import com.example.cantine.service.AuthService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentification", description = "API d'authentification pour Android")
@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthRestController {
    private final AuthService authService;

    @Operation(summary = "Connexion et génération du token JWT")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
