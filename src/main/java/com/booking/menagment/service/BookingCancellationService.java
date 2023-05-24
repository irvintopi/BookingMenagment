package com.booking.menagment.service;

import com.booking.menagment.model.dto.BookingCancellationDTO;
import com.booking.menagment.model.dto.CancellationDeclineDTO;
import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.BookingCancellation;

import java.util.List;

public interface BookingCancellationService {

    BookingCancellationDTO requestCancellation(CancellationRequestDTO cancellationRequestDTO);

    BookingCancellation approveCancellation(Integer cancellationId);

    BookingCancellation declineCancellation(CancellationDeclineDTO cancellationDeclineDTO);

    List<CancellationRequestDTO> getRequestsByStatus(String status);

    BookingCancellationDTO getRequestsById(Integer cancellationId);
}
