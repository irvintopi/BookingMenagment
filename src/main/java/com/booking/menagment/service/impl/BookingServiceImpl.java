package com.booking.menagment.service.impl;

import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    FlightService flightService;

    @Override
    public List<Booking> findByFlightId(Integer flightId) {
        return bookingRepository.findByFlights(flightService.findById(flightId).get());
    }
}
