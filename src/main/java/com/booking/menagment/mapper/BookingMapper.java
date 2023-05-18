package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingDTO;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;

import java.util.List;
import java.util.stream.Collectors;

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
        List<FlightDTO> flightDTOs = booking.getFlights().stream()
                .map(flightMapper::toDto)
                .collect(Collectors.toList());
        bookingDTO.setFlights(flightDTOs);
        return bookingDTO;
    }
}
