package com.booking.menagment.model.dto;

import com.booking.menagment.model.enums.BookingStatus;
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
    private String userName;
    private BookingStatus status;
    private Date bookingDate;
    private List<FlightDTO> flights;
    private List<Integer> flightIds;
}
