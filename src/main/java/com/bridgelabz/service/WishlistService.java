package com.bridgelabz.service;
import com.bridgelabz.dto.WishlistRequest;
import com.bridgelabz.dto.WishlistResponse;

public interface WishlistService {

    WishlistResponse addToWishlist(
            WishlistRequest request);

    WishlistResponse getWishlist();

    void removeFromWishlist(Long productId);
}
