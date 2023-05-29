package com.booking.menagment.validators;

import com.booking.menagment.model.dto.FlightDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class FlightValidator {

    public void validateFlight(FlightDTO flightDTO) {
        log.info("Validating flight: {}", flightDTO);

        String airlineCode = flightDTO.getAirline();
        String flightNumber = flightDTO.getFlightNumber();

        // Airline code
        if (!Arrays.asList("LH", "OS", "LX", "EW").contains(airlineCode)) {
            String errorMessage = "Invalid airline code. Allowed airline codes are LH, OS, LX, EW.";
            log.error("Flight validation failed for flight: {}. Error: {}", flightDTO, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        validateFutureDate(flightDTO.getFlightDate());
        validateFutureTime(flightDTO.getFlightDate(), flightDTO.getDepartureTime());

        if (!flightNumber.matches("^" + airlineCode + "\\d{3}$")) {
            String errorMessage = "Invalid flight number format. Flight number should start with the airline code followed by three numbers.";
            log.error("Flight validation failed for flight: {}. Error: {}", flightDTO, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (!flightDTO.getOrigin().matches("^[A-Z]{3}$") || !flightDTO.getDestination().matches("^[A-Z]{3}$")) {
            String errorMessage = "Invalid airport code format on origin or destination. Airport code should consist of three uppercase letters.";
            log.error("Flight validation failed for flight: {}. Error: {}", flightDTO, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (flightDTO.getOrigin().equals(flightDTO.getDestination())) {
            String errorMessage = "Origin and destination airports cannot be the same.";
            log.error("Flight validation failed for flight: {}. Error: {}", flightDTO, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        log.info("Flight validation passed for flight: {}", flightDTO);
    }

    private void validateFutureDate(Date date) {
        Date currentDate = new Date();
        if (date.before(currentDate)) {
            String errorMessage = "Flight date must be in the future.";
            log.error("Flight date validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateFutureTime(Date date, Date time) {
        Date currentDateTime = new Date();
        Date flightDateTime = mergeDateAndTime(date, time);

        if (flightDateTime.before(currentDateTime)) {
            String errorMessage = "Departure time must be in the future.";
            log.error("Departure time validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Date mergeDateAndTime(Date date, Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(time);

        calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));

        return calendar.getTime();
    }
}

