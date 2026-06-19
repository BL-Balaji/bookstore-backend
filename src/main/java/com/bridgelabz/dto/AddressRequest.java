package com.bridgelabz.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequest {

    private String line1;

    private String city;

    private String state;

    private String pincode;

    private Boolean isDefault;
}
