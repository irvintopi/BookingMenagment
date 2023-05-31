package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingWithFlightsDTO;
import com.booking.menagment.model.dto.FlightAccessDTO;
import com.booking.menagment.model.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class BookingWithFlightsMapper {

    FlightAccessMapper flightAccessMapper;

    public BookingWithFlightsDTO toDTOWithFlights(Booking booking) {
        log.info("Mapping booking with id {} to DTO including flights", booking.getId());
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
