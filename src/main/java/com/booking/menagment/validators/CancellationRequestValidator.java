package com.booking.menagment.validators;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.enums.CancellationStatusEnum;
import com.booking.menagment.repository.BookingCancellationRepository;
import com.booking.menagment.repository.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class CancellationRequestValidator {

    BookingRepository bookingRepository;
    BookingCancellationRepository cancellationRepository;

    public void validateCancellationRequest(CancellationRequestDTO requestDTO) {
        log.debug("Validating cancellation request: {}", requestDTO);

        // Check if booking exists
        Booking booking = bookingRepository.findById(requestDTO.getBookingId())
                .orElseThrow(() -> {
                    String errorMessage = "Invalid booking ID: " + requestDTO.getBookingId();
                    log.error("Cancellation request validation failed. Error: {}", errorMessage);
                    return new IllegalArgumentException(errorMessage);
                });

        // Check if the booking belongs to the user with the given email
        if (!booking.getUser().getEmail().equals(requestDTO.getEmail())) {
            String errorMessage = "Booking does not belong to the user with email: " + requestDTO.getEmail() + ", contact the person who booked the flight!";
            log.error("Cancellation request validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if authenticated user is sending the request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!username.equals(requestDTO.getEmail())) {
            String errorMessage = "This is not your email, only account owners can request cancellations.";
            log.error("Cancellation request validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if the booking is in the past
        if (booking.getBookingDate().before(new Date())) {
            String errorMessage = "Booking is in the past and cannot be canceled, contact support for help.";
            log.error("Cancellation request validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Check if there is an existing declined or approved cancellation request for the booking
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(requestDTO.getBookingId(), Arrays.asList(CancellationStatusEnum.APPROVED, CancellationStatusEnum.DECLINED, CancellationStatusEnum.PENDING));
        if (existingCancellation.isPresent()) {
            String errorMessage = "There is already a cancellation request for this booking with status: " + existingCancellation.get().getStatus().name() + ", contact support for help.";
            log.error("Cancellation request validation failed. Error: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        log.debug("Cancellation request validation passed for request: {}", requestDTO);
    }
}
