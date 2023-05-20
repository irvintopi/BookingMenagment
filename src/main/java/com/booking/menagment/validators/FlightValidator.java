package com.booking.menagment.validators;

import com.booking.menagment.model.dto.FlightDTO;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class FlightValidator {
    public void validateFlight(FlightDTO flightDTO) {
        String airlineCode = flightDTO.getAirline();
        String flightNumber = flightDTO.getFlightNumber();

        validateFutureDate(flightDTO.getFlightDate());
        validateFutureTime(flightDTO.getFlightDate(), flightDTO.getDepartureTime(), "Departure time must be in the future.");

        if (!flightNumber.matches("^" + airlineCode + "\\d{3}$")) {
            throw new IllegalArgumentException("Invalid flight number format. Flight number should start with the airline code followed by three numbers.");
        }

        if (!flightDTO.getOrigin().matches("^[A-Z]{3}$") || !flightDTO.getDestination().matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Invalid airport code format on origin or destination. Airport code should consist of three uppercase letters.");
        }

        if (flightDTO.getOrigin().equals(flightDTO.getDestination())) {
            throw new IllegalArgumentException("Origin and destination airports cannot be the same.");
        }
    }

    private void validateFutureDate(Date date) {
        Date currentDate = new Date();
        if (date.before(currentDate)) {
            throw new IllegalArgumentException("Flight date must be in the future.");
        }
    }

    private void validateFutureTime(Date date, Date time, String errorMessage) {
        Date currentDateTime = new Date();
        Date bookingDateTime = mergeDateAndTime(date, time);

        if (bookingDateTime.before(currentDateTime)) {
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

