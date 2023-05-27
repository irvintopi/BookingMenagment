package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.repository.BookingRepository;
import com.booking.menagment.repository.FlightRepository;
import com.booking.menagment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingMapper extends AbstractMapper<Booking, BookingDTO>{

        FlightRepository flightRepository;
        UserRepository userRepository;
    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStatus(bookingDTO.getStatus());
        booking.setFlightClass(bookingDTO.getFlightClass());
        booking.setSeatsBooked(bookingDTO.getSeatsBooked());
        booking.setUser(userRepository.findByEmail(bookingDTO.getEmail()).get());
        List<Flight> flights = flightRepository.findAllById(bookingDTO.getFlightIds());
        booking.setFlights(flights);
        return booking;
    }

    @Override
    public BookingDTO toDto(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setEmail(booking.getUser().getEmail());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setBookingDate(booking.getBookingDate());
        List<Integer> flightIds = booking.getFlights().stream()
                .map(Flight::getId)
                .collect(Collectors.toList());
        bookingDTO.setFlightIds(flightIds);
        bookingDTO.setFlightClass(booking.getFlightClass());
        return bookingDTO;
    }


}
