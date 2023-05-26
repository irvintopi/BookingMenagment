package com.booking.menagment.repository;

import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.enums.AirlineEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Optional<Flight> findById(Integer flightId);
    List<Flight> findFlightsByOriginAndDestinationAndFlightDate(String origin, String destination, Date flightDate);
    List<Flight> findFlightsByOriginAndDestinationAndFlightDateAndAirline(String origin, String destination, Date flightDate, AirlineEnum airline);
}
