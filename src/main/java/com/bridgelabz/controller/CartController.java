package com.bridgelabz.controller;
import com.bridgelabz.dto.AddToCartRequest;
import com.bridgelabz.dto.CartResponse;
import com.bridgelabz.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public CartResponse addToCart(
            @RequestBody AddToCartRequest request) {

        return cartService.addToCart(request);
    }

    @GetMapping
    public CartResponse getCart() {

        return cartService.getCart();
    }

    @DeleteMapping("/remove/{id}")
    public String removeItem(
            @PathVariable Long id) {

        cartService.removeItem(id);

        return "Item removed successfully";
    }

    @DeleteMapping("/clear")
    public String clearCart() {

        cartService.clearCart();

        return "Cart cleared successfully";
    }
}