package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.BookingMapper;
import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.User;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.validators.BookingValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;
    BookingValidator bookingValidator;
    FlightRepository flightRepository;
    UserRepository userRepository;
    BookingMapper bookingMapper;
    @Override
    public List<Booking> findByFlightId(Integer flightId) {
        return bookingRepository.findByFlights(flightRepository.findById(flightId).get());
    }

    @Override
    public BookingDTO saveBooking(BookingDTO bookingDTO) {
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
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) throw new IllegalArgumentException("User not found");
        else {
            List<Booking> bookings = bookingRepository.findByUser(user.get());
            if (bookings.isEmpty())
                throw new IllegalArgumentException("No bookings associated with this email address.");
            else return bookings.stream()
                    .map(bookingMapper::toDto)
                    .collect(Collectors.toList());
             }
    }
}
