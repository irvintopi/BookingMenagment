package com.booking.menagment.controller;

import com.booking.menagment.mapper.UserMapper;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    UserService userService;
    UserMapper userMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> get(){
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    public ResponseEntity<UserDTO> getByEmail(@PathVariable("email") String email) {
        UserDTO userDTO = userService.findByEmail(email);
        if(userDTO != null) return ResponseEntity.ok(userDTO);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{email}")
    public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody User updatedUser) {
        User savedUser = userService.update(email, updatedUser);
        return ResponseEntity.ok(userMapper.toDto(savedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{email}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable String email) {
        UserDTO userDTO = userService.findByEmail(email);
        if(userDTO != null){
            userService.delete(email);
            return ResponseEntity.ok(userDTO);
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}