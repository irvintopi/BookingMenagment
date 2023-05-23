package com.booking.menagment.model.dto;

import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.enums.BookingStatus;
import com.booking.menagment.model.enums.FlightClass;
import jakarta.ws.rs.DefaultValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;
@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDTO {
    private String email;
    private BookingStatus status;
    private Date bookingDate;
    private FlightClass flightClass;
    @DefaultValue("1")
    private Integer seatsBooked;
    private List<Integer> flightIds;
}
