package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Flight;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper extends AbstractMapper<Flight, FlightDTO>{
    @Override
    public Flight toEntity(FlightDTO flightDTO) {
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightDTO, flight);

        return flight;
    }
    @Override
    public FlightDTO toDto(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        BeanUtils.copyProperties(flight, flightDTO);

        return flightDTO;
    }
}