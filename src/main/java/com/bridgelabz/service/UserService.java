package com.bridgelabz.service;


import com.bridgelabz.dto.LoginRequest;
import com.bridgelabz.dto.LoginResponse;
import com.bridgelabz.dto.RegisterRequest;
import com.bridgelabz.dto.UserResponse;

public interface UserService {

    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
