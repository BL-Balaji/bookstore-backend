package com.bridgelabz.service.impl;

import com.bridgelabz.dto.LoginRequest;
import com.bridgelabz.dto.LoginResponse;
import com.bridgelabz.dto.RegisterRequest;
import com.bridgelabz.dto.UserResponse;
import com.bridgelabz.entity.Role;
import com.bridgelabz.entity.User;
import com.bridgelabz.exception.EmailAlreadyExistsException;
import com.bridgelabz.exception.InvalidCredentialsException;
import com.bridgelabz.repository.UserRepository;
import com.bridgelabz.security.CustomUserDetailsService;
import com.bridgelabz.security.JwtUtil;
import com.bridgelabz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public UserResponse register(
            RegisterRequest request) {

        if(userRepository.existsByEmail(
                request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .role(Role.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser =
                userRepository.save(user);

        return mapToResponse(savedUser);
    }

    private UserResponse mapToResponse(
            User user){

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public LoginResponse login(
            LoginRequest request) {

        User user = userRepository.findByEmail(
                        request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid Email or Password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid Email or Password");
        }

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                request.getEmail());

        String token =
                jwtUtil.generateToken(userDetails);

        return LoginResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
