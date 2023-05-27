package com.booking.menagment.validators;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.Booking;
import com.booking.menagment.model.entity.BookingCancellation;
import com.booking.menagment.model.enums.CancellationStatusEnum;
import com.booking.menagment.repository.BookingCancellationRepository;
import com.booking.menagment.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CancellationRequestValidator {
    BookingRepository bookingRepository;
    BookingCancellationRepository cancellationRepository;

    public void validateCancellationRequest(CancellationRequestDTO requestDTO) {
        // Check if booking exists
        Booking booking = bookingRepository.findById(requestDTO.getBookingId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID: " + requestDTO.getBookingId()));

        // Check if the booking belongs to the user with the given email
        if (!booking.getUser().getEmail().equals(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Booking does not belong to the user with email: " + requestDTO.getEmail() + ", contact the person who booked the flight!");
        }

        // Check if authenticated user is sending the request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!username.equals(requestDTO.getEmail()) ) {
            throw new IllegalArgumentException("This is not your email, only account owners can request cancellations.");
        }

        // Check if the booking is in the past
        if (booking.getBookingDate().before(new Date())) {
            throw new IllegalArgumentException("Booking is in the past and cannot be canceled, contact support for help.");
        }

        // Check if there is an existing declined or approved cancellation request for the booking
        Optional<BookingCancellation> existingCancellation = cancellationRepository.findByBookingIdAndStatusIn(requestDTO.getBookingId(), Arrays.asList(CancellationStatusEnum.APPROVED, CancellationStatusEnum.DECLINED, CancellationStatusEnum.PENDING));
        if (existingCancellation.isPresent()) {
            throw new IllegalArgumentException("There is already a cancellation request for this booking with status: " + existingCancellation.get().getStatus().name() +", contact support for help.");
        }
    }
}
