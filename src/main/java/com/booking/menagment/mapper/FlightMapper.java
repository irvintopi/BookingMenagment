package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.FlightDTO;
import com.booking.menagment.model.entity.Flight;
import com.booking.menagment.model.enums.AirlineEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlightMapper extends AbstractMapper<Flight, FlightDTO>{
    @Override
    public Flight toEntity(FlightDTO flightDTO) {
        log.info("Mapping flight with number {} to entity", flightDTO.getFlightNumber());
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightDTO, flight);
        flight.setAirline(AirlineEnum.valueOf(flightDTO.getAirline()));
        return flight;
    }
    @Override
    public FlightDTO toDto(Flight flight) {
        log.info("Mapping flight with id {} to dto", flight.getId());
        FlightDTO flightDTO = new FlightDTO();
        BeanUtils.copyProperties(flight, flightDTO);
        flightDTO.setAirline(flight.getAirline().name());
        return flightDTO;
    }
}