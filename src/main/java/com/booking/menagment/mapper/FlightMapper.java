package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Flight;

public class FlightMapper extends AbstractMapper<Flight, FlightDTO>{
    @Override
    public Flight toEntity(FlightDTO flightDTO) {
        Flight flight = new Flight();
        flight.setFlightNumber(flightDTO.getFlightNumber());
        flight.setAirline(flightDTO.getAirline());
        flight.setOrigin(flightDTO.getOrigin());
        flight.setDestination(flightDTO.getDestination());
        flight.setFlightDate(flightDTO.getFlightDate());
        flight.setDepartureTime(flightDTO.getDepartureTime());
        flight.setAircraftType(flightDTO.getAircraftType());
        flight.setStatus(flightDTO.getStatus());
        return flight;
    }
    @Override
    public FlightDTO toDto(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setAirline(flight.getAirline());
        flightDTO.setOrigin(flight.getOrigin());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setFlightDate(flight.getFlightDate());
        flightDTO.setStatus(flight.getStatus());
        flightDTO.setDepartureTime(flight.getDepartureTime());
        flightDTO.setAircraftType(flight.getAircraftType());
        flightDTO.setFlightNumber(flight.getFlightNumber());
        return flightDTO;
    }
}