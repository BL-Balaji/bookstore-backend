package com.bridgelabz.controller;
import com.bridgelabz.dto.WishlistRequest;
import com.bridgelabz.dto.WishlistResponse;
import com.bridgelabz.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public WishlistResponse addToWishlist(
            @RequestBody WishlistRequest request) {
        System.out.println(
                "Wishlist API Called");
        return wishlistService
                .addToWishlist(request);
    }

    @GetMapping
    public WishlistResponse getWishlist() {

        return wishlistService
                .getWishlist();
    }

    @DeleteMapping("/{productId}")
    public String removeFromWishlist(
            @PathVariable Long productId) {

        wishlistService
                .removeFromWishlist(productId);

        return "Product removed from wishlist";
    }
}
