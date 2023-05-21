package com.booking.menagment.service;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findByEmail(String email);
    User update(String email, User updatedUser);
    void delete(String email);
    List<UserDTO> findUsersOnFlight(Integer flightId);
}
