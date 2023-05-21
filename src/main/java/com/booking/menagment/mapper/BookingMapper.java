package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.Flight;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper extends AbstractMapper<Booking, BookingDTO>{

        FlightMapper flightMapper;
    @Override
    public Booking toEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(booking.getBookingDate());
        booking.setStatus(booking.getStatus());
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
