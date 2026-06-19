package com.bridgelabz.service.impl;
import com.bridgelabz.dto.*;
import com.bridgelabz.entity.Address;
import com.bridgelabz.entity.CustomerProfile;
import com.bridgelabz.entity.User;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.AddressRepository;
import com.bridgelabz.repository.CustomerProfileRepository;
import com.bridgelabz.repository.UserRepository;
import com.bridgelabz.security.SecurityUtils;
import com.bridgelabz.service.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerProfileServiceImpl
        implements CustomerProfileService {

    private final CustomerProfileRepository profileRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public CustomerProfileResponse createProfile(
            CustomerProfileRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        // Check if profile already exists
        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException("Profile already exists");
        }

        CustomerProfile profile = CustomerProfile.builder()
                .phone(request.getPhone())
                .preferenceNotes(request.getPreferenceNotes())
                .user(user)
                .build();

        CustomerProfile saved =
                profileRepository.save(profile);

        return mapProfile(saved);
    }

    @Override
    public CustomerProfileResponse getProfile() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        CustomerProfile profile =
                profileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profile not found"));

        return mapProfile(profile);
    }

    @Override
    public CustomerProfileResponse updateProfile(
            CustomerProfileRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        CustomerProfile profile =
                profileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profile not found"));

        profile.setPhone(request.getPhone());
        profile.setPreferenceNotes(
                request.getPreferenceNotes());

        return mapProfile(
                profileRepository.save(profile));
    }

    @Override
    public AddressResponse addAddress(
            AddressRequest request) {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        CustomerProfile profile =
                profileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profile not found"));

        Address address = Address.builder()
                .line1(request.getLine1())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .isDefault(request.getIsDefault())
                .customerProfile(profile)
                .build();

        Address saved =
                addressRepository.save(address);

        return mapAddress(saved);
    }

    @Override
    public List<AddressResponse> getAddresses() {

        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        CustomerProfile profile =
                profileRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Profile not found"));

        return profile.getAddresses()
                .stream()
                .map(this::mapAddress)
                .toList();
    }

    @Override
    public void deleteAddress(Long addressId) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"));

        addressRepository.delete(address);
    }

    private CustomerProfileResponse mapProfile(
            CustomerProfile profile) {

        return CustomerProfileResponse.builder()
                .id(profile.getId())
                .phone(profile.getPhone())
                .preferenceNotes(
                        profile.getPreferenceNotes())
                .userName(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .build();
    }

    private AddressResponse mapAddress(
            Address address) {

        return AddressResponse.builder()
                .id(address.getId())
                .line1(address.getLine1())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .isDefault(address.getIsDefault())
                .build();
    }
}
