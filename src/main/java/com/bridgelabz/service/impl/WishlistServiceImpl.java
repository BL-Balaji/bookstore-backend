package com.bridgelabz.service.impl;
import com.bridgelabz.dto.*;
import com.bridgelabz.entity.*;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.*;
import com.bridgelabz.security.SecurityUtils;
import com.bridgelabz.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl
        implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public WishlistResponse addToWishlist(
            WishlistRequest request) {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Product product =
                productRepository.findById(
                                request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        Wishlist wishlist =
                wishlistRepository.findByUserId(user.getId())
                        .orElseGet(() -> {

                            Wishlist newWishlist =
                                    Wishlist.builder()
                                            .user(user)
                                            .build();

                            return wishlistRepository
                                    .save(newWishlist);
                        });

        boolean alreadyExists =
                wishlistItemRepository
                        .findByWishlistIdAndProductId(
                                wishlist.getId(),
                                product.getId())
                        .isPresent();

        if (alreadyExists) {

            return buildResponse(wishlist);
        }

        WishlistItem wishlistItem =
                WishlistItem.builder()
                        .wishlist(wishlist)
                        .product(product)
                        .build();

        wishlistItemRepository.save(wishlistItem);

        if (wishlist.getItems() == null) {
            wishlist.setItems(
                    new ArrayList<>());
        }

        wishlist.getItems().add(wishlistItem);

        return buildResponse(wishlist);
    }

    @Override
    public WishlistResponse getWishlist() {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Wishlist wishlist =
                wishlistRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Wishlist not found"));

        return buildResponse(wishlist);
    }

    @Override
    public void removeFromWishlist(
            Long productId) {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Wishlist wishlist =
                wishlistRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Wishlist not found"));

        WishlistItem wishlistItem =
                wishlistItemRepository
                        .findByWishlistIdAndProductId(
                                wishlist.getId(),
                                productId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Wishlist item not found"));

        wishlistItemRepository.delete(wishlistItem);
    }

    private WishlistResponse buildResponse(
            Wishlist wishlist) {

        List<WishlistItem> items =
                wishlist.getItems() == null
                        ? new ArrayList<>()
                        : wishlist.getItems();

        List<WishlistItemResponse> responses =
                items.stream()
                        .map(item ->
                                WishlistItemResponse.builder()
                                        .productId(
                                                item.getProduct().getId())
                                        .title(
                                                item.getProduct().getTitle())
                                        .price(
                                                item.getProduct().getPrice())
                                        .build())
                        .toList();

        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .items(responses)
                .build();
    }
}