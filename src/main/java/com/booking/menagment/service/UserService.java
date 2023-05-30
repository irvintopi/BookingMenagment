package com.booking.menagment.service;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findByEmail(String email);
    User update(String email, User updatedUser);
    ResponseEntity<String> delete(String email);
    List<UserDTO> findUsersOnFlight(Integer flightId);
}
