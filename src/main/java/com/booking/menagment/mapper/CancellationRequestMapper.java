package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.CancellationRequestDTO;
import com.booking.menagment.model.entity.BookingCancellation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CancellationRequestMapper extends AbstractMapper<BookingCancellation, CancellationRequestDTO>{

    @Override
    public BookingCancellation toEntity(CancellationRequestDTO requestDTO) {
        return null;
    }

    @Override
    public CancellationRequestDTO toDto(BookingCancellation bookingCancellation) {
        log.info("Mapping cancellation with number {} to dto", bookingCancellation.getId());
        CancellationRequestDTO cancellationRequestDTO = new CancellationRequestDTO();
        cancellationRequestDTO.setEmail(bookingCancellation.getEmail());
        cancellationRequestDTO.setBookingId(bookingCancellation.getBookingId());
        return cancellationRequestDTO;
    }
}
