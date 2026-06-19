package com.bridgelabz.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    private Long productId;

    private Integer rating;

    private String comment;
}
