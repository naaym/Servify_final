package com.servify.auth.service;

import com.servify.auth.dto.LoginRequest;
import com.servify.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
