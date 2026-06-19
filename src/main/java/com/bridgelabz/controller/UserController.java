package com.bridgelabz.controller;
import com.bridgelabz.dto.LoginRequest;
import com.bridgelabz.dto.LoginResponse;
import com.bridgelabz.dto.RegisterRequest;
import com.bridgelabz.dto.UserResponse;
import com.bridgelabz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponse register(
            @Valid
            @RequestBody RegisterRequest request){

        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid
            @RequestBody LoginRequest request) {

        return userService.login(request);
    }
}
