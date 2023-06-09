package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.FlightMapper;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.enums.AirlineEnum;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.service.FlightService;
import com.booking.menagment.validators.FlightValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    FlightRepository flightRepository;
    FlightMapper flightMapper;
    FlightValidator flightValidator;
    BookingRepository bookingRepository;

    @Override
    public Optional<Flight> findById(Integer id) {
        log.info("Finding flight by ID: {}", id);
        return flightRepository.findById(id);
    }

    @Override
    public FlightDTO save(FlightDTO flightDTO) {
        log.info("Saving flight: {}", flightDTO);

        flightValidator.validateFlight(flightDTO);

        try {
            flightRepository.save(flightMapper.toEntity(flightDTO));
            log.info("Flight saved successfully");
        } catch (DataIntegrityViolationException ex) {
            log.error("Flight number already exists for the specified flight date", ex);
            throw new IllegalArgumentException("Flight number already exists for the specified flight date.");
        }

        return flightDTO;
    }

    @Override
    public FlightDTO update(Integer flightID, FlightDTO flightDTO) {
        log.info("Updating flight with ID: {}, new flight details: {}", flightID, flightDTO);

        Flight existingFlight = flightRepository.findById(flightID)
                .orElseThrow(() -> {
                    log.error("Flight not found with ID: {}", flightID);
                    return new NoSuchElementException("Flight not found.");
                });

        // Validate the updated flight DTO
        flightValidator.validateFlight(flightDTO);

        // Check if any traveler has booked the flight
        List<Booking> bookings = bookingRepository.findByFlights(existingFlight);
        if (!bookings.isEmpty()) {
            log.warn("Attempted to modify flight that is booked.");
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
    public List<FlightDTO> searchFlights(String origin, String destination, Date flightDate, String airlineCode) {

        Date currentDate = new Date();
        if (flightDate.before(currentDate)) {
            throw new IllegalArgumentException("Flight date cannot be in the past");
        }

        if (airlineCode != null && !airlineCode.isEmpty()) {
            List<Flight> flights = flightRepository.findFlightsByOriginAndDestinationAndFlightDateAndAirline(origin, destination, flightDate, AirlineEnum.valueOf(airlineCode));
            return flights.stream().map(flightMapper::toDto).collect(Collectors.toList());
        } else {
            List<Flight> flightList = flightRepository.findFlightsByOriginAndDestinationAndFlightDate(origin, destination, flightDate);
            return flightList.stream().map(flightMapper::toDto).collect(Collectors.toList());
        }
    }

    @Override
    public void delete(Integer flightId){
        log.info("Deleting flight with ID: {}", flightId);
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NoSuchElementException("Flight not found"));

        List<Booking> bookings = bookingRepository.findByFlights(flight);
        if (!bookings.isEmpty()) {
            log.warn("Attempted to delete flight with existing bookings, with ID: {}", flightId);
            throw new IllegalArgumentException("Cannot delete a flight with existing bookings");
        }

        flightRepository.delete(flight);
    }
    private boolean isDepartureTimeUpdated(Flight existingFlight, FlightDTO flightDTO) {
        return !existingFlight.getDepartureTime().equals(flightDTO.getDepartureTime());
    }



}
