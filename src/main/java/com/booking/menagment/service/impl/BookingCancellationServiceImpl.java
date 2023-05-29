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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
@Slf4j
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
        log.info("Retrieving cancellation requests by status: {}", status);
        try {
            CancellationStatusEnum statusEnum = CancellationStatusEnum.valueOf(status.toUpperCase());
            List<BookingCancellation> cancellations = cancellationRepository.findByStatus(statusEnum);

            if (cancellations.isEmpty()) {
                log.warn("No cancellations found with status: {}", status);
                throw new NoSuchElementException("No cancellations found with status: " + status);
            }

            return cancellations.stream()
                    .map(cancellationRequestMapper::toDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException ex) {
            log.error("Invalid cancellation status: {}", status);
            throw new IllegalArgumentException("Invalid cancellation status: " + status);
        }
    }

    @Override
    public BookingCancellationDTO getRequestsById(Integer cancellationId) {
        log.info("Retrieving cancellation request by ID: {}", cancellationId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<BookingCancellation> cancellationOptional = cancellationRepository.findById(cancellationId);

        if (cancellationOptional.isPresent()) {
            Optional<Booking> booking = bookingRepository.findById(cancellationOptional.get().getBookingId());
            BookingCancellation cancellation = cancellationOptional.get();
            if (booking.get().getUser().getUsername().equals(username)) {
                return cancellationMapper.toDto(cancellation);
            } else {
                log.error("User is not authorized to view this cancellation. Cancellation ID: {}", cancellationId);
                throw new IllegalArgumentException("You are not authorized to view this cancellation.");
            }
        } else {
            log.error("Cancellation not found with ID: {}", cancellationId);
            throw new NoSuchElementException("Cancellation not found with ID: " + cancellationId);
        }
    }

    @Override
    public BookingCancellationDTO requestCancellation(CancellationRequestDTO requestDTO) {
        log.info("Processing cancellation request: {}", requestDTO);
        try {
            cancellationRequestValidator.validateCancellationRequest(requestDTO);

            BookingCancellation cancellation = new BookingCancellation();
            cancellation.setBookingId(requestDTO.getBookingId());
            cancellation.setEmail(requestDTO.getEmail());
            cancellation.setStatus(CancellationStatusEnum.PENDING);

            cancellationRepository.save(cancellation);

            BookingCancellationDTO cancellationDTO = cancellationMapper.toDto(cancellation);
            log.info("Cancellation request processed successfully. Cancellation ID: {}", cancellationDTO.getCancellationId());
            return cancellationDTO;
        } catch (IllegalArgumentException ex) {
            log.error("Cancellation request validation failed: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public BookingCancellation approveCancellation(Integer cancellationId) {
        log.info("Approving cancellation request. Cancellation ID: {}", cancellationId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> admin = userRepository.findByEmail(authentication.getName());

        Optional<BookingCancellation> cancellationOptional = cancellationRepository.findById(cancellationId);
        if (cancellationOptional.isEmpty()) {
            log.error("Cancellation not found with ID: {}", cancellationId);
            throw new IllegalArgumentException("Cancellation not found with ID: " + cancellationId);
        }

        BookingCancellation cancellation = cancellationOptional.get();

        // Check if booking has already been approved or declined
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(cancellation.getBookingId(), Arrays.asList(CancellationStatusEnum.APPROVED, CancellationStatusEnum.DECLINED));
        if (existingCancellation.isPresent()) {
            String errorMessage = "Booking has already been " + existingCancellation.get().getStatus().name() + " by admin with id " + cancellation.getAdminID();
            log.error("Failed to approve cancellation. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if the booking has already passed
        Optional<Booking> bookingOptional = bookingRepository.findById(cancellation.getBookingId());
        if (bookingOptional.isEmpty()) {
            log.error("Invalid booking ID: {}", cancellation.getBookingId());
            throw new IllegalArgumentException("Invalid booking ID: " + cancellation.getBookingId());
        }

        Booking booking = bookingOptional.get();
        Date currentDate = new Date();
        if (booking.getBookingDate().before(currentDate)) {
            String errorMessage = "Cannot approve cancellation for a booking that has already passed";
            log.error("Failed to approve cancellation. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        booking.setStatus(BookingStatusEnum.CANCELED);
        bookingRepository.save(booking);
        updateSeatsAfterCancellation(booking);

        cancellation.setStatus(CancellationStatusEnum.APPROVED);
        cancellation.setAdminID(admin.get().getId());
        cancellationRepository.save(cancellation);

        log.info("Cancellation request approved. Cancellation ID: {}", cancellationId);
        return cancellation;
    }

    @Override
    public BookingCancellation declineCancellation(CancellationDeclineDTO declineDTO) {
        log.info("Declining cancellation request: {}", declineDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> admin = userRepository.findByEmail(authentication.getName());

        Optional<BookingCancellation> cancellationOptional = cancellationRepository.findById(declineDTO.getCancellationId());
        if (cancellationOptional.isEmpty()) {
            log.error("Cancellation not found with ID: {}", declineDTO.getCancellationId());
            throw new IllegalArgumentException("Cancellation not found with ID: " + declineDTO.getCancellationId());
        }

        BookingCancellation cancellation = cancellationOptional.get();

        // Check if booking has already been approved or declined
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(cancellation.getBookingId(), Arrays.asList(CancellationStatusEnum.DECLINED, CancellationStatusEnum.APPROVED));
        if (existingCancellation.isPresent()) {
            String errorMessage = "Booking has already been " + existingCancellation.get().getStatus().name() + " by admin with id " + cancellation.getAdminID();
            log.error("Failed to decline cancellation. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        Optional<Booking> bookingOptional = bookingRepository.findById(cancellation.getBookingId());
        if (bookingOptional.isEmpty()) {
            log.error("Invalid booking ID: {}", cancellation.getBookingId());
            throw new IllegalArgumentException("Invalid booking ID: " + cancellation.getBookingId());
        }

        Booking booking = bookingOptional.get();
        Date currentDate = new Date();
        if (booking.getBookingDate().before(currentDate)) {
            String errorMessage = "Booking in cancellation request is in the past";
            log.error("Failed to decline cancellation. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        cancellation.setStatus(CancellationStatusEnum.DECLINED);
        cancellation.setDeclineReason(declineDTO.getReason());
        cancellation.setAdminID(admin.get().getId());
        cancellationRepository.save(cancellation);

        log.info("Cancellation request declined. Cancellation ID: {}", declineDTO.getCancellationId());
        return cancellation;
    }

    private void updateSeatsAfterCancellation(Booking booking) {
        Integer seatsBooked = booking.getSeatsBooked();
        List<Flight> flights = booking.getFlights();
        for (Flight flight : flights) {
            switch (booking.getFlightClass().name()) {
                case "ECONOMY":
                    flight.setSeatsEconomy(flight.getSeatsEconomy() + seatsBooked);
                    break;
                case "PREMIUMECONOMY":
                    flight.setSeatsPremiumEconomy(flight.getSeatsPremiumEconomy() + seatsBooked);
                    break;
                case "BUSINESS":
                    flight.setSeatsBusiness(flight.getSeatsBusiness() + seatsBooked);
                    break;
                case "FIRSTCLASS":
                    flight.setSeatsFirstClass(flight.getSeatsFirstClass() + seatsBooked);
                    break;
                default:
                    String errorMessage = "Invalid flight class: " + booking.getFlightClass();
                    log.error("Failed to update seats after cancellation. Error: {}", errorMessage);
                    throw new IllegalArgumentException(errorMessage);
            }
            flightRepository.save(flight);
        }
    }
}
