package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.FlightMapper;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.service.FlightService;
import com.booking.menagment.validators.FlightValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    FlightMapper flightMapper;
    FlightValidator flightValidator;
    BookingRepository bookingRepository;

    @Override
    public Optional<Flight> findById(Integer id) {
        return flightRepository.findById(id);
    }

    @Override
    public FlightDTO save(FlightDTO flightDTO) {
        flightValidator.validateFlight(flightDTO);

        try {
            flightRepository.save(flightMapper.toEntity(flightDTO));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Flight number already exists for the specified flight date.");
        }

        return flightDTO;
    }

    @Override
    public FlightDTO update(Integer flightID, FlightDTO flightDTO) {
        Flight existingFlight = flightRepository.findById(flightID)
                .orElseThrow(() -> new NoSuchElementException("Flight not found."));

        // Validate the updated flight DTO
        flightValidator.validateFlight(flightDTO);

        // Check if any traveler has booked the flight
        List<Booking> bookings = bookingRepository.findByFlights(existingFlight);
        if (!bookings.isEmpty()) {
            throw new IllegalArgumentException("Flight has been booked by travelers. Only departure time can be updated.");
        }

        // Update the flight details
        BeanUtils.copyProperties(flightDTO, existingFlight);

        // Update the departure time if it is allowed
        if (isDepartureTimeUpdated(existingFlight, flightDTO)) {
            existingFlight.setDepartureTime(flightDTO.getDepartureTime());
        }

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public void delete(Integer flightId){
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NoSuchElementException("Flight not found"));

        List<Booking> bookings = bookingRepository.findByFlights(flight);
        if (!bookings.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a flight with existing bookings");
        }

        flightRepository.delete(flight);
    }
    private boolean isDepartureTimeUpdated(Flight existingFlight, FlightDTO flightDTO) {
        return !existingFlight.getDepartureTime().equals(flightDTO.getDepartureTime());
    }



}
