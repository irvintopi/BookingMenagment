package com.booking.menagment.validators;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.entity.User;
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


    public void validate(BookingDTO bookingDTO){
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
            for (Integer flightId : flightIds) {
                Flight flight = flightRepository.findById(flightId).orElse(null);
                if (flight == null) {
                    throw new IllegalArgumentException("Invalid flight ID: " + flightId);
                }

                Date departureDate = flight.getFlightDate();
                if (departureDate.before(currentDate)) {
                    throw new IllegalArgumentException("Flight with ID " + flightId + " has already departed");
                }
            }
        }
    }

