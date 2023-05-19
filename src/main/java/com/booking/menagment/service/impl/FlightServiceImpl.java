package com.booking.menagment.service.impl;

import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;

    @Override
    public Optional<Flight> findById(Integer id) {
        return flightRepository.findById(id);
    }
}
