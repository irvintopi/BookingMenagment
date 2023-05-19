package com.booking.menagment.service;

import com.booking.menagment.model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.List;


public interface BookingService {
    List<Booking> findByFlightId(Integer flightId);
}
