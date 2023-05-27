package com.booking.menagment.controller;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.BookingWithFlightsDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.service.BookingService;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
@AllArgsConstructor
public class BookingController {
    private BookingService bookingService;

    @RequestMapping(method = RequestMethod.GET, value = "/user/{email}")
    public ResponseEntity<Page<BookingWithFlightsDTO>> getUserBookings(
            @PathVariable("email") String email,
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        String loggedInUserEmail = authentication.getName();
        if (!loggedInUserEmail.equals(email)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Page<BookingWithFlightsDTO> bookings = bookingService.getUserBookings(email, page);
        return ResponseEntity.ok(bookings);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{email}")
    public ResponseEntity<?> getAllBookingsOfUser(@PathVariable String email) {
        return ResponseEntity.ok(bookingService.getBookingsByEmail(email));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createBooking(@RequestBody BookingDTO bookingDTO) {
        bookingService.saveBooking(bookingDTO);
        return ResponseEntity.ok(bookingDTO);
    }
}
