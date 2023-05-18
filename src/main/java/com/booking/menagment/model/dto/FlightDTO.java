package com.booking.menagment.model.dto;


import com.booking.menagment.model.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.util.Date;


@Component
@Data
public class FlightDTO {
    private String airline;
    private String origin;
    private String destination;
    private FlightStatus status;
    private Date flightDate;
    private Time departureTime;
    private String aircraftType;
    private String flightNumber;
}
