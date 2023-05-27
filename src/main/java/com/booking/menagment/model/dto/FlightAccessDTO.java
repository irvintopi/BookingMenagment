package com.booking.menagment.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightAccessDTO {
    String airline;
    String origin;
    String destination;
    String status;
    Date departureDate;
    Date departureTime;
}
