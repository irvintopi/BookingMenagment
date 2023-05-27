package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.FlightAccessDTO;
import com.booking.menagment.model.entity.Flight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FlightAccessMapper {
    public FlightAccessDTO toDto(Flight flight){
        FlightAccessDTO flightAccessDTO = new FlightAccessDTO();
        BeanUtils.copyProperties(flight, flightAccessDTO);
        flightAccessDTO.setAirline(flight.getAirline().name());
        flightAccessDTO.setStatus(flight.getStatus().name());
        flightAccessDTO.setDepartureDate(flight.getFlightDate());
        return flightAccessDTO;
    }
}
