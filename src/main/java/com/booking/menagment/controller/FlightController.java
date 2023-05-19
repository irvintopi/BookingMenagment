package com.booking.menagment.controller;

import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@AllArgsConstructor
public class FlightController {

    UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value ="/{flightId}")
    public ResponseEntity<List<UserDTO>> getUsersOnFlight(@PathVariable Integer flightId){
        return ResponseEntity.ok(userService.findUsersOnFlight(flightId));
    }

}
