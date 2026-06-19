package com.bridgelabz.controller;
import com.bridgelabz.dto.*;
import com.bridgelabz.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final CustomerProfileService customerProfileService;

    @PostMapping("/profile")
    public CustomerProfileResponse createProfile(
            @RequestBody CustomerProfileRequest request) {

        return customerProfileService.createProfile(
                request);
    }

    @GetMapping("/profile")
    public CustomerProfileResponse getProfile() {

        return customerProfileService.getProfile();
    }

    @PutMapping("/profile")
    public CustomerProfileResponse updateProfile(
            @RequestBody CustomerProfileRequest request) {

        return customerProfileService.updateProfile(
                request);
    }

    @PostMapping("/address")
    public AddressResponse addAddress(
            @RequestBody AddressRequest request) {

        return customerProfileService.addAddress(
                request);
    }

    @GetMapping("/address")
    public List<AddressResponse> getAddresses() {

        return customerProfileService.getAddresses();
    }

    @DeleteMapping("/address/{id}")
    public String deleteAddress(
            @PathVariable Long id) {

        customerProfileService.deleteAddress(id);

        return "Address deleted successfully";
    }
}
