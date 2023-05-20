package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import com.booking.menagment.validators.MailValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private BookingService bookingService;
    private MailValidator mailValidator;

    @Override
    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByEmail(String email) {
        Optional<User> user = repository.findByEmail(email);
        return user.map(value -> userMapper.toDto(value)).orElse(null);
    }

    @Override
    public User update(String email, User updatedUser) {
        if (!mailValidator.isValidEmail(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email is invalid or already in use!");
        }

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User does not exist!"));

        Integer userId= user.getId();

        BeanUtils.copyProperties(updatedUser, user);

        user.setId(userId);

        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return repository.save(user);
    }

    @Override
    public void delete(String email) {
        repository.delete(repository.findByEmail(email).get());
    }

    @Override
    public List<UserDTO> findUsersOnFlight(Integer flightId) {
        List<Booking> bookings = bookingService.findByFlightId(flightId);

        return bookings.stream()
                .map(booking -> {
                    User user = repository.findById(booking.getUser().getId()).orElse(null);
                    return user != null ? userMapper.toDto(user) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
