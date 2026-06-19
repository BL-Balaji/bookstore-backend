package com.bridgelabz.service.impl;
import com.bridgelabz.dto.*;
import com.bridgelabz.entity.*;
import com.bridgelabz.exception.ResourceNotFoundException;
import com.bridgelabz.repository.*;
import com.bridgelabz.security.SecurityUtils;
import com.bridgelabz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl
        implements OrderService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponse placeOrder(
            PlaceOrderRequest request) {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        Address address =
                addressRepository.findById(
                                request.getAddressId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Address not found"));

        Cart cart =
                cartRepository.findByUserId(
                                user.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cart not found"));

        if(cart.getItems().isEmpty()){
            throw new RuntimeException(
                    "Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .address(address)
                .totalAmount(
                        cart.getTotalAmount())
                .orderDate(
                        LocalDateTime.now())
                .status(
                        OrderStatus.PENDING)
                .build();

        Order savedOrder =
                orderRepository.save(order);

        for(CartItem cartItem :
                cart.getItems()) {

            Product product =
                    cartItem.getProduct();

            if(product.getStockQuantity()
                    < cartItem.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient stock for "
                                + product.getTitle());
            }

            product.setStockQuantity(
                    product.getStockQuantity()
                            - cartItem.getQuantity());

            productRepository.save(product);

            OrderItem orderItem =
                    OrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .quantity(
                                    cartItem.getQuantity())
                            .price(
                                    cartItem.getUnitPrice())
                            .build();

            orderItemRepository.save(orderItem);

            savedOrder.getItems()
                    .add(orderItem);
        }

        cart.getItems().clear();

        cart.setTotalAmount(
                java.math.BigDecimal.ZERO);

        cartRepository.save(cart);

        return mapOrder(savedOrder);
    }

    @Override
    public List<OrderResponse> getMyOrders() {

        String email =
                SecurityUtils.getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"));

        return orderRepository
                .findByUserId(user.getId())
                .stream()
                .map(this::mapOrder)
                .toList();
    }

    @Override
    public void cancelOrder(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"));

        order.setStatus(
                OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapOrder)
                .toList();
    }

    private OrderResponse mapOrder(
            Order order) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item ->
                                OrderItemResponse.builder()
                                        .productName(
                                                item.getProduct()
                                                        .getTitle())
                                        .quantity(
                                                item.getQuantity())
                                        .price(
                                                item.getPrice())
                                        .build())
                        .toList();

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(
                        order.getTotalAmount())
                .orderDate(
                        order.getOrderDate())
                .status(
                        order.getStatus())
                .items(items)
                .build();
    }
}
