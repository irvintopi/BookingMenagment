package com.booking.menagment.service;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.BookingCancellation;

public interface BookingCancellationService {

    BookingCancellation requestCancellation(CancellationRequestDTO cancellationRequestDTO);

    BookingCancellation approveCancellation(Integer cancellationId, String adminEmail);

    BookingCancellation declineCancellation(Integer cancellationId, String declineReason);
}
