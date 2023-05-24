package com.booking.menagment.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class BookingCancellationDTO {
    Integer cancellationId;
    Integer bookingId;
    String status;
    String reason;
}
