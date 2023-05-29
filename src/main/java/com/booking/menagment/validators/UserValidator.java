package com.booking.menagment.validators;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
@AllArgsConstructor
@Slf4j
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
            log.error("Validation failed: Forbidden. Travellers younger than 18 can only travel under supervision by a parent or guardian");
            throw new IllegalArgumentException("Forbidden. Travellers younger than 18 can only travel under supervision by a parent or guardian");
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (!email.matches(emailRegex)) {
            log.error("Validation failed: Invalid email format.");
            throw new IllegalArgumentException("Invalid email format.");
        }

        boolean emailExists = userRepository.existsByEmail(email);
        if (emailExists) {
            log.error("Validation failed: Email already in use.");
            throw new IllegalArgumentException("Email already in use.");
        }
    }
}
