package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.BookingCancellation;
import org.springframework.stereotype.Component;

@Component
public class CancellationRequestMapper extends AbstractMapper<BookingCancellation, CancellationRequestDTO>{

    @Override
    public BookingCancellation toEntity(CancellationRequestDTO requestDTO) {
        return null;
    }

    @Override
    public CancellationRequestDTO toDto(BookingCancellation bookingCancellation) {
        CancellationRequestDTO cancellationRequestDTO = new CancellationRequestDTO();
        cancellationRequestDTO.setEmail(bookingCancellation.getEmail());
        cancellationRequestDTO.setBookingId(bookingCancellation.getBookingId());
        return cancellationRequestDTO;
    }
}
