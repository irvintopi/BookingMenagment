package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.UserService;
import com.booking.menagment.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
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

        UserDTO userDTO = userMapper.toDto(updatedUser);
        userValidator.validateUser(userDTO);


        Integer userId= user.getId();

        BeanUtils.copyProperties(updatedUser, user);

        user.setId(userId);

        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public void delete(String email) {
        userRepository.delete(userRepository.findByEmail(email).get());
    }

    @Override
    public List<UserDTO> findUsersOnFlight(Integer flightId) {
        List<Booking> bookings = bookingService.findByFlightId(flightId);

        return bookings.stream()
                .map(booking -> {
                    User user = userRepository.findById(booking.getUser().getId()).orElse(null);
                    return user != null ? userMapper.toDto(user) : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
