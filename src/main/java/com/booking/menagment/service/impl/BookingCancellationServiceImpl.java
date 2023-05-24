package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.BookingCancellationMapper;
import com.booking.menagment.mapper.CancellationRequestMapper;
import com.booking.menagment.model.dto.BookingCancellationDTO;
import com.booking.menagment.model.dto.CancellationDeclineDTO;
import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.model.enums.BookingStatusEnum;
import com.booking.menagment.model.enums.CancellationStatusEnum;
import com.booking.menagment.repository.BookingCancellationRepository;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingCancellationService;
import com.booking.menagment.validators.CancellationRequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingCancellationServiceImpl implements BookingCancellationService {
    BookingCancellationRepository cancellationRepository;
    BookingRepository bookingRepository;
    BookingCancellationMapper cancellationMapper;
    CancellationRequestMapper cancellationRequestMapper;
    CancellationRequestValidator cancellationRequestValidator;
    UserRepository userRepository;
    FlightRepository flightRepository;

    @Override
    public List<CancellationRequestDTO> getRequestsByStatus(String status) {
        try {
            CancellationStatusEnum statusEnum = CancellationStatusEnum.valueOf(status.toUpperCase());
            List<BookingCancellation> cancellations = cancellationRepository.findByStatus(statusEnum);

            if (cancellations.isEmpty()) {
                throw new NoSuchElementException("No cancellations found with status: " + status);
            }

            return cancellations.stream()
                    .map(cancellationRequestMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid cancellation status: " + status);
        }
    }

    @Override
    public BookingCancellationDTO getRequestsById(Integer cancellationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<BookingCancellation> cancellationOptional = cancellationRepository.findById(cancellationId);

        if (cancellationOptional.isPresent()) {
            Optional<Booking> booking = bookingRepository.findById(cancellationOptional.get().getBookingId());
            BookingCancellation cancellation = cancellationOptional.get();
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||
                    booking.get().getUser().getUsername().equals(username)) {
                return cancellationMapper.toDto(cancellation);
            } else {
                throw new IllegalArgumentException("You are not authorized to view this cancellation.");
            }
        } else {
            throw new NoSuchElementException("Cancellation not found with ID: " + cancellationId);
        }
    }

    @Override
    public BookingCancellationDTO requestCancellation(CancellationRequestDTO requestDTO) {
        cancellationRequestValidator.validateCancellationRequest(requestDTO);

        BookingCancellation cancellation = new BookingCancellation();
        cancellation.setBookingId(requestDTO.getBookingId());
        cancellation.setEmail(requestDTO.getEmail());
        cancellation.setStatus(CancellationStatusEnum.PENDING);

        cancellationRepository.save(cancellation);

        return cancellationMapper.toDto(cancellation);
    }

    @Override
    public BookingCancellation approveCancellation(Integer cancellationId) {
        // Retrieve the cancellation request by ID
        BookingCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cancellation ID: " + cancellationId));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> admin = userRepository.findByEmail(authentication.getName());


        // Check if booking has already been approved or declined
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(cancellation.getBookingId(), Arrays.asList(CancellationStatusEnum.APPROVED, CancellationStatusEnum.DECLINED));
        if (existingCancellation.isPresent()) {
            throw new IllegalArgumentException("Booking has already been " + existingCancellation.get().getStatus().name() + " by admin with id" + cancellation.getAdminID());
        }

        // Check if the booking has already passed
        Booking booking = bookingRepository.findById(cancellation.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + cancellation.getBookingId()));
        Date currentDate = new Date();
        if (booking.getBookingDate().before(currentDate)) {
            throw new IllegalArgumentException("Cannot approve cancellation for a booking that has already passed");
        }
        booking.setStatus(BookingStatusEnum.CANCELED);
        bookingRepository.save(booking);

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

        cancellation.setStatus(CancellationStatusEnum.APPROVED);
        cancellation.setAdminID(admin.get().getId());
        cancellationRepository.save(cancellation);
        return cancellation;
    }

    @Override
    public BookingCancellation declineCancellation(CancellationDeclineDTO declineDTO) {
        // Retrieve the cancellation request by ID
        BookingCancellation cancellation = cancellationRepository.findById(declineDTO.getCancellationId())
                .orElseThrow(() -> new IllegalArgumentException("Cancellation Id not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> admin = userRepository.findByEmail(authentication.getName());

        // Check if booking has already been approved or declined
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(cancellation.getBookingId(), Arrays.asList(CancellationStatusEnum.DECLINED, CancellationStatusEnum.APPROVED));
        if (existingCancellation.isPresent()) {
            throw new IllegalArgumentException("Booking has already been " + existingCancellation.get().getStatus().name() + " by admin with id" + cancellation.getAdminID());
        }

        // Check if the booking has already passed
        Booking booking = bookingRepository.findById(cancellation.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + cancellation.getBookingId()));
        Date currentDate = new Date();
        if (booking.getBookingDate().before(currentDate)) {
            throw new IllegalArgumentException("Booking in cancellation request is in the past");
        }

        // Decline request
        cancellation.setStatus(CancellationStatusEnum.DECLINED);
        cancellation.setDeclineReason(declineDTO.getReason());
        cancellation.setAdminID(admin. get().getId());
        return cancellation;
    }

}
