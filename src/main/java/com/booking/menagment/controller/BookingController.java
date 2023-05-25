package com.booking.menagment.controller;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
            bookingService.saveBooking(bookingDTO);
            return ResponseEntity.ok(bookingDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    public ResponseEntity<?> getAllBookingsOfUser(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }
}
