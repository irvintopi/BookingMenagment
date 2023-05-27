package com.booking.menagment.service;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.BookingWithFlightsDTO;
import com.booking.menagment.model.entity.Booking;
import org.springframework.data.domain.Page;

import java.util.List;


public interface BookingService {
    List<Booking> findByFlightId(Integer flightId);

    BookingDTO saveBooking(BookingDTO bookingDTO);

    List<BookingDTO> getBookingsByEmail(String email);

    Page<BookingWithFlightsDTO> getUserBookings(String userEmail, int page);
}
