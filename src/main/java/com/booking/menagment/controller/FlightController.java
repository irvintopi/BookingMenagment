package com.booking.menagment.controller;

import com.booking.menagment.mapper.FlightMapper;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.dto.UserDTO;
import com.booking.menagment.service.FlightService;
import com.booking.menagment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@AllArgsConstructor
public class FlightController {

    UserService userService;
    FlightService flightService;
    FlightMapper flightMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value ="/{flightId}")
    public ResponseEntity<List<UserDTO>> getUsersOnFlight(@PathVariable Integer flightId){
        return ResponseEntity.ok(userService.findUsersOnFlight(flightId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createFlight(@RequestBody FlightDTO flightDTO) {
        try {
            FlightDTO savedFlight = flightService.save(flightDTO);
            return ResponseEntity.ok(savedFlight);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

}
