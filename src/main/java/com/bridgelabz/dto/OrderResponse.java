package com.bridgelabz.dto;
import com.bridgelabz.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;

    private BigDecimal totalAmount;

    private LocalDateTime orderDate;

    private OrderStatus status;

    private List<OrderItemResponse> items;
}
