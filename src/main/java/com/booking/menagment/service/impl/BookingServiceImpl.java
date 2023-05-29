package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.BookingMapper;
import com.booking.menagment.mapper.BookingWithFlightsMapper;
import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.BookingWithFlightsDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.validators.BookingValidator;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingValidator bookingValidator;
    BookingWithFlightsMapper bookingWithFlightsMapper;
    FlightRepository flightRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;

    @Override
    public List<Booking> findByFlightId(Integer flightId) {
        log.info("Finding bookings by flight ID: {}", flightId);
        return bookingRepository.findByFlights(flightRepository.findById(flightId).get());
    }

    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
        log.info("Saving booking: {}", bookingDTO);
        bookingValidator.validate(bookingDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!username.equals(bookingDTO.getEmail()) ) {
            throw new IllegalArgumentException("This is not your email, only account owners can make booking.");
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        bookingRepository.save(booking);
        return bookingDTO;
    }

    @Override
    public List<BookingDTO> getBookingsByEmail(String email) {
        log.info("Getting bookings by email: {}", email);
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        } else {
            List<Booking> bookings = bookingRepository.findByUser(user.get());
            if (bookings.isEmpty()) {
                throw new IllegalArgumentException("No bookings associated with this email address.");
            } else {
                return bookings.stream()
                        .map(bookingMapper::toDto)
                        .collect(Collectors.toList());
            }
        }
    }

    @Override
    public Page<BookingWithFlightsDTO> getUserBookings(String userEmail, int pageNumber) {
        log.info("Getting user bookings for email: {}", userEmail);
        int pageSize = 5;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userEmail));

        Page<Booking> bookingsPage = bookingRepository.findByUserOrderByBookingDateDesc(user , pageRequest);
        List<BookingWithFlightsDTO> bookingDTOs = bookingsPage.getContent().stream()
                .map(bookingWithFlightsMapper::toDTOWithFlights)
                .collect(Collectors.toList());

        return new PageImpl<>(bookingDTOs, pageRequest, bookingsPage.getTotalElements());
    }
}
