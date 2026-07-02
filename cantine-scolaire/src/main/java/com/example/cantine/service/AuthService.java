package com.example.cantine.service;

import com.example.cantine.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest req);
}
