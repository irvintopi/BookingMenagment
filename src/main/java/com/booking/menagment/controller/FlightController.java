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
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/flights")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class FlightController {

    UserService userService;
    FlightService flightService;
    FlightMapper flightMapper;

    @RequestMapping(method = RequestMethod.GET, value ="/{flightId}")
    public ResponseEntity<?> getUsersOnFlight(@PathVariable Integer flightId){
        List<UserDTO> users = userService.findUsersOnFlight(flightId);
        if (users.isEmpty()) throw new NoSuchElementException("No travelers on board at this time!");
        else return ResponseEntity.ok(users);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createFlight(@RequestBody FlightDTO flightDTO) {
        FlightDTO savedFlight = flightService.save(flightDTO);
        return ResponseEntity.ok(savedFlight);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{flightId}")
    public ResponseEntity<?> updateFlight(@PathVariable Integer flightId, @RequestBody FlightDTO flightDTO) {
        flightService.update(flightId, flightDTO);
        return ResponseEntity.ok().body(flightDTO);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable Integer id) {
        flightService.delete(id);
        return ResponseEntity.ok("Deleted flight Successfully");
    }
}
