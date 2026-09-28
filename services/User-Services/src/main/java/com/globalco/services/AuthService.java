package com.globalco.services;

import com.globalco.payload.AuthResponse;
import com.globalco.payload.LoginRequest;
import com.globalco.payload.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest signupRequest);
    AuthResponse login(LoginRequest loginRequest);
}
