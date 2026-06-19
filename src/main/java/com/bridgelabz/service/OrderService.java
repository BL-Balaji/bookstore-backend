package com.bridgelabz.service;
import com.bridgelabz.dto.OrderResponse;
import com.bridgelabz.dto.PlaceOrderRequest;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(
            PlaceOrderRequest request);

    List<OrderResponse> getMyOrders();

    void cancelOrder(Long orderId);

    List<OrderResponse> getAllOrders();
}
