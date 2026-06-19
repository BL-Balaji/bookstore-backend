package com.bridgelabz.service;
import com.bridgelabz.dto.AddToCartRequest;
import com.bridgelabz.dto.CartResponse;

public interface CartService {

    CartResponse addToCart(
            AddToCartRequest request);

    CartResponse getCart();

    void removeItem(Long cartItemId);

    void clearCart();
}
