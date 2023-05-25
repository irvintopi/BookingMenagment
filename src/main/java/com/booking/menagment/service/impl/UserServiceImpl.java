package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.model.enums.BookingStatusEnum;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import com.booking.menagment.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserValidator userValidator;
    private PasswordEncoder passwordEncoder;
    private BookingService bookingService;

    @Override
    public List<UserDTO> findAll() {

        return userRepository.findAll().stream()
                .map(userMapper::toDto).sorted(Comparator.comparing(UserDTO::getFirstName)).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> userMapper.toDto(value)).orElse(null);
    }

    @Override
    public User update(String email, User updatedUser) {
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
