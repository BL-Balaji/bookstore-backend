package com.bridgelabz.service.impl;
import com.bridgelabz.dto.*;
import com.bridgelabz.entity.*;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.*;
import com.bridgelabz.security.SecurityUtils;
import com.bridgelabz.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse addToCart(
            AddToCartRequest request) {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Product product =
                productRepository.findById(
                                request.getProductId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product not found"));

        Cart cart =
                cartRepository.findByUserId(user.getId())
                        .orElseGet(() -> {

                            Cart newCart =
                                    Cart.builder()
                                            .user(user)
                                            .totalAmount(
                                                    BigDecimal.ZERO)
                                            .build();

                            return cartRepository.save(newCart);
                        });

        CartItem cartItem =
                cartItemRepository
                        .findByCartIdAndProductId(
                                cart.getId(),
                                product.getId())
                        .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity()
                            + request.getQuantity());

        } else {

            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(
                            request.getQuantity())
                    .unitPrice(
                            product.getPrice())
                    .build();
        }

        cartItemRepository.save(cartItem);

        return buildCartResponse(cart);
    }

    @Override
    public CartResponse getCart() {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Cart cart =
                cartRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart not found"));

        return buildCartResponse(cart);
    }

    @Override
    public void removeItem(Long cartItemId) {

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart Item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart() {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Cart cart =
                cartRepository.findByUserId(user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart not found"));

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    private CartResponse buildCartResponse(
            Cart cart) {

        List<CartItem> items =
                cart.getItems();

        List<CartItemResponse> itemResponses =
                items.stream()
                        .map(item -> {

                            BigDecimal itemTotal =
                                    item.getUnitPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()));

                            return CartItemResponse
                                    .builder()
                                    .productId(
                                            item.getProduct()
                                                    .getId())
                                    .title(
                                            item.getProduct()
                                                    .getTitle())
                                    .quantity(
                                            item.getQuantity())
                                    .unitPrice(
                                            item.getUnitPrice())
                                    .itemTotal(
                                            itemTotal)
                                    .build();
                        })
                        .toList();

        BigDecimal totalAmount =
                itemResponses.stream()
                        .map(
                                CartItemResponse::getItemTotal)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add);

        cart.setTotalAmount(totalAmount);

        cartRepository.save(cart);

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }
}