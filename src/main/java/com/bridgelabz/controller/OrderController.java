package com.bridgelabz.controller;
import com.bridgelabz.dto.OrderResponse;
import com.bridgelabz.dto.PlaceOrderRequest;
import com.bridgelabz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse placeOrder(
            @RequestBody PlaceOrderRequest request) {

        return orderService.placeOrder(request);
    }

    @GetMapping("/my")
    public List<OrderResponse> getMyOrders() {

        return orderService.getMyOrders();
    }

    @PutMapping("/cancel/{id}")
    public String cancelOrder(
            @PathVariable Long id) {

        orderService.cancelOrder(id);

        return "Order cancelled successfully";
    }

    @GetMapping("/all")
    public List<OrderResponse> getAllOrders() {

        return orderService.getAllOrders();
    }
}
