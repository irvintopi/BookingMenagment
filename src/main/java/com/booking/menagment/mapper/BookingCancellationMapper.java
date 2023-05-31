package com.booking.menagment.mapper;

import com.booking.menagment.model.dto.BookingCancellationDTO;
import com.booking.menagment.model.entity.BookingCancellation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookingCancellationMapper extends AbstractMapper<BookingCancellation, BookingCancellationDTO>{
    @Override
    public BookingCancellation toEntity(BookingCancellationDTO bookingCancellationDTO) {
        return null;
    }

    @Override
    public BookingCancellationDTO toDto(BookingCancellation bookingCancellation) {
        log.info("Mapping booking cancellation request with id {} to Dto", bookingCancellation.getId());
        BookingCancellationDTO cancellationDTO = new BookingCancellationDTO();
        cancellationDTO.setCancellationId(bookingCancellation.getId());
        cancellationDTO.setBookingId(bookingCancellation.getBookingId());
        cancellationDTO.setStatus(bookingCancellation.getStatus().name());
        cancellationDTO.setReason(bookingCancellation.getDeclineReason());
        return cancellationDTO;
    }
}
