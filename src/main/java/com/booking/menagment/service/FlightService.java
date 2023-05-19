package com.booking.menagment.service;

import com.booking.menagment.model.entity.Flight;

import java.util.Optional;

public interface FlightService {

    Optional<Flight> findById(Integer id);
}
