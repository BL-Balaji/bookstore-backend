package com.bridgelabz.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String title;

    private String author;

    private String isbn;

    private BigDecimal price;

    private Integer stockQuantity;

    private String imageUrl;

    private String categoryName;
}
