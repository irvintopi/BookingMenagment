package com.booking.menagment.service;

import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Flight;

import java.util.Optional;

public interface FlightService {

    Optional<Flight> findById(Integer id);
    FlightDTO save(FlightDTO flightDTO);
    FlightDTO update(String flightNumber, FlightDTO flightDTO);
}
