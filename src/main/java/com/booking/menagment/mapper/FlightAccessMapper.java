package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.FlightAccessDTO;
import com.booking.menagment.model.entity.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlightAccessMapper {
    public FlightAccessDTO toDto(Flight flight){
        log.info("Mapping flight with number {} to DTO", flight.getFlightNumber());
        FlightAccessDTO flightAccessDTO = new FlightAccessDTO();
        BeanUtils.copyProperties(flight, flightAccessDTO);
        flightAccessDTO.setAirline(flight.getAirline().name());
        flightAccessDTO.setStatus(flight.getStatus().name());
        flightAccessDTO.setDepartureDate(flight.getFlightDate());
        return flightAccessDTO;
    }
}
