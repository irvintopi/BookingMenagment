package com.booking.menagment.service;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.entity.Booking;

import java.util.List;


public interface BookingService {
    List<Booking> findByFlightId(Integer flightId);

    BookingDTO saveBooking(BookingDTO bookingDTO);
}
