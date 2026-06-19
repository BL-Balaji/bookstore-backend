package com.bridgelabz.dto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileResponse {

    private Long id;

    private String phone;

    private String preferenceNotes;

    private String userName;

    private String email;
}
