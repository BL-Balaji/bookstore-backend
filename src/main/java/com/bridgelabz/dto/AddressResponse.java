package com.bridgelabz.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;

    private String line1;

    private String city;

    private String state;

    private String pincode;

    private Boolean isDefault;
}
