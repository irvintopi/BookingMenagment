package com.booking.menagment.validators;

import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class MailValidator {

    UserRepository repository;

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        boolean isValidFormat = email.matches(emailRegex);

        if (!isValidFormat) {
            return false;
        }

        Optional<User> existingUser = repository.findByEmail(email);
        return existingUser.isEmpty();
    }
}
