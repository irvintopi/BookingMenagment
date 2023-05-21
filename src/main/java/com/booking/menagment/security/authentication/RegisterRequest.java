package com.booking.menagment.security.authentication;

import com.booking.menagment.model.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthday;
    private String email;
    private String phoneNumber;
    private String address;
    private RoleEnum role;
    private String password;
}