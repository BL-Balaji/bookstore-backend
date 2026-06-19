package com.bridgelabz.service;
import com.bridgelabz.dto.*;

import java.util.List;

public interface CustomerProfileService {

    CustomerProfileResponse createProfile(
            CustomerProfileRequest request);

    CustomerProfileResponse getProfile();

    CustomerProfileResponse updateProfile(
            CustomerProfileRequest request);

    AddressResponse addAddress(
            AddressRequest request);

    List<AddressResponse> getAddresses();

    void deleteAddress(Long addressId);
}