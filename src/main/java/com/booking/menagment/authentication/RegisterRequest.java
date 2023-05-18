package com.booking.menagment.authentication;

import com.booking.menagment.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String middlename;
    private String lastname;
    private String email;
    private String phoneNumber;
    private String address;
    private RoleEnum role;
    private String password;
}