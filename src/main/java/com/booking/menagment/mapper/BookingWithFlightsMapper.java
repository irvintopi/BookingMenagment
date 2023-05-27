package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingWithFlightsDTO;
import com.booking.menagment.model.dto.FlightAccessDTO;
import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Booking;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingWithFlightsMapper {

    FlightAccessMapper flightAccessMapper;

    public BookingWithFlightsDTO toDTOWithFlights(Booking booking) {
        BookingWithFlightsDTO bookingDTO = new BookingWithFlightsDTO();

        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setSeatsBooked(booking.getSeatsBooked());

        List<FlightAccessDTO> flightDTOs = booking.getFlights()
                .stream()
                .map(flightAccessMapper::toDto)
                .collect(Collectors.toList());
        bookingDTO.setFlights(flightDTOs);
        return bookingDTO;
    }

}
