package com.booking.menagment.service.impl;

import com.booking.menagment.mapper.BookingMapper;
import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import com.booking.menagment.service.BookingService;
import com.booking.menagment.validators.BookingValidator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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
    public BookingDTO saveBooking(BookingDTO bookingDTO){
            bookingValidator.validate(bookingDTO);
            Booking booking = new Booking();

            booking.setUser(userRepository.findByEmail(bookingDTO.getEmail()).get());
            booking.setBookingDate(bookingDTO.getBookingDate());
            booking.setFlights(flightRepository.findAllById(bookingDTO.getFlightIds()));
            booking.setFlightClass(bookingDTO.getFlightClass());
            booking.setStatus(bookingDTO.getStatus());
            bookingRepository.save(booking);

            return bookingMapper.toDto(booking);
    }
}
