package com.booking.menagment.service.impl;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.model.enums.CancellationStatus;
import com.booking.menagment.repository.BookingCancellationRepository;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingCancellationService;
import com.booking.menagment.validators.CancellationRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingCancellationServiceImpl implements BookingCancellationService {
    BookingCancellationRepository cancellationRepository;
    CancellationRequestValidator cancellationRequestValidator;
    BookingRepository bookingRepository;
    UserRepository userRepository;
    FlightRepository flightRepository;

    @Override
    public BookingCancellation requestCancellation(CancellationRequestDTO requestDTO) {
        cancellationRequestValidator.validateCancellationRequest(requestDTO);
        // Implement cancellation request logic
        BookingCancellation cancellation = new BookingCancellation();
        cancellation.setBookingId(requestDTO.getBookingId());
        cancellation.setEmail(requestDTO.getEmail());
        cancellation.setStatus(CancellationStatus.PENDING);

        return cancellationRepository.save(cancellation);
    }

    @Override
    public BookingCancellation approveCancellation(Integer cancellationId, String adminEmail) {
        // Retrieve the cancellation request by ID
        BookingCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cancellation ID: " + cancellationId));

        // Check if the user making the request is an admin
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin email: " + adminEmail));

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new IllegalArgumentException("User with email " + adminEmail + " is not authorized to approve cancellations");
        }

        // Check if the booking has already passed
        Booking booking = bookingRepository.findById(cancellation.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + cancellation.getBookingId()));
        Date currentDate = new Date();
        if (booking.getBookingDate().before(currentDate)) {
            throw new IllegalArgumentException("Cannot approve cancellation for a booking that has already passed");
        }

        // Increment the seats booked on each flight
        Integer seatsBooked = booking.getSeatsBooked();
        List<Flight> flights = booking.getFlights();
        for (Flight flight : flights) {
            switch (booking.getFlightClass().name()) {
                case "ECONOMY" -> flight.setSeatsEconomy(flight.getSeatsEconomy() + seatsBooked);
                case "PREMIUMECONOMY" -> flight.setSeatsPremiumEconomy(flight.getSeatsPremiumEconomy() + seatsBooked);
                case "BUSINESS" -> flight.setSeatsBusiness(flight.getSeatsBusiness() + seatsBooked);
                case "FIRSTCLASS" -> flight.setSeatsFirstClass(flight.getSeatsFirstClass() + seatsBooked);
                default -> throw new IllegalArgumentException("Invalid flight class: " + booking.getFlightClass());
            }
            flightRepository.save(flight);
        }

        cancellation.setStatus(CancellationStatus.APPROVED);
        cancellation.setAdminID(admin.getId());
        cancellationRepository.save(cancellation);
        return cancellation;
    }

    @Override
    public BookingCancellation declineCancellation(Integer cancellationId, String declineReason) {

        return new BookingCancellation();
    }
}
