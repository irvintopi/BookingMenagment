package com.booking.menagment.controller;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/booking")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello");
    }
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
            bookingService.saveBooking(bookingDTO);
            return ResponseEntity.ok(bookingDTO);
    }
}
