package com.booking.menagment.model.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private Date birthday;
    private String role;
    private String phoneNumber;
    private String email;
    private String address;
}
