package com.booking.menagment.validators;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
@AllArgsConstructor
public class UserValidator {

    UserRepository userRepository;
    private static final int MIN_AGE = 18;

    public void validateUser(UserDTO userDTO) {
        validateAge(userDTO.getBirthday());
        validateEmail(userDTO.getEmail());
    }

    private void validateAge(Date birthday) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -MIN_AGE);
        Date minAgeDate = calendar.getTime();

        if (birthday.after(minAgeDate)) {
            throw new IllegalArgumentException("Forbidden. Travellers younger than 18 can only travel under supervision by a parent or guardian");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            throw new IllegalArgumentException("Email already in use.");
        }

    }
}
