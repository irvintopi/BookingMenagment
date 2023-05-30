package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.model.enums.BookingStatusEnum;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    BookingService bookingService;

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .sorted(Comparator.comparing(UserDTO::getFirstName))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> userMapper.toDto(value)).orElse(null);
    }

    @Override
    public User update(String email, User updatedUser) {
        log.info("Updating user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User does not exist!"));


        Integer userId = user.getId();

        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }

        if (updatedUser.getMiddleName() != null) {
            user.setMiddleName(updatedUser.getMiddleName());
        }

        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getAddress() != null) {
            user.setAddress(updatedUser.getAddress());
        }

        if (updatedUser.getPhoneNumber() != null) {
            user.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        if (updatedUser.getBirthday() != null) {
            user.setBirthday(updatedUser.getBirthday());
        }

        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        user.setId(userId);

        return userRepository.save(user);
    }

    @Override
    public ResponseEntity<String> delete(String email) {
        log.info("Deleting user with email: {}", email);
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent() && !userOptional.get().getRole().name().equals("ADMIN")) {
            User user = userOptional.get();
            userRepository.delete(user);

            return ResponseEntity.ok("User deleted successfully.");
        } else {

            return ResponseEntity.badRequest().body("Cannot perform account deletion, email does not exist or can't be deleted.");
        }
    }

    @Override
    public List<UserDTO> findUsersOnFlight(Integer flightId) {
        log.info("Finding users on flight with ID: {}", flightId);
        List<Booking> bookings = bookingService.findByFlightId(flightId);

        return bookings.stream()
                .filter(booking -> !BookingStatusEnum.CANCELED.equals(booking.getStatus())) // Filter out canceled bookings
                .map(booking -> {
                    User user = userRepository.findById(booking.getUser().getId()).orElse(null);
                    return user != null ? userMapper.toDto(user) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
