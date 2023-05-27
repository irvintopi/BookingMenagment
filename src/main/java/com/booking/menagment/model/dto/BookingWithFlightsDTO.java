package com.booking.menagment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingWithFlightsDTO {
    private Date bookingDate;
    private Integer seatsBooked;
    private List<FlightAccessDTO> flights;
}
