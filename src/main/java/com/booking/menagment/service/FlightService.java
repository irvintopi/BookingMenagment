package com.booking.menagment.service;

import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Flight;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface FlightService {

    Optional<Flight> findById(Integer id);
    FlightDTO save(FlightDTO flightDTO);
    FlightDTO update(Integer flightId, FlightDTO flightDTO);
    List<FlightDTO> searchFlights(String origin, String destination, Date flightDate, String airlineCode);
    void delete(Integer id);
}
