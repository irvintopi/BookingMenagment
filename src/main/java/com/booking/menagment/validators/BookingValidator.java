package com.booking.menagment.validators;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingValidator {
    UserRepository userRepository;
    FlightRepository flightRepository;
    BookingRepository bookingRepository;

    public void validate(BookingDTO bookingDTO) {
        Integer previousFlightId = null;

        // Check if email exists
        String email = bookingDTO.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));

        // Check if booking date is in the future
        Date bookingDate = bookingDTO.getBookingDate();
        Date currentDate = new Date();
        if (bookingDate.before(currentDate)) {
            throw new IllegalArgumentException("Booking date must be in the future");
        }

        // Check if flights exist and are in the future
        List<Integer> flightIds = bookingDTO.getFlightIds();
        if (flightIds.isEmpty()) {
            throw new IllegalArgumentException("At least one flight must be specified");
        }

        for (Integer flightId : flightIds) {
            Flight flight = flightRepository.findById(flightId).orElse(null);
            if (flight == null) {
                throw new IllegalArgumentException("Invalid flight ID: " + flightId);
            }

            Date departureDate = flight.getFlightDate();
            if (departureDate.before(currentDate)) {
                throw new IllegalArgumentException("Flight with ID " + flightId + " has already departed");
            }

            // Validate origin and destination
            if (previousFlightId != null) {
                Flight previousFlight = flightRepository.findById(previousFlightId).orElse(null);
                if (previousFlight == null) {
                    throw new IllegalArgumentException("Invalid previous flight ID: " + previousFlightId);
                }

                if (!flight.getOrigin().equals(previousFlight.getDestination())) {
                    throw new IllegalArgumentException("Origin of flight with ID " + flightId + " does not match the destination of the previous flight");
                }
            }

            previousFlightId = flightId;

            // Check if the user has already booked the flight
            Booking existingBooking = bookingRepository.findByUserAndFlights(user, flight);
            if (existingBooking != null) {
                throw new IllegalArgumentException("Booking already created for flight with ID: " + flightId);
            }

            // Deduction logic for seat class
            switch (bookingDTO.getFlightClass().name()) {
                case "ECONOMY" -> {
                    if (flight.getSeatsEconomy() < bookingDTO.getSeatsBooked()) {
                        throw new IllegalArgumentException("Not enough available economy seats for flight with ID " + flightId);
                    }
                    flight.setSeatsEconomy(flight.getSeatsEconomy() - bookingDTO.getSeatsBooked());
                }
                case "BUSINESS" -> {
                    if (flight.getSeatsBusiness() < bookingDTO.getSeatsBooked()) {
                        throw new IllegalArgumentException("Not enough available business seats for flight with ID " + flightId);
                    }
                    flight.setSeatsBusiness(flight.getSeatsBusiness() - bookingDTO.getSeatsBooked());
                }
                case "FIRSTCLASS" -> {
                    if (flight.getSeatsFirstClass() < bookingDTO.getSeatsBooked()) {
                        throw new IllegalArgumentException("Not enough available first class seats for flight with ID " + flightId);
                    }
                    flight.setSeatsFirstClass(flight.getSeatsFirstClass() - bookingDTO.getSeatsBooked());
                }
                case "ECONOMYPREMIUM" -> {
                    if (flight.getSeatsPremiumEconomy() < bookingDTO.getSeatsBooked()) {
                        throw new IllegalArgumentException("Not enough available premium economy seats for flight with ID " + flightId);
                    }
                    flight.setSeatsPremiumEconomy(flight.getSeatsPremiumEconomy() - bookingDTO.getSeatsBooked());
                }
                default ->
                        throw new IllegalArgumentException("Invalid flight class: " + bookingDTO.getFlightClass().name());
            }
        }
    }
}

