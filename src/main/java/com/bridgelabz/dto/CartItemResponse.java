package com.bridgelabz.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long productId;

    private String title;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal itemTotal;
}
