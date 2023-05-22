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

            //deduction logic
                List<Integer> flightIdList = bookingDTO.getFlightIds();
                for (Integer flightId1 : flightIdList) {
                    Flight flight1 = flightRepository.findById(flightId1)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid flight ID: " + flightId1));

                    switch (bookingDTO.getFlightClass().name()) {
                        case "ECONOMY":
                            if (flight1.getSeatsEconomy() <= 0) {
                                throw new IllegalArgumentException("No available economy seats for flight with ID " + flightId);
                            }
                            flight1.setSeatsEconomy(flight1.getSeatsEconomy() - 1);
                            break;
                        case "BUSINESS":
                            if (flight1.getSeatsBusiness() <= 0) {
                                throw new IllegalArgumentException("No available business seats for flight with ID " + flightId);
                            }
                            flight1.setSeatsBusiness(flight1.getSeatsBusiness() - 1);
                            break;
                        case "FIRSTCLASS":
                            if (flight1.getSeatsFirstClass() <= 0) {
                                throw new IllegalArgumentException("No available first class seats for flight with ID " + flightId);
                            }
                            flight1.setSeatsFirstClass(flight1.getSeatsFirstClass() - 1);
                            break;
                        case "ECONOMYPREMIUM":
                            if (flight1.getSeatsPremiumEconomy() <= 0) {
                                throw new IllegalArgumentException("No available premium economy seats for flight with ID " + flightId);
                            }
                            flight1.setSeatsPremiumEconomy(flight1.getSeatsPremiumEconomy() - 1);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid flight class: " + bookingDTO.getFlightClass().name());
                    }
                }
        }
    }
}

